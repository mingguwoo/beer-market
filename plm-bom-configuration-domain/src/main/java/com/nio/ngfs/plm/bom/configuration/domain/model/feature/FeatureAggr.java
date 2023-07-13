package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.BrandEnum;
import com.nio.ngfs.plm.bom.configuration.common.enums.CatalogEnum;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.common.util.RegexUtil;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Slf4j
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureAggr extends AbstractDo implements AggrRoot<FeatureId>, Cloneable {

    private FeatureId featureId;

    /**
     * Parent Feature Code
     */
    private String parentFeatureCode;

    /**
     * 英文描述
     */
    private String displayName;

    /**
     * 中文描述
     */
    private String chineseName;

    /**
     * 补充描述
     */
    private String description;

    /**
     * Feature的可选性
     */
    private String selectionType;

    /**
     * Feature的必须性
     */
    private String mayMust;

    /**
     * Feature的分类
     */
    private String catalog;

    /**
     * Feature的成熟度
     */
    private String maturity;

    /**
     * 版本，颜色件相关
     */
    private String version;

    /**
     * 创建方
     */
    private String requestor;

    /**
     * 状态，Active/Inactive
     */
    private String status;

    /**
     * 父节点
     */
    private transient FeatureAggr parent;

    /**
     * children节点列表
     */
    private transient List<FeatureAggr> childrenList = Collections.emptyList();

    /**
     * children是否变更（仅适用于childrenList中对象）
     */
    private transient boolean childrenChanged = false;

    @Override
    public FeatureId getUniqId() {
        return featureId;
    }

    @Override
    public FeatureAggr clone() {
        try {
            FeatureAggr copy = (FeatureAggr) super.clone();
            copy.setFeatureId(new FeatureId(featureId.getFeatureCode(), featureId.getType()));
            if (parent != null) {
                copy.setParent(parent.clone());
            }
            if (CollectionUtils.isNotEmpty(childrenList)) {
                copy.setChildrenList(childrenList.stream().map(FeatureAggr::clone).collect(Collectors.toList()));
            }
            return copy;
        } catch (CloneNotSupportedException e) {
            log.error("FeatureAggr clone error", e);
            throw new BusinessException(ConfigErrorCode.CLONE_ERROR);
        }
    }

    /**
     * 新增Group
     */
    public void addGroup() {
        checkType(FeatureTypeEnum.GROUP);
        checkGroupCode(featureId.getFeatureCode());
        setStatus(FeatureStatusEnum.ACTIVE.getStatus());
        setParentFeatureCode(ConfigConstants.GROUP_PARENT_FEATURE_CODE);
    }

    /**
     * 编辑Group
     */
    public void editGroup(EditGroupCmd cmd) {
        String newGroupCode = cmd.getGroupCode().trim();
        checkType(FeatureTypeEnum.GROUP);
        checkGroupCode(newGroupCode);
        // 属性更新
        setDisplayName(cmd.getDisplayName());
        setChineseName(cmd.getChineseName());
        setDescription(cmd.getDescription());
        setUpdateUser(cmd.getUpdateUser());
        changeGroupCode(newGroupCode, cmd.getUpdateUser());
    }

    /**
     * 改变Group状态
     */
    public FeatureStatusChangeTypeEnum changeGroupStatus(ChangeGroupStatusCmd cmd) {
        checkType(FeatureTypeEnum.GROUP);
        // status校验
        String newStatus = cmd.getStatus();
        FeatureStatusEnum oldStatusEnum = FeatureStatusEnum.getByStatus(status);
        FeatureStatusEnum newStatusEnum = FeatureStatusEnum.getByStatus(newStatus);
        if (oldStatusEnum == null || newStatusEnum == null) {
            throw new BusinessException(ConfigErrorCode.FEATURE_STATUS_INVALID);
        }
        if (oldStatusEnum == FeatureStatusEnum.INACTIVE && newStatusEnum == FeatureStatusEnum.ACTIVE) {
            return FeatureStatusChangeTypeEnum.INACTIVE_TO_ACTIVE;
        } else if (oldStatusEnum == FeatureStatusEnum.ACTIVE && newStatusEnum == FeatureStatusEnum.INACTIVE) {
            childrenList.forEach(children -> {
                if (children.isActive()) {
                    throw new BusinessException(ConfigErrorCode.FEATURE_CHANGE_GROUP_STATUS_FEATURE_EXIST_ACTIVE);
                }
            });
            setStatus(newStatus);
            setUpdateUser(cmd.getUpdateUser());
            return FeatureStatusChangeTypeEnum.ACTIVE_TO_INACTIVE;
        }
        return FeatureStatusChangeTypeEnum.NO_CHANGE;
    }

    /**
     * 新增Feature
     */
    public void addFeature() {
        // Group状态校验
        if (!parent.isActive()) {
            throw new BusinessException(ConfigErrorCode.FEATURE_GROUP_IS_NOT_ACTIVE);
        }
        // 字段校验
        checkType(FeatureTypeEnum.FEATURE);
        checkFeatureAndOptionCode();
        checkCatalog(catalog);
        checkRequestor(requestor);
        // 字段赋值
        setSelectionType(ConfigConstants.SINGLE);
        setMayMust(ConfigConstants.MAY);
        setMaturity(ConfigConstants.IN_WORK);
        setVersion(ConfigConstants.VERSION_A);
        setStatus(FeatureStatusEnum.ACTIVE.getStatus());
    }

    public void editFeature(EditFeatureCmd cmd) {
        checkType(FeatureTypeEnum.FEATURE);
        // 参数校验
        checkCatalog(cmd.getCatalog());
        checkRequestor(cmd.getRequestor());
        // 属性更新
        setDisplayName(cmd.getDisplayName());
        setChineseName(cmd.getChineseName());
        setDescription(cmd.getDescription());
        setCatalog(cmd.getCatalog());
        setRequestor(cmd.getRequestor());
        setUpdateUser(cmd.getUpdateUser());
    }

    /**
     * 改变Feature状态
     */
    public FeatureStatusChangeTypeEnum changeFeatureStatus(ChangeFeatureStatusCmd cmd) {
        checkType(FeatureTypeEnum.FEATURE);
        // status校验
        String newStatus = cmd.getStatus();
        FeatureStatusEnum oldStatusEnum = FeatureStatusEnum.getByStatus(status);
        FeatureStatusEnum newStatusEnum = FeatureStatusEnum.getByStatus(newStatus);
        if (oldStatusEnum == null || newStatusEnum == null) {
            throw new BusinessException(ConfigErrorCode.FEATURE_STATUS_INVALID);
        }
        if (oldStatusEnum == newStatusEnum) {
            return FeatureStatusChangeTypeEnum.NO_CHANGE;
        }
        // Feature状态变更
        setStatus(newStatusEnum.getStatus());
        setUpdateUser(cmd.getUpdateUser());
        // Feature下的Option列表状态变更
        changeChildrenListStatus(newStatusEnum, cmd.getUpdateUser());
        if (oldStatusEnum == FeatureStatusEnum.INACTIVE && newStatusEnum == FeatureStatusEnum.ACTIVE) {
            // 状态由Inactive变为Active
            return FeatureStatusChangeTypeEnum.INACTIVE_TO_ACTIVE;
        }
        // 状态由Active变为Inactive
        return FeatureStatusChangeTypeEnum.ACTIVE_TO_INACTIVE;
    }

    /**
     * 新增Option
     */
    public void addOption() {
        checkType(FeatureTypeEnum.OPTION);
        checkFeatureAndOptionCode();
        checkRequestor(requestor);
        checkOptionChineseNameUnique();
        checkOptionCodeAndFeatureCodeTwoDigits();
        // 字段赋值
        setVersion(ConfigConstants.VERSION_A);
        setStatus(FeatureStatusEnum.ACTIVE.getStatus());
    }

    /**
     * 编辑Option
     */
    public void editOption(EditOptionCmd cmd) {
        //参数校验
        checkType(FeatureTypeEnum.OPTION);
        checkRequestor(cmd.getRequestor());
        checkOptionChineseNameUnique();
        checkOptionCodeAndFeatureCodeTwoDigits();
        //字段赋值
        setDisplayName(cmd.getDisplayName());
        setChineseName(cmd.getChineseName());
        setDescription(cmd.getDescription());
        setRequestor(cmd.getRequestor());
        setUpdateUser(cmd.getUpdateUser());

    }

    /**
     * 校验类型
     */
    private void checkType(FeatureTypeEnum typeEnum) {
        if (!Objects.equals(featureId.getType(), typeEnum.getType())) {
            throw new BusinessException(ConfigErrorCode.FEATURE_TYPE_NOT_MATCH);
        }
    }

    /**
     * 校验Group Code，只允许字母、数字和空格
     */
    private void checkGroupCode(String groupCode) {
        if (!RegexUtil.isMatchAlphabetNumberAndBlank(groupCode)) {
            throw new BusinessException(ConfigErrorCode.FEATURE_GROUP_CODE_FORMAT_ERROR);
        }
    }

    /**
     * 校验Feature/Option Code，只允许字母、数字和空格
     */
    private void checkFeatureAndOptionCode() {
        if (!RegexUtil.isMatchAlphabetAndNumber(featureId.getFeatureCode())) {
            throw new BusinessException(isType(FeatureTypeEnum.FEATURE) ?
                    ConfigErrorCode.FEATURE_FEATURE_CODE_FORMAT_ERROR : ConfigErrorCode.FEATURE_OPTION_CODE_FORMAT_ERROR);
        }
    }

    /**
     * 校验Catalog
     */
    private void checkCatalog(String catalog) {
        if (CatalogEnum.getByCatalog(catalog) == null) {
            throw new BusinessException(ConfigErrorCode.FEATURE_CATALOG_INVALID);
        }
    }

    /**
     * 校验Requestor
     */
    private void checkRequestor(String requestor) {
        if (BrandEnum.getByName(requestor) == null) {
            throw new BusinessException(ConfigErrorCode.FEATURE_REQUESTOR_INVALID);
        }
    }

    /**
     * 更新Group Code
     */
    private void changeGroupCode(String newGroupCode, String updateUser) {
        if (Objects.equals(featureId.getFeatureCode(), newGroupCode)) {
            // Group Code未更新
            return;
        }
        // Group Code更新
        featureId.setFeatureCode(newGroupCode);
        childrenList.forEach(children -> changeParentFeatureCode(children, newGroupCode, updateUser));
    }

    /**
     * 更新ParentFeatureCode
     */
    private void changeParentFeatureCode(FeatureAggr featureAggr, String newParentFeatureCode, String updateUser) {
        featureAggr.setParentFeatureCode(newParentFeatureCode);
        featureAggr.setUpdateUser(updateUser);
        featureAggr.setChildrenChanged(true);
    }

    /**
     * 改变子节点列表的状态
     *
     * @param newStatusEnum 新的状态
     */
    private void changeChildrenListStatus(FeatureStatusEnum newStatusEnum, String updateUser) {
        childrenList.forEach(children -> changeStatus(children, newStatusEnum, updateUser));
    }

    /**
     * 更新状态
     */
    private void changeStatus(FeatureAggr featureAggr, FeatureStatusEnum newStatusEnum, String updateUser) {
        featureAggr.setStatus(newStatusEnum.getStatus());
        featureAggr.setUpdateUser(updateUser);
        featureAggr.setChildrenChanged(true);
    }

    /**
     * 状态是否Active
     */
    public boolean isActive() {
        return Objects.equals(status, FeatureStatusEnum.ACTIVE.getStatus());
    }

    /**
     * 是否指定类型
     */
    public boolean isType(FeatureTypeEnum typeEnum) {
        return Objects.equals(featureId.getType(), typeEnum.getType());
    }

    /**
     * Chinese Name在同一feature下是否唯一
     */
    public void checkOptionChineseNameUnique() {
        List<String> chineseNameList = parent.getChildrenList().stream().map(FeatureAggr::getChineseName).toList();
        if (chineseNameList.contains(chineseName)) {
            throw new BusinessException(ConfigErrorCode.FEATURE_OPTION_CHINESE_NAME_REPEAT);
        }
    }

    /**
     * 校验OptionCode前两位与所属Feature是否一致
     */
    public void checkOptionCodeAndFeatureCodeTwoDigits() {
        if (featureId.getFeatureCode().substring(0, 3).equals(parentFeatureCode.substring(0, 3))) {
            throw new BusinessException(ConfigErrorCode.FEATURE_OPTION_CODE_DIFF_FROM_FEATURE_CODE);
        }
    }

    public void changeOptionStatus(String newStatus, String updateUser) {
        checkType(FeatureTypeEnum.OPTION);
        // status校验
        FeatureStatusEnum oldStatusEnum = FeatureStatusEnum.getByStatus(status);
        FeatureStatusEnum newStatusEnum = FeatureStatusEnum.getByStatus(newStatus);
        if (oldStatusEnum == null || newStatusEnum == null) {
            throw new BusinessException(ConfigErrorCode.FEATURE_STATUS_INVALID);
        }
        if (!(oldStatusEnum.getStatus().equals(newStatusEnum.getStatus()))) {
            setStatus(newStatusEnum.getStatus());
            setUpdateUser(updateUser);
        }
    }

}
