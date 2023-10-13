package com.nio.ngfs.plm.bom.configuration.application.task.productconfig;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.enums.YesOrNoEnum;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.GsonUtils;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.enums.ProductConfigOptionTypeEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigModelOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigModelOptionEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigOptionEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.ImportProductConfigOptionRespDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * 同步Product Config勾选历史数据
 *
 * @author xiaozhou.tu
 * @date 2023/10/12
 */
@Component
@RequiredArgsConstructor
public class ImportProductConfigOptionTask {

    /**
     * 固定表头
     */
    private static final List<String> FIXED_HEAD_NAME_LIST = Lists.newArrayList(
            "Feature Code", "Feature Display Name", "Option Code", "Option Display Name"
    );
    private static final String SELECT_TEXT = "√";

    private final BomsProductConfigDao bomsProductConfigDao;
    private final BomsProductConfigOptionDao bomsProductConfigOptionDao;
    private final BomsProductConfigModelOptionDao bomsProductConfigModelOptionDao;

    public ImportProductConfigOptionRespDto execute(MultipartFile file) {
        // 读取历史数据
        List<ProductConfigHistory> productConfigHistoryList = readData(file);
        // 导入打点
        productConfigHistoryList.forEach(this::importProductConfigHistory);
        return new ImportProductConfigOptionRespDto();
    }

