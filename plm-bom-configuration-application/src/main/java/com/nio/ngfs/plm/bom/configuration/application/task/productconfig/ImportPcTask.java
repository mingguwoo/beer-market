package com.nio.ngfs.plm.bom.configuration.application.task.productconfig;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.google.common.collect.Lists;
import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.enums.YesOrNoEnum;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ModelFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.response.ModelRespDto;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.ImportPcRespDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 导入PC历史数据
 *
 * @author xiaozhou.tu
 * @date 2023/10/11
 */
@Component
@RequiredArgsConstructor
public class ImportPcTask {

    private static final Pattern PATTERN_PC_ID = Pattern.compile("^([^ ]+) ([^- ]+)-(\\d+)$");

    private final ModelFacade modelFacade;
    private final BomsProductConfigDao bomsProductConfigDao;

    public ImportPcRespDto execute(MultipartFile file) {
        // 读取历史数据
        List<PcHistory> pcHistoryList = readData(file);
        // 查询所有车型列表
        List<ModelRespDto> modelRespDtoList = modelFacade.getAllModelList();
        Map<String, String> modelBrandMap = LambdaUtil.toKeyValueMap(modelRespDtoList, i -> StringUtils.isNotBlank(i.getBrand()),
                ModelRespDto::getModel, ModelRespDto::getBrand);
        // 构建BomsProductConfigEntity
        List<BomsProductConfigEntity> productConfigEntityList = LambdaUtil.map(pcHistoryList, i -> buildProductConfigEntity(i, modelBrandMap));
        // 批量导入到数据库
        saveOrUpdate(productConfigEntityList);
        return new ImportPcRespDto();
    }

    /**
     * 批量导入到数据库
     */
    private void saveOrUpdate(List<BomsProductConfigEntity> productConfigEntityList) {
        if (CollectionUtils.isEmpty(productConfigEntityList)) {
            return;
        }
        List<BomsProductConfigEntity> allProductConfigEntityList = bomsProductConfigDao.queryAll();
        Map<String, BomsProductConfigEntity> pcIdMap = LambdaUtil.toKeyMap(allProductConfigEntityList, BomsProductConfigEntity::getPcId);
        // 待保存的列表
        List<BomsProductConfigEntity> saveProductConfigEntityList = Lists.newArrayList();
        productConfigEntityList.forEach(productConfigEntity -> {
            BomsProductConfigEntity existProductConfigEntity = pcIdMap.get(productConfigEntity.getPcId());
            if (existProductConfigEntity != null) {
                // PC Id已存在，更新Name
                existProductConfigEntity.setName(productConfigEntity.getName());
                saveProductConfigEntityList.add(existProductConfigEntity);
            } else {
                // PC Id不存在，新增
                saveProductConfigEntityList.add(productConfigEntity);
                allProductConfigEntityList.add(productConfigEntity);
            }
        });
        Map<String, BomsProductConfigEntity> pcNameMap = LambdaUtil.toKeyMap(allProductConfigEntityList, BomsProductConfigEntity::getName);
        saveProductConfigEntityList.forEach(productConfigEntity -> {
            // PC Name已存在，且PC Id不一致，重复报错
            Optional.ofNullable(pcNameMap.get(productConfigEntity.getName())).ifPresent(existPc -> {
                if (!Objects.equals(existPc.getPcId(), productConfigEntity.getPcId())) {
                    throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_PC_IMPORT_ERROR, String.format("PC Name %s Is Repeat", productConfigEntity.getName()));
                }
            });
        });
        bomsProductConfigDao.saveOrUpdateBatch(saveProductConfigEntityList, 100);
    }

    /**
     * 构建BomsProductConfigEntity
     */
    private BomsProductConfigEntity buildProductConfigEntity(PcHistory pcHistory, Map<String, String> modelBrandMap) {
        Matcher matcher = PATTERN_PC_ID.matcher(pcHistory.getPcId());
        if (!matcher.matches()) {
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_PC_IMPORT_ERROR, String.format("PC Id %s Format Is Error", pcHistory.getPcId()));
        }
        String model = matcher.group(1);
        String modelYear = matcher.group(2);
        BomsProductConfigEntity productConfigEntity = new BomsProductConfigEntity();
        productConfigEntity.setPcId(pcHistory.getPcId());
        productConfigEntity.setModelCode(model);
        productConfigEntity.setModelYear(modelYear);
        productConfigEntity.setName(pcHistory.getPcName());
        productConfigEntity.setCompleteInitSelect(YesOrNoEnum.YES.getCode());
        productConfigEntity.setBrand(modelBrandMap.get(productConfigEntity.getModelCode()));
        if (StringUtils.isBlank(productConfigEntity.getBrand())) {
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_PC_IMPORT_ERROR, String.format("Model %s Not Existed", productConfigEntity.getModelCode()));
        }
        productConfigEntity.setSkipCheck(CommonConstants.CLOSE);
        productConfigEntity.setCreateUser(ConfigConstants.SYSTEM_USER);
        productConfigEntity.setUpdateUser(ConfigConstants.SYSTEM_USER);
        return productConfigEntity;
    }

    /**
     * 读取历史数据
     */
    private List<PcHistory> readData(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             ExcelReader excelReader = EasyExcel.read(new BufferedInputStream(inputStream)).excelType(ExcelTypeEnum.XLSX).build()) {
            PcReadListener readListener = new PcReadListener();
            // 读取Sheet 0
            ReadSheet readSheet = EasyExcel.readSheet(0).head(PcHistory.class)
                    .registerReadListener(readListener)
                    .headRowNumber(1).build();
            // 开始读取Sheet
            excelReader.read(Lists.newArrayList(readSheet));
            return readListener.getPcHistoryList();
        } catch (Exception e) {
            throw new BusinessException(ConfigErrorCode.EXCEL_UPLOAD_ERROR, e.getMessage());
        }
    }

    private static class PcReadListener implements ReadListener<PcHistory> {

        private final List<PcHistory> pcHistoryList = Lists.newArrayList();

        @Override
        public void invoke(PcHistory pcHistory, AnalysisContext analysisContext) {
            pcHistoryList.add(pcHistory);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        }

        public List<PcHistory> getPcHistoryList() {
            return pcHistoryList;
        }

    }

    @Data
    @NoArgsConstructor
    public static class PcHistory {

        @ExcelProperty("PC Id")
        private String pcId;

        @ExcelProperty("Name")
        private String pcName;

    }

}
