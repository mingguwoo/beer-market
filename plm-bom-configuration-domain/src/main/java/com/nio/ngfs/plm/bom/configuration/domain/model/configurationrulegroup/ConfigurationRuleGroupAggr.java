package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup;

import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
     * Group勾选的Constrained Feature Code列表
     */
    private String constrainedFeatureList;

    @Override
    public Long getUniqId() {
        return id;
    }

}