    /**
     * 导入打点
     */
    private void importProductConfigHistory(ProductConfigHistory productConfigHistory) {
        // 校验PC Name存在
        BomsProductConfigEntity productConfigEntity = bomsProductConfigDao.getByName(productConfigHistory.getPcName());
        if (productConfigEntity == null) {
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_PRODUCT_CONFIG_OPTION_IMPORT_ERROR, "PC Name Not Exist");
        }
        // 构建BomsProductConfigOptionEntity
        List<BomsProductConfigOptionEntity> optionEntityList = LambdaUtil.map(productConfigHistory.getProductConfigOptionHistoryList(),
                i -> buildProductConfigOptionEntity(productConfigEntity, i));
        // 校验打点的Feature/Option行必须存在
        List<BomsProductConfigModelOptionEntity> modelOptionEntityList = bomsProductConfigModelOptionDao.queryByModel(productConfigEntity.getModelCode());
        Set<String> modelOptionCodeSet = Sets.newHashSet(LambdaUtil.map(modelOptionEntityList, BomsProductConfigModelOptionEntity::getOptionCode));
        List<String> notExistedModelOptionCodeList = optionEntityList.stream().map(BomsProductConfigOptionEntity::getOptionCode)
                .filter(optionCode -> !modelOptionCodeSet.contains(optionCode)).toList();
        if (CollectionUtils.isNotEmpty(notExistedModelOptionCodeList)) {
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_PRODUCT_CONFIG_OPTION_IMPORT_ERROR, "Model Feature/Option Row Not Exist OptionCodeList=" + GsonUtils.toJson(notExistedModelOptionCodeList));
        }
        // 打点已存在，赋值id
        List<BomsProductConfigOptionEntity> existOptionEntityList = bomsProductConfigOptionDao.queryByPcId(productConfigEntity.getId());
        Map<String, BomsProductConfigOptionEntity> existOptionEntityMap = LambdaUtil.toKeyMap(existOptionEntityList, BomsProductConfigOptionEntity::getOptionCode);
        optionEntityList.forEach(optionEntity ->
                Optional.ofNullable(existOptionEntityMap.get(optionEntity.getOptionCode())).ifPresent(existOptionEntity ->
                        optionEntity.setId(existOptionEntity.getId())
                )
        );
        // 只导入id不为空或勾选的打点
        optionEntityList = optionEntityList.stream().filter(i -> i.getId() != null || Objects.equals(i.getSelectStatus(), YesOrNoEnum.YES.getCode())).toList();
        // 打点保存到数据库
        bomsProductConfigOptionDao.saveOrUpdateBatch(optionEntityList);
    }

    private BomsProductConfigOptionEntity buildProductConfigOptionEntity(BomsProductConfigEntity productConfigEntity, ProductConfigOptionHistory productConfigOptionHistory) {
        BomsProductConfigOptionEntity optionEntity = new BomsProductConfigOptionEntity();
        optionEntity.setPcId(productConfigEntity.getId());
        optionEntity.setOptionCode(productConfigOptionHistory.getOptionCode());
        optionEntity.setFeatureCode(productConfigOptionHistory.getFeatureCode());
        optionEntity.setSelectStatus(productConfigOptionHistory.isSelect() ? YesOrNoEnum.YES.getCode() : YesOrNoEnum.NO.getCode());
        optionEntity.setSelectCanEdit(YesOrNoEnum.YES.getCode());
        optionEntity.setType(ProductConfigOptionTypeEnum.NORMAL.getType());
        return optionEntity;
    }

    /**
     * 读取历史数据
     */
    private List<ProductConfigHistory> readData(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             ExcelReader excelReader = EasyExcel.read(new BufferedInputStream(inputStream)).excelType(ExcelTypeEnum.XLS).build()) {
            ProductConfigOptionReadListener readListener = new ProductConfigOptionReadListener();
            // 读取Sheet 0
            ReadSheet readSheet = EasyExcel.readSheet(0)
                    .registerReadListener(readListener)
                    .headRowNumber(1).build();
            // 开始读取Sheet
            excelReader.read(Lists.newArrayList(readSheet));
            return readListener.getProductConfigHistoryList();
        } catch (Exception e) {
            throw new BusinessException(ConfigErrorCode.EXCEL_UPLOAD_ERROR, e.getMessage());
        }
    }

    private static class ProductConfigOptionReadListener extends AnalysisEventListener<Map<Integer, String>> {

        private String currentFeatureCode;
        private final List<ProductConfigHistory> productConfigHistoryList = Lists.newArrayList();

        @Override
        public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
            headMap.forEach((index, headName) -> {
                if (index < FIXED_HEAD_NAME_LIST.size()) {
                    if (!Objects.equals(headName, FIXED_HEAD_NAME_LIST.get(index))) {
                        throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_PRODUCT_CONFIG_OPTION_IMPORT_ERROR, "Excel Head Name Is Error");
                    }
                    return;
                }
                productConfigHistoryList.add(new ProductConfigHistory(headName, index));
            });
        }

        @Override
        public void invoke(Map<Integer, String> integerStringMap, AnalysisContext analysisContext) {
            // 第1行和第2行跳过
            if (analysisContext.readRowHolder().getRowIndex() < CommonConstants.INT_TWO) {
                return;
            }
            String featureCode = integerStringMap.get(0);
            if (StringUtils.isBlank(featureCode)) {
                // 合并单元格的Feature Code
                featureCode = currentFeatureCode;
            } else {
                currentFeatureCode = featureCode;
            }
            String optionCode = integerStringMap.get(2);
            if (StringUtils.isBlank(featureCode)) {
                throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_PRODUCT_CONFIG_OPTION_IMPORT_ERROR, "Feature Code Is Blank");
            }
            if (StringUtils.isBlank(optionCode)) {
                throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_PRODUCT_CONFIG_OPTION_IMPORT_ERROR, "Option Code Is Blank");
            }
            for (ProductConfigHistory productConfigHistory : productConfigHistoryList) {
                String selectText = integerStringMap.get(productConfigHistory.getPcIndex());
                ProductConfigOptionHistory productConfigOptionHistory = new ProductConfigOptionHistory();
                productConfigOptionHistory.setOptionCode(optionCode);
                productConfigOptionHistory.setFeatureCode(featureCode);
                productConfigOptionHistory.setSelect(Objects.equals(selectText, SELECT_TEXT));
                productConfigHistory.getProductConfigOptionHistoryList().add(productConfigOptionHistory);
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        }

        public List<ProductConfigHistory> getProductConfigHistoryList() {
            return productConfigHistoryList;
        }

    }

    @Data
    private static class ProductConfigHistory {

        private String pcName;

        private Integer pcIndex;

        private List<ProductConfigOptionHistory> productConfigOptionHistoryList = Lists.newArrayList();

        public ProductConfigHistory(String pcName, Integer pcIndex) {
            this.pcName = pcName;
            this.pcIndex = pcIndex;
        }

    }

    @Data
    private static class ProductConfigOptionHistory {

        private String optionCode;

        private String featureCode;

        private boolean select;

    }

}
