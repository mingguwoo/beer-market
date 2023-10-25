package com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.service.impl;

import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.service.ConfigurationRuleQueryService;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleGroupDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleGroupEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.RuleViewBasicInformationRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.RuleViewHeadInfoRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.RuleViewInfoRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author bill.wang
 * @date 2023/10/23
 */
@Component
@RequiredArgsConstructor
public class ConfigurationRuleQueryServiceImpl implements ConfigurationRuleQueryService {

    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;
    private final BomsConfigurationRuleGroupDao bomsConfigurationRuleGroupDao;
    private final ConfigurationRuleRepository configurationRuleRepository;
    @Override
    public Map<String, BomsFeatureLibraryEntity> queryFeatureOptionMap() {
        Map<String,BomsFeatureLibraryEntity> respMap = new HashMap<>();
        List<BomsFeatureLibraryEntity> entityList = bomsFeatureLibraryDao.queryAll();
        entityList.forEach(entity->{
            respMap.put(entity.getFeatureCode(),entity);
        });
        return respMap;
    }


    @Override
    public RuleViewInfoRespDto queryView(Long groupId) {

        BomsConfigurationRuleGroupEntity entity = bomsConfigurationRuleGroupDao.getById(groupId);

        if(Objects.isNull(entity)){
            throw new BusinessException(ConfigErrorCode.ID_ERROR);
        }

        RuleViewInfoRespDto ruleViewInfoRespDto = buildRuleViewBasicInformation(entity);
        List<ConfigurationRuleAggr> ruleEntities = configurationRuleRepository.queryByGroupId(groupId);
        if(CollectionUtils.isEmpty(ruleEntities)){
            return ruleViewInfoRespDto;
        }
        //获取optionName
        Map<String,String> optionNameMaps = findOptionName(ruleEntities);
        Map<String,List<ConfigurationRuleAggr>> ruleMap =
                ruleEntities.stream().collect(Collectors.groupingBy(ConfigurationRuleAggr::getRuleNumber));

        RuleViewHeadInfoRespDto headInfoRespDto=new RuleViewHeadInfoRespDto();
        String driveFeatureCode = ruleEntities.stream().filter(x->CollectionUtils.isNotEmpty(x.getOptionList()))
                .findFirst().get().getOptionList().get(0).getDrivingFeatureCode();
        headInfoRespDto.setDriveFeatureCode(optionNameMaps.get(driveFeatureCode));
        ruleMap.forEach((ruleNumber,ruleAggrs)->{
            List<ConfigurationRuleAggr>  configurationRuleSorts=
                    ruleAggrs.stream().sorted(Comparator.comparing(ConfigurationRuleAggr::getRuleVersion).reversed()).toList();
            //configurationRuleSorts.forEach();

        });











        return null;
    }


    public Map<String,String> findOptionName(List<ConfigurationRuleAggr> ruleEntities){
        Set<String> optionCodes = new HashSet<>(128);

        ruleEntities.forEach(rule->{
            rule.getOptionList().forEach(option->{
                optionCodes.add(option.getDrivingFeatureCode());
                optionCodes.add(option.getDrivingOptionCode());
                optionCodes.add(option.getConstrainedOptionCode());
                optionCodes.add(option.getConstrainedFeatureCode());
            });
        });
        List<BomsFeatureLibraryEntity>  entityList =bomsFeatureLibraryDao.queryByFeatureCodes(optionCodes.stream().distinct().toList());
        return entityList.stream().collect(Collectors.toMap(BomsFeatureLibraryEntity::getFeatureCode,BomsFeatureLibraryEntity::getDisplayName));

    }

    private RuleViewInfoRespDto buildRuleViewBasicInformation(BomsConfigurationRuleGroupEntity entity) {
        RuleViewInfoRespDto ruleViewInfoRespDto=new RuleViewInfoRespDto();
        RuleViewBasicInformationRespDto basicInformationRespDto=new RuleViewBasicInformationRespDto();
        basicInformationRespDto.setChineseName(entity.getChineseName());
        basicInformationRespDto.setDescription(entity.getDescription());
        basicInformationRespDto.setPurpose(entity.getPurpose());
        basicInformationRespDto.setDisplayName(entity.getDisplayName());
        ruleViewInfoRespDto.setRuleViewBasicInformationRespDto(basicInformationRespDto);
        return ruleViewInfoRespDto;
    }
}
