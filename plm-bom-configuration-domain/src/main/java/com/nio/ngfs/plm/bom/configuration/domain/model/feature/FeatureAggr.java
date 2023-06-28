package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.common.enums.ErrorCode;
import com.nio.ngfs.plm.bom.configuration.common.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.util.PreconditionUtil;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.dos.EditGroupDO;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureAggr {

    private static final int MAX_LENGTH = 128;

    private FeatureAggr parent;

    private List<FeatureAggr> childrenList = Collections.emptyList();

    private String featureCode;

    private String parentFeatureCode;

    private String type;

    private String displayName;

    private String chineseName;

    private String description;

    private String status;

    private String createUser;

    private String updateUser;

    private transient boolean statusChanged;

    public void addGroup() {
        PreconditionUtil.checkNotNull(featureCode, "Group Code");
        PreconditionUtil.checkMaxLength(featureCode, MAX_LENGTH, "Group Code");
        PreconditionUtil.checkMaxLength(displayName, MAX_LENGTH, "Display Name");
        PreconditionUtil.checkMaxLength(chineseName, MAX_LENGTH, "Chinese Name");
        PreconditionUtil.checkMaxLength(description, MAX_LENGTH, "Description");
        setType(FeatureTypeEnum.GROUP.getType());
        setStatus(FeatureStatusEnum.ACTIVE.getStatus());
    }

    public void editGroup(EditGroupDO editGroupDO) {
        if (!Objects.equals(type, FeatureTypeEnum.GROUP.getType())) {
            throw new BusinessException(ErrorCode.FEATURE_ADD_GROUP_GROUP_CODE_REPEAT);
        }
        setDisplayName(editGroupDO.getDisplayName());
        setChineseName(editGroupDO.getChineseName());
        setDescription(editGroupDO.getDescription());
        setUpdateUser(editGroupDO.getUpdateUser());
        // status未更新
        if (Objects.equals(status, editGroupDO.getStatus())) {
            updateFeatureCode(editGroupDO.getFeatureCode());
            return;
        }
        if (isActive()) {
            // status从Active变为Inactive
            for (FeatureAggr children : childrenList) {
                if (children.isActive()) {
                    throw new BusinessException(ErrorCode.FEATURE_EDIT_GROUP_FEATURE_EXISTS_ACTIVE);
                }
            }
        } else {
            // status从Inactive变为Active
            setStatus(editGroupDO.getStatus());
            setStatusChanged(true);
            updateChildrenStatusActive();
            updateFeatureCode(editGroupDO.getFeatureCode());
        }
    }

    private void updateChildrenStatusActive() {
        childrenList.forEach(children -> children.setStatus(FeatureStatusEnum.ACTIVE.getStatus()));
    }

    private void updateFeatureCode(String newFeatureCode) {
        if (Objects.equals(featureCode, newFeatureCode)) {
            return;
        }
        setFeatureCode(newFeatureCode);
        childrenList.forEach(children -> children.setParentFeatureCode(newFeatureCode));
    }

    public boolean isActive() {
        return Objects.equals(status, FeatureStatusEnum.ACTIVE.getStatus());
    }

    public boolean isInactive() {
        return Objects.equals(status, FeatureStatusEnum.INACTIVE.getStatus());
    }

}
