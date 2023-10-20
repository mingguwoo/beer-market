package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup;

import com.google.common.base.Splitter;
import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRulePurposeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;

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

    private transient List<String> constrainedFeatureList;

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
     * 校验Purpose
     */
    private void checkPurpose() {
        if (ConfigurationRulePurposeEnum.getByCode(purpose) == null) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_PURPOSE_ERROR);
        }
    }

    /**
     * 是否指定的Purpose
     */
    public boolean isPurpose(ConfigurationRulePurposeEnum purposeEnum) {
        return Objects.equals(purpose, purposeEnum.getCode());
    }

    public List<String> getConstrainedFeatureList() {
        if (constrainedFeatureList == null) {
            constrainedFeatureList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(constrainedFeature);
        }
        return constrainedFeatureList;
    }

}
