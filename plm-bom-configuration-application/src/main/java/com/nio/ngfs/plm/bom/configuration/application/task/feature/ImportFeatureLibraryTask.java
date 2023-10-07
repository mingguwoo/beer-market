package com.nio.ngfs.plm.bom.configuration.application.task.feature;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.enums.BrandEnum;
import com.nio.bom.share.enums.CommonErrorCode;
import com.nio.bom.share.enums.StatusEnum;
import com.nio.bom.share.enums.YesOrNoEnum;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.DateUtils;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.bom.share.utils.PreconditionUtil;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductContextDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.ImportFeatureLibraryRespDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

/**
 * 导入Feature Library历史数据
 *
 * @author xiaozhou.tu
 * @date 2023/7/26
 */
@Component
@RequiredArgsConstructor
public class ImportFeatureLibraryTask {

    private static final int BATCH_SIZE = 50;
    private static final String SYSTEM_USER = "system";
    private static final String CONFIGURATION_FEATURE = "Configuration Feature";
    private static final String CONFIGURATION_OPTION = "Configuration Option";

    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;
    private final BomsProductContextDao bomsProductContextDao;

    public ImportFeatureLibraryRespDto execute(MultipartFile file) {
        // 读取历史数据
        List<FeatureLibraryHistory> featureLibraryHistoryList = readData(file);
        if (CollectionUtils.isEmpty(featureLibraryHistoryList)) {
            return new ImportFeatureLibraryRespDto();
        }
        // 校验数据
        checkData(featureLibraryHistoryList);
        // 导入Group
        importGroup(featureLibraryHistoryList);
        // 解析Feature和Option
        List<BomsFeatureLibraryEntity> featureOptionList = buildFeatureAndOption(featureLibraryHistoryList);
        // 批量保存到数据库
        for (List<BomsFeatureLibraryEntity> partitionList : Lists.partition(featureOptionList, BATCH_SIZE)) {
            saveOrUpdate(partitionList);
        }
        // 导入历史Related Model Year
        importRelatedModelYear(featureLibraryHistoryList);
        return new ImportFeatureLibraryRespDto();
    }

    /**
     * 新增或更新
     */
    private void saveOrUpdate(List<BomsFeatureLibraryEntity> partitionList) {
        List<BomsFeatureLibraryEntity> existEntityList = bomsFeatureLibraryDao.queryByFeatureOptionCodeList(LambdaUtil.map(partitionList,
                BomsFeatureLibraryEntity::getFeatureCode));
        Map<String, Long> featureCodeIdMap = LambdaUtil.toKeyValueMap(existEntityList, BomsFeatureLibraryEntity::getFeatureCode, BomsFeatureLibraryEntity::getId);
        partitionList.forEach(entity -> entity.setId(featureCodeIdMap.get(entity.getFeatureCode())));
        bomsFeatureLibraryDao.saveBatch(partitionList.stream().filter(i -> i.getId() == null).toList());
        bomsFeatureLibraryDao.updateBatchById(partitionList.stream().filter(i -> i.getId() != null).toList());
    }

