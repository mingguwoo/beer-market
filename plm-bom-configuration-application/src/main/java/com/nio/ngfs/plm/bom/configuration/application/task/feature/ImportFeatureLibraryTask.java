package com.nio.ngfs.plm.bom.configuration.application.task.feature;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.google.common.collect.Lists;
import com.nio.bom.share.enums.StatusEnum;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.DateUtils;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    public void execute(MultipartFile file) {
        List<FeatureLibraryHistory> featureLibraryHistoryList = readData(file);
        List<BomsFeatureLibraryEntity> bomsFeatureLibraryEntityList = Lists.newArrayList();
        importGroup(featureLibraryHistoryList, bomsFeatureLibraryEntityList);
        importFeatureAndOption(featureLibraryHistoryList, bomsFeatureLibraryEntityList);
        for (List<BomsFeatureLibraryEntity> partitionList : Lists.partition(bomsFeatureLibraryEntityList, BATCH_SIZE)) {
            bomsFeatureLibraryDao.saveBatch(partitionList);
        }
    }

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

    private void importGroup(List<FeatureLibraryHistory> featureLibraryHistoryList, List<BomsFeatureLibraryEntity> bomsFeatureLibraryEntityList) {
        List<String> groupCodeList = featureLibraryHistoryList.stream().map(FeatureLibraryHistory::getGroup).distinct().toList();
        List<BomsFeatureLibraryEntity> groupList = groupCodeList.stream().map(this::buildGroup).toList();
        bomsFeatureLibraryEntityList.addAll(groupList);
    }

    private void importFeatureAndOption(List<FeatureLibraryHistory> featureLibraryHistoryList, List<BomsFeatureLibraryEntity> bomsFeatureLibraryEntityList) {
        bomsFeatureLibraryEntityList.addAll(LambdaUtil.map(featureLibraryHistoryList, history -> {
            if (history.isFeature()) {
                return buildFeature(history);
            } else if (history.isOption()) {
                return buildOption(history);
            }
            return null;
        }));
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

        private FeatureLibraryHistory parent;

        public boolean isFeature() {
            return Objects.equals(getType(), CONFIGURATION_FEATURE);
        }

        public boolean isOption() {
            return Objects.equals(getType(), CONFIGURATION_OPTION);
        }

    }

}
