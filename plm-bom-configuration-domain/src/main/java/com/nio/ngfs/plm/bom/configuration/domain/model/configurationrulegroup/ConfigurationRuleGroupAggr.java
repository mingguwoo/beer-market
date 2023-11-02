package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRulePurposeEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.EditGroupAndRuleCmd;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * Configuration Rule Group
 *
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationRuleGroupAggr extends AbstractDo implements AggrRoot<Long> {

    /**
     * Rule的中文描述
     */
    private String chineseName;

    /**
     * Rule的英文描述
     */
    private String displayName;

    /**
     * 创建Rule的目的，1-（Sales —> Eng），2-（Sales —> Sales），3-（Sales <—> Sales），4-（Sales X Sales），5-（Base Vehicle —> Sales）
     */
    private Integer purpose;

    /**
     * Rule适用范围
     */
    private String definedBy;

    /**
     * Rule的补充描述
     */
    private String description;

    /**
     * Group勾选的Driving Feature Code
     */
    private String drivingFeature;

    /**
     * Group勾选的Constrained Feature Code列表，多个之间逗号分隔
     */
    private String constrainedFeature;

    @Override
    public Long getUniqId() {
        return id;
    }

    /**
     * 新增Group
     */
    public void add() {
        checkPurpose();
    }

    /**
     * 编辑Group
     */
    public void edit(EditGroupAndRuleCmd cmd) {
        ConfigurationRulePurposeEnum purposeEnum = ConfigurationRulePurposeEnum.getAndCheckByCode(purpose);
        if (!purposeEnum.isCanEditGroup()) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_RULE_GROUP_CAN_NOT_EDIT);
        }
        setChineseName(cmd.getChineseName());
        setDisplayName(cmd.getDisplayName());
        setDescription(cmd.getDescription());
        // Driving Feature有值，则保持不变
        if (StringUtils.isBlank(this.getDrivingFeature())) {
            setDrivingFeature(cmd.getDrivingFeature());
        }
        setConstrainedFeature(Joiner.on(",").skipNulls().join(Optional.ofNullable(cmd.getConstrainedFeatureList()).orElse(Lists.newArrayList())));
        setUpdateUser(cmd.getUpdateUser());
    }

    /**
     * 删除Group
     */
    public void delete() {
        ConfigurationRulePurposeEnum purposeEnum = ConfigurationRulePurposeEnum.getAndCheckByCode(purpose);
        if (!purposeEnum.isCanDeleteGroup()) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_RULE_GROUP_CAN_NOT_DELETE);
        }
    }

    /**
     * 校验Purpose
     */
    private void checkPurpose() {
        ConfigurationRulePurposeEnum.getAndCheckByCode(purpose);
    }

    /**
     * 获取Purpose枚举
     */
    public ConfigurationRulePurposeEnum getRulePurposeEnum() {
        return ConfigurationRulePurposeEnum.getByCode(purpose);
    }

    public List<String> getConstrainedFeatureList() {
        if (StringUtils.isBlank(constrainedFeature)) {
            return Lists.newArrayList();
        }
        return Splitter.on(",").trimResults().omitEmptyStrings().splitToList(constrainedFeature);
    }

    public String getModel() {
        return definedBy.split(" ")[0];
    }

}
