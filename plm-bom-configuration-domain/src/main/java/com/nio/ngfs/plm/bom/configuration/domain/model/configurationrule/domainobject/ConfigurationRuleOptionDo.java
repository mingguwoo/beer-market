package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.domain.model.Entity;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.RuleOptionMatrixValueEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.BeanUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * Configuration Rule Option
 *
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationRuleOptionDo extends AbstractDo implements Entity<Long> {

    /**
     * Rule Id
     */
    private Long ruleId;

    /**
     * Rule条件的Option Code
     */
    private String drivingOptionCode;

    /**
     * Rule条件的Feature Code
     */
    private String drivingFeatureCode;

    /**
     * Rule结果的Option Code
     */
    private String constrainedOptionCode;

    /**
     * Rule结果的Feature Code
     */
    private String constrainedFeatureCode;

    /**
     * 矩阵打点，1-Inclusive，2-Exclusive，3-Unavailable
     */
    private Integer matrixValue;

    private transient ConfigurationRuleAggr rule;

    @Override
    public Long getUniqId() {
        return id;
    }

    /**
     * 新增打点
     */
    public void add() {
        checkMatrixValue();
    }

    /**
     * 校验矩阵打点
     */
    private void checkMatrixValue() {
        RuleOptionMatrixValueEnum matrixValueEnum = RuleOptionMatrixValueEnum.getByCode(matrixValue);
        if (matrixValueEnum == null) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_OPTION_MATRIX_VALUE_ERROR);
        }
        Optional.of(rule.getRulePurposeEnum().getMatrixValueList()).ifPresent(matrixValueList -> {
            if (!matrixValueList.contains(matrixValueEnum)) {
                throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_OPTION_MATRIX_VALUE_ERROR);
            }
        });
    }

    /**
     * 复制双向Rule的打点
     */
    public ConfigurationRuleOptionDo copyBothWayRuleOption() {
        ConfigurationRuleOptionDo copyRuleOptionDo = new ConfigurationRuleOptionDo();
        BeanUtils.copyProperties(this, copyRuleOptionDo);
        // Driving和Constrained调换
        copyRuleOptionDo.setDrivingOptionCode(getConstrainedOptionCode());
        copyRuleOptionDo.setDrivingFeatureCode(getConstrainedFeatureCode());
        copyRuleOptionDo.setConstrainedOptionCode(getDrivingOptionCode());
        copyRuleOptionDo.setConstrainedFeatureCode(getDrivingFeatureCode());
        return copyRuleOptionDo;
    }

    /**
     * 是否指定的矩阵打点
     */
    public boolean isMatrixValue(RuleOptionMatrixValueEnum matrixValueEnum) {
        return Objects.equals(matrixValue, matrixValueEnum.getCode());
    }

    /**
     * 是否双向Rule的Option
     */
    public boolean isBothWayRuleOption(ConfigurationRuleOptionDo another) {
        return Objects.equals(this.getDrivingOptionCode(), another.getConstrainedOptionCode()) &&
                Objects.equals(this.getConstrainedOptionCode(), another.getDrivingOptionCode()) &&
                Objects.equals(this.getMatrixValue(), another.getMatrixValue());
    }

    @Override
    public String toString() {
        return "ConfigurationRuleOptionDo{" +
                "id=" + id +
                ", ruleId=" + ruleId +
                ", drivingOptionCode='" + drivingOptionCode + '\'' +
                ", drivingFeatureCode='" + drivingFeatureCode + '\'' +
                ", constrainedOptionCode='" + constrainedOptionCode + '\'' +
                ", constrainedFeatureCode='" + constrainedFeatureCode + '\'' +
                ", matrixValue=" + matrixValue +
                ", createUser='" + createUser + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