    /**
     * 读取历史数据
     */
    private List<FeatureLibraryHistory> readData(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             ExcelReader excelReader = EasyExcel.read(new BufferedInputStream(inputStream)).excelType(ExcelTypeEnum.XLSX).build()) {
            FeatureLibraryReadListener readListener = new FeatureLibraryReadListener();
            // 读取Sheet 0
            ReadSheet readSheet = EasyExcel.readSheet(0).head(FeatureLibraryHistory.class)
                    .registerReadListener(readListener)
                    .headRowNumber(1).build();
            // 开始读取Sheet
            excelReader.read(Lists.newArrayList(readSheet));
            return readListener.getFeatureLibraryHistoryList();
        } catch (Exception e) {
            throw new BusinessException(ConfigErrorCode.EXCEL_UPLOAD_ERROR, e.getMessage());
        }
    }

    /**
     * 校验数据
     */
    private void checkData(List<FeatureLibraryHistory> featureLibraryHistoryList) {
        featureLibraryHistoryList.forEach(history -> {
            PreconditionUtil.checkNotBlank(history.getCode(), "Code is blank");
            PreconditionUtil.checkNotBlank(history.getDisplayName(), "DisplayName is blank");
            PreconditionUtil.checkNotBlank(history.getChineseName(), "ChineseName is blank");
            PreconditionUtil.checkNotBlank(history.getGroup(), "Group is blank");
            PreconditionUtil.checkNotBlank(history.getType(), "Type is blank");
            PreconditionUtil.checkNotBlank(history.getCatalogue(), "Catalogue is blank");
            PreconditionUtil.checkNotBlank(history.getRequestor(), "Requestor is blank");
            PreconditionUtil.checkNotBlank(history.getCreator(), "Creator is blank");
            PreconditionUtil.checkNotBlank(history.getOriginated(), "Originated is blank");

            PreconditionUtil.checkMaxLength(history.getDisplayName(), 128, "DisplayName max length is 128");
            PreconditionUtil.checkMaxLength(history.getChineseName(), 128, "ChineseName max length is 128");
            PreconditionUtil.checkMaxLength(history.getDescription(), 128, "Description max length is 128");

            checkEnumValues(history.getType(), Lists.newArrayList(
                    CONFIGURATION_FEATURE, CONFIGURATION_OPTION
            ), "Type enum value is not match");
//            checkEnumValues(history.getCatalogue(), Lists.newArrayList(
//                    FeatureCatalogEnum.SALES.getCatalog(), FeatureCatalogEnum.ENGINEERING.getCatalog()
//            ), "Catalogue enum value is not match");
            checkEnumValues(history.getRequestor(), Lists.newArrayList(
                    BrandEnum.NIO.name(), BrandEnum.ALPS.name(), BrandEnum.FY.name()
            ), "Requestor enum value is not match");

            if (history.isOption() && history.getParent() == null) {
                throw new BusinessException(CommonErrorCode.PARAMETER_ERROR.getCode(), "Option Has No Parent Feature");
            }
        });
    }

    /**
     * 校验枚举值
     */
    private void checkEnumValues(String str, List<String> enumValueList, String errorMessage) {
        if (!enumValueList.contains(str)) {
            throw new BusinessException(CommonErrorCode.PARAMETER_ERROR.getCode(), errorMessage);
        }
    }

    /**
     * 解析Group
     */
    private void importGroup(List<FeatureLibraryHistory> featureLibraryHistoryList) {
        List<String> groupCodeList = featureLibraryHistoryList.stream().map(FeatureLibraryHistory::getGroup).distinct().toList();
        groupCodeList.stream().map(this::buildGroup).forEach(bomsFeatureLibraryEntity -> {
            BomsFeatureLibraryEntity existEntity = bomsFeatureLibraryDao.getByFeatureCodeAndType(bomsFeatureLibraryEntity.getFeatureCode(),
                    FeatureTypeEnum.GROUP.getType());
            if (existEntity != null) {
                bomsFeatureLibraryEntity.setId(existEntity.getId());
                bomsFeatureLibraryDao.updateById(bomsFeatureLibraryEntity);
            } else {
                bomsFeatureLibraryDao.save(bomsFeatureLibraryEntity);
            }
        });
    }

    /**
     * 解析Feature和Option
     */
    private List<BomsFeatureLibraryEntity> buildFeatureAndOption(List<FeatureLibraryHistory> featureLibraryHistoryList) {
        return LambdaUtil.map(featureLibraryHistoryList, history -> {
            if (history.isFeature()) {
                return buildFeature(history);
            } else if (history.isOption()) {
                return buildOption(history);
            }
            return null;
        });
    }

    private BomsFeatureLibraryEntity buildGroup(String groupCode) {
        BomsFeatureLibraryEntity entity = new BomsFeatureLibraryEntity();
        entity.setFeatureCode(groupCode);
        entity.setParentFeatureCode(ConfigConstants.GROUP_PARENT_FEATURE_CODE);
        entity.setType(FeatureTypeEnum.GROUP.getType());
        entity.setStatus(StatusEnum.ACTIVE.getStatus());
        entity.setCreateUser(SYSTEM_USER);
        entity.setUpdateUser(SYSTEM_USER);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        return entity;
    }

    private BomsFeatureLibraryEntity buildFeature(FeatureLibraryHistory history) {
        BomsFeatureLibraryEntity entity = new BomsFeatureLibraryEntity();
        entity.setFeatureCode(history.getCode());
        entity.setParentFeatureCode(history.getGroup());
        entity.setType(FeatureTypeEnum.FEATURE.getType());
        entity.setDisplayName(history.getDisplayName());
        entity.setChineseName(history.getChineseName());
        entity.setDescription(history.getDescription());
        entity.setSelectionType(ConfigConstants.SINGLE);
        entity.setMayMust(ConfigConstants.MAY);
        entity.setCatalog(history.getCatalogue());
        entity.setMaturity(ConfigConstants.IN_WORK);
        handleCommonField(entity, history);
        return entity;
    }

    private BomsFeatureLibraryEntity buildOption(FeatureLibraryHistory history) {
        BomsFeatureLibraryEntity entity = new BomsFeatureLibraryEntity();
        entity.setFeatureCode(history.getCode());
        entity.setParentFeatureCode(history.getParent().getCode());
        entity.setType(FeatureTypeEnum.OPTION.getType());
        entity.setDisplayName(history.getDisplayName());
        entity.setChineseName(history.getChineseName());
        entity.setDescription(history.getDescription());
        entity.setCatalog(history.getParent().getCatalogue());
        handleCommonField(entity, history);
        return entity;
    }

    private void handleCommonField(BomsFeatureLibraryEntity entity, FeatureLibraryHistory history) {
        entity.setVersion(ConfigConstants.VERSION_A);
        entity.setRequestor(history.getRequestor());
        entity.setStatus(StatusEnum.ACTIVE.getStatus());
        entity.setCreateUser(history.getCreator());
        entity.setUpdateUser(history.getUpdateUser());
        if (StringUtils.isNotBlank(history.getOriginated())) {
            entity.setCreateTime(DateUtils.parseDate(history.getOriginated(), DateUtils.YYYY_MM_DD_HH_MM_SS));
        }
        if (StringUtils.isNotBlank(history.getLastModified())) {
            entity.setUpdateTime(DateUtils.parseDate(history.getLastModified(), DateUtils.YYYY_MM_DD_HH_MM_SS));
        }
    }

    /**
     * 导入历史Related Model Year
     */
    private void importRelatedModelYear(List<FeatureLibraryHistory> featureLibraryHistoryList) {
        List<BomsProductContextEntity> contextEntityList = Lists.newArrayList();
        featureLibraryHistoryList.stream().filter(FeatureLibraryHistory::isOption).forEach(option -> {
            if (StringUtils.isBlank(option.getModelYear())) {
                return;
            }
            Splitter.on(",").trimResults().omitEmptyStrings().splitToList(option.getModelYear()).forEach(modelAndModelYear -> {
                String[] splitArr = modelAndModelYear.split(" ");
                if (splitArr.length != CommonConstants.INT_TWO) {
                    throw new BusinessException(CommonErrorCode.PARAMETER_ERROR, "Related Model Year Format Is Error modelAndModelYear=" + modelAndModelYear);
                }
                contextEntityList.add(buildProductContextEntity(option.getCode(), option.getParent().getCode(), splitArr[0], splitArr[1]));
            });
        });
        // 查询已存在的Model Year
        List<BomsProductContextEntity> existContextEntityList = bomsProductContextDao.queryAllIncludeDelete();
        Map<String, Set<String>> existOptionCodeSetByModelYearMap = existContextEntityList.stream().collect(
                Collectors.groupingBy(e -> e.getModelCode() + " " + e.getModelYear(), mapping(BomsProductContextEntity::getOptionCode, toSet()))
        );
        LambdaUtil.groupBy(contextEntityList, e -> e.getModelCode() + " " + e.getModelYear()).forEach((k, entityList) -> {
            Set<String> existOptionCodeSet = existOptionCodeSetByModelYearMap.getOrDefault(k, Sets.newHashSet());
            List<BomsProductContextEntity> saveEntityList = entityList.stream().filter(i -> !existOptionCodeSet.contains(i.getOptionCode())).toList();
            bomsProductContextDao.saveBatch(saveEntityList);
        });
    }

    private BomsProductContextEntity buildProductContextEntity(String optionCode, String featureCode, String modelCode, String modelYear) {
        BomsProductContextEntity entity = new BomsProductContextEntity();
        entity.setModelCode(modelCode);
        entity.setModelYear(modelYear);
        entity.setFeatureCode(featureCode);
        entity.setOptionCode(optionCode);
        // 此处设为逻辑删除，代表历史数据
        entity.setDelFlag(YesOrNoEnum.YES.getCode());
        entity.setCreateUser(ConfigConstants.SYSTEM_USER);
        entity.setUpdateUser(ConfigConstants.SYSTEM_USER);
        return entity;
    }

    private static class FeatureLibraryReadListener implements ReadListener<FeatureLibraryHistory> {

        private final List<FeatureLibraryHistory> featureLibraryHistoryList = Lists.newArrayList();
        private FeatureLibraryHistory parentFeature;

        @Override
        public void invoke(FeatureLibraryHistory featureLibraryHistory, AnalysisContext analysisContext) {
            featureLibraryHistoryList.add(featureLibraryHistory);
            if (featureLibraryHistory.isFeature()) {
                parentFeature = featureLibraryHistory;
            } else if (featureLibraryHistory.isOption()) {
                featureLibraryHistory.setParent(parentFeature);
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        }

        public List<FeatureLibraryHistory> getFeatureLibraryHistoryList() {
            return featureLibraryHistoryList;
        }

    }

    @Data
    @NoArgsConstructor
    public static class FeatureLibraryHistory {

        @ExcelProperty("Code")
        private String code;

        @ExcelProperty("Display Name")
        private String displayName;

        @ExcelProperty("Chinese Name")
        private String chineseName;

        @ExcelProperty("Description")
        private String description;

        @ExcelProperty("Group")
        private String group;

        @ExcelProperty("Type")
        private String type;

        @ExcelProperty("Catalogue")
        private String catalogue;

        @ExcelProperty("Requestor")
        private String requestor;

        @ExcelProperty("Creator")
        private String creator;

        @ExcelProperty("Originated")
        private String originated;

        @ExcelProperty("Update User")
        private String updateUser;

        @ExcelProperty("Last Modified")
        private String lastModified;

        @ExcelProperty("Status")
        private String status;

        @ExcelProperty("Model Year")
        private String modelYear;

        private transient FeatureLibraryHistory parent;

        public boolean isFeature() {
            return Objects.equals(getType(), CONFIGURATION_FEATURE);
        }

        public boolean isOption() {
            return Objects.equals(getType(), CONFIGURATION_OPTION);
        }

    }

}
