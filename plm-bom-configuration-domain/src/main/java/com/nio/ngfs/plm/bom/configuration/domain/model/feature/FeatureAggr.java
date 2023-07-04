package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.ngfs.plm.bom.configuration.common.enums.ErrorCode;
import com.nio.ngfs.plm.bom.configuration.common.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.util.PreconditionUtil;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
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

    private transient FeatureAggr parent;

    private transient List<FeatureAggr> childrenList = Collections.emptyList();

    private FeatureId featureId;

    private String parentFeatureCode;

    private String displayName;

    private String chineseName;

    private String description;

    private String status;

    private transient boolean statusChanged;

    @Override
    public FeatureId getId() {
        return featureId;
    }

    public void addGroup() {
        PreconditionUtil.checkNotNull(getId().getFeatureCode(), "Group Code");
        PreconditionUtil.checkMaxLength(getId().getFeatureCode(), MAX_LENGTH, "Group Code");
        PreconditionUtil.checkMaxLength(displayName, MAX_LENGTH, "Display Name");
        PreconditionUtil.checkMaxLength(chineseName, MAX_LENGTH, "Chinese Name");
        PreconditionUtil.checkMaxLength(description, MAX_LENGTH, "Description");
        setStatus(FeatureStatusEnum.ACTIVE.getStatus());
    }

    public void editGroup(EditGroupCmd cmd) {
        if (!Objects.equals(getId().getType(), FeatureTypeEnum.GROUP.getType())) {
            throw new BusinessException(ErrorCode.FEATURE_ADD_GROUP_GROUP_CODE_REPEAT);
        }
        setDisplayName(cmd.getDisplayName());
        setChineseName(cmd.getChineseName());
        setDescription(cmd.getDescription());
        setUpdateUser(cmd.getUpdateUser());
        // status未更新
        if (Objects.equals(status, cmd.getStatus())) {
            updateFeatureCode(cmd.getFeatureCode());
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
            setStatus(cmd.getStatus());
            setStatusChanged(true);
            updateChildrenStatusActive();
            updateFeatureCode(cmd.getFeatureCode());
        }
    }

    private void updateChildrenStatusActive() {
        childrenList.forEach(children -> children.setStatus(FeatureStatusEnum.ACTIVE.getStatus()));
    }

    private void updateFeatureCode(String newFeatureCode) {
        if (Objects.equals(getId().getFeatureCode(), newFeatureCode)) {
            return;
        }
        getId().setFeatureCode(newFeatureCode);
        childrenList.forEach(children -> children.setParentFeatureCode(newFeatureCode));
    }

    public boolean isActive() {
        return Objects.equals(status, FeatureStatusEnum.ACTIVE.getStatus());
    }

    public boolean isInactive() {
        return Objects.equals(status, FeatureStatusEnum.INACTIVE.getStatus());
    }

}
