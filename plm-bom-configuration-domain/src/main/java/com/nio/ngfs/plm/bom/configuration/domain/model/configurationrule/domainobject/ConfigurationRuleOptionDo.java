package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject;

import com.nio.bom.share.domain.model.Entity;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

    @Override
    public Long getUniqId() {
        return id;
    }

}
