package com.sh.beer.market.domain.service.configurationrule.impl;

import org.springframework.stereotype.Service;
import com.sh.beer.market.domain.service.configurationrule.ConfigurationRuleGroupDomainService;
import lombok.RequiredArgsConstructor;

/**
 * @author
 * @date 2023/10/17
 */
@Service
@RequiredArgsConstructor
public class ConfigurationRuleGroupDomainServiceImpl implements ConfigurationRuleGroupDomainService {

    /*private final ConfigurationRuleGroupRepository configurationRuleGroupRepository;
    private final ModelFacade modelFacade;

    @Override
    public ConfigurationRuleGroupAggr getAndCheckAggr(Long id) {
        ConfigurationRuleGroupAggr aggr = configurationRuleGroupRepository.find(id);
        if (aggr == null) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_RULE_GROUP_NOT_EXIST);
        }
        return aggr;
    }

    @Override
    public void checkDefinedBy(ConfigurationRuleGroupAggr aggr) {
        String[] definedByArr = aggr.getDefinedBy().split(" ");
        if (definedByArr.length > 2) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_DEFINED_BY_ERROR);
        }
        String model = definedByArr[0];
        ModelRespDto modelRespDto = modelFacade.getModel(model);
        // Model存在校验
        if (modelRespDto == null) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_DEFINED_BY_MODEL_NOT_EXIST);
        }
        if (definedByArr.length == 2) {
            // Model Year存在校验
            if (CollectionUtils.isEmpty(modelRespDto.getModelYear()) || !modelRespDto.getModelYear().contains(definedByArr[1])) {
                throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_DEFINED_BY_MODEL_YEAR_NOT_EXIST);
            }
        }
    }*/

}
