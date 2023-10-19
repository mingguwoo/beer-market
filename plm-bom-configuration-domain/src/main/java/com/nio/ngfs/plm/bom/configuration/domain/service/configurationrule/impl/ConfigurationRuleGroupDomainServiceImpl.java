package com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.impl;

import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ModelFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.response.ModelRespDto;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleGroupDomainService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Service
@RequiredArgsConstructor
public class ConfigurationRuleGroupDomainServiceImpl implements ConfigurationRuleGroupDomainService {

    private final ModelFacade modelFacade;

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
    }

}
