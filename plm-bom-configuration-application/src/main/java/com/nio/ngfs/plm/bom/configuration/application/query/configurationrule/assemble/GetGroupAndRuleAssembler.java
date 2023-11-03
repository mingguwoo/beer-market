package com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.assemble;

import com.google.common.base.Splitter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleGroupEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.GetGroupAndRuleRespDto;

/**
 * @author xiaozhou.tu
 * @date 2023/10/31
 */
public class GetGroupAndRuleAssembler {

    public static GetGroupAndRuleRespDto assemble(BomsConfigurationRuleGroupEntity entity) {
        GetGroupAndRuleRespDto respDto = new GetGroupAndRuleRespDto();
        respDto.setGroupId(entity.getId());
        respDto.setChineseName(entity.getChineseName());
        respDto.setDisplayName(entity.getDisplayName());
        respDto.setPurpose(entity.getPurpose());
        respDto.setDefinedBy(entity.getDefinedBy());
        respDto.setDescription(entity.getDescription());
        respDto.setDrivingFeature(entity.getDrivingFeature());
        respDto.setConstrainedFeatureList(Splitter.on(",").trimResults().omitEmptyStrings().splitToList(entity.getConstrainedFeature()));
        return respDto;
    }

    public static GetGroupAndRuleRespDto.RuleDrivingFeature assembleDrivingFeature(BomsFeatureLibraryEntity entity) {
        GetGroupAndRuleRespDto.RuleDrivingFeature ruleDrivingFeature = new GetGroupAndRuleRespDto.RuleDrivingFeature();
        ruleDrivingFeature.setFeatureCode(entity.getFeatureCode());
        ruleDrivingFeature.setChineseName(entity.getChineseName());
        return ruleDrivingFeature;
    }

    public static GetGroupAndRuleRespDto.RuleDrivingOption assembleDrivingOption(BomsFeatureLibraryEntity entity) {
        GetGroupAndRuleRespDto.RuleDrivingOption ruleDrivingOption = new GetGroupAndRuleRespDto.RuleDrivingOption();
        ruleDrivingOption.setUniqueId(entity.getId());
        ruleDrivingOption.setOptionCode(entity.getFeatureCode());
        ruleDrivingOption.setChineseName(entity.getChineseName());
        ruleDrivingOption.setFeatureCode(entity.getParentFeatureCode());
        return ruleDrivingOption;
    }

    public static GetGroupAndRuleRespDto.RuleConstrainedFeature assembleConstrainedFeature(BomsFeatureLibraryEntity entity) {
        GetGroupAndRuleRespDto.RuleConstrainedFeature ruleConstrainedFeature = new GetGroupAndRuleRespDto.RuleConstrainedFeature();
        ruleConstrainedFeature.setFeatureCode(entity.getFeatureCode());
        ruleConstrainedFeature.setChineseName(entity.getChineseName());
        return ruleConstrainedFeature;
    }

    public static GetGroupAndRuleRespDto.RuleConstrainedOption assembleConstrainedOption(BomsFeatureLibraryEntity entity) {
        GetGroupAndRuleRespDto.RuleConstrainedOption ruleConstrainedOption = new GetGroupAndRuleRespDto.RuleConstrainedOption();
        ruleConstrainedOption.setOptionCode(entity.getFeatureCode());
        ruleConstrainedOption.setChineseName(entity.getChineseName());
        return ruleConstrainedOption;
    }

}
