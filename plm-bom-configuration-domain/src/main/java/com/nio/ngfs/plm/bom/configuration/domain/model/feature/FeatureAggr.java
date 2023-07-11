package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.common.util.RegexUtil;
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
     * children节点列表
     */
    private transient List<FeatureAggr> childrenList = Collections.emptyList();

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
     * 新增Feature
     */
    public void addFeature() {
        checkType(FeatureTypeEnum.FEATURE);
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
    }

    /**
     * 更新ParentFeatureCode
     */
    private void changeParentFeatureCode(FeatureAggr featureAggr, String newParentFeatureCode) {
        featureAggr.setParentFeatureCode(newParentFeatureCode);
    }

    public boolean isActive() {
        return Objects.equals(status, FeatureStatusEnum.ACTIVE.getStatus());
    }

}
