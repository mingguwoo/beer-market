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
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.EditFeatureCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.EditGroupCmd;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureAggr extends AbstractDo implements AggrRoot<FeatureId> {

    private static final int MAX_LENGTH = 128;

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
    private String  mayMust;

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
     * children节点列表是否变更
     */
    private transient boolean childrenChanged = false;

    @Override
    public FeatureId getUniqId() {
        return featureId;
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
        changeGroupCode(newGroupCode);
    }

    /**
     * 改变Group状态
     */
    public FeatureStatusChangeTypeEnum changeGroupStatus(String newStatus) {
        checkType(FeatureTypeEnum.GROUP);
        // status校验
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
    private void changeGroupCode(String newGroupCode) {
        if (Objects.equals(featureId.getFeatureCode(), newGroupCode)) {
            // Group Code未更新
            return;
        }
        // Group Code更新
        featureId.setFeatureCode(newGroupCode);
        childrenList.forEach(children -> changeParentFeatureCode(children, newGroupCode));
        setChildrenChanged(true);
    }

    /**
     * 更新ParentFeatureCode
     */
    private void changeParentFeatureCode(FeatureAggr featureAggr, String newParentFeatureCode) {
        featureAggr.setParentFeatureCode(newParentFeatureCode);
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
     * 新增Option
     */
    public void addOption() {
        checkType(FeatureTypeEnum.OPTION);
        checkFeatureAndOptionCode();
        checkRequestor(requestor);
        // 字段赋值
        setVersion(ConfigConstants.VERSION_A);
        setStatus(FeatureStatusEnum.ACTIVE.getStatus());
    }

}
