package com.nio.ngfs.plm.bom.configuration.application.query.configurationrule;

import com.google.common.collect.Lists;
import com.nio.bom.share.enums.YesOrNoEnum;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.assemble.GetGroupAndRuleAssembler;
import com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.bean.RuleGroupDto;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRuleStatusEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleGroupDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleGroupEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleOptionEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.GetGroupAndRuleQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.GetGroupAndRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 获取Group和Rule详情
 *
 * @author xiaozhou.tu
 * @date 2023/10/27
 */
@Component
@RequiredArgsConstructor
public class GetGroupAndRuleQuery extends AbstractQuery<GetGroupAndRuleQry, GetGroupAndRuleRespDto> {

    private final BomsConfigurationRuleDao bomsConfigurationRuleDao;
    private final BomsConfigurationRuleGroupDao bomsConfigurationRuleGroupDao;
    private final BomsConfigurationRuleOptionDao bomsConfigurationRuleOptionDao;
    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;

    @Override
    protected GetGroupAndRuleRespDto executeQuery(GetGroupAndRuleQry qry) {
        BomsConfigurationRuleGroupEntity ruleGroupEntity = bomsConfigurationRuleGroupDao.getById(qry.getGroupId());
        if (ruleGroupEntity == null) {
            return new GetGroupAndRuleRespDto();
        }
        List<BomsConfigurationRuleEntity> ruleEntityList = bomsConfigurationRuleDao.queryByGroupId(ruleGroupEntity.getId());
        List<BomsConfigurationRuleOptionEntity> ruleOptionEntityList = bomsConfigurationRuleOptionDao.queryByRuleIdList(LambdaUtil.map(ruleEntityList, BomsConfigurationRuleEntity::getId));
        List<RuleGroupDto> ruleGroupDtoList = buildRuleGroupDto(ruleEntityList, ruleOptionEntityList);
        GetGroupAndRuleRespDto respDto = GetGroupAndRuleAssembler.assemble(ruleGroupEntity);
        // 构建Driving Feature/Option列
        respDto.setRuleDrivingFeatureList(buildDrivingFeatureOption(ruleGroupDtoList, ruleGroupEntity));
        // 构建Constrained Feature/Option行
        respDto.setRuleConstrainedFeatureList(buildConstrainedFeatureOption(ruleGroupDtoList, ruleGroupEntity));
        return respDto;
    }

    private List<RuleGroupDto> buildRuleGroupDto(List<BomsConfigurationRuleEntity> ruleEntityList, List<BomsConfigurationRuleOptionEntity> ruleOptionEntityList) {
        // 排除掉不可见的Rule
        Map<String, List<BomsConfigurationRuleEntity>> ruleEntityGroup = LambdaUtil.groupBy(ruleEntityList, i -> Objects.equals(i.getInvisible(), YesOrNoEnum.NO.getCode()),
                BomsConfigurationRuleEntity::getRuleNumber);
        Map<Long, List<BomsConfigurationRuleOptionEntity>> ruleOptionMap = LambdaUtil.groupBy(ruleOptionEntityList, BomsConfigurationRuleOptionEntity::getRuleId);
        return ruleEntityGroup.values().stream().map(ruleList -> {
            RuleGroupDto ruleGroupDto = new RuleGroupDto();
            // 按版本正排
            ruleGroupDto.setRuleList(ruleList.stream().sorted(Comparator.comparing(BomsConfigurationRuleEntity::getRuleVersion)).toList());
            ruleGroupDto.getRuleList().forEach(rule -> rule.setRuleOptionList(ruleOptionMap.getOrDefault(rule.getId(), Lists.newArrayList())));
            return ruleGroupDto;
        }).toList();
    }

    private List<GetGroupAndRuleRespDto.RuleDrivingFeature> buildDrivingFeatureOption(List<RuleGroupDto> ruleGroupDtoList, BomsConfigurationRuleGroupEntity ruleGroupEntity) {
        List<BomsConfigurationRuleEntity> currentRuleList = Lists.newArrayList();
        // 选取最后一个版本的Rule
        ruleGroupDtoList.forEach(ruleGroupDto -> currentRuleList.add(ruleGroupDto.getRuleList().get(ruleGroupDto.getRuleList().size() - 1)));
        List<String> drivingFeatureCodeList = currentRuleList.stream().flatMap(i -> i.getRuleOptionList().stream()).map(BomsConfigurationRuleOptionEntity::getDrivingFeatureCode).distinct().toList();
        if (drivingFeatureCodeList.size() != 1) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_BOTH_WAY_DRIVING_FEATURE_ONLY_SELECT_ONE);
        }
        String drivingFeatureCode = drivingFeatureCodeList.get(0);
        if (StringUtils.isNotBlank(ruleGroupEntity.getDrivingFeature()) && !Objects.equals(ruleGroupEntity.getDrivingFeature(), drivingFeatureCode)) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_DRIVING_FEATURE_NOT_THE_SAME);
        }
        // 查询Feature/Option信息
        List<BomsFeatureLibraryEntity> featureEntityList = bomsFeatureLibraryDao.queryByFeatureOptionCodeList(Lists.newArrayList(drivingFeatureCode));
        if (CollectionUtils.isEmpty(featureEntityList)) {
            throw new BusinessException(ConfigErrorCode.FEATURE_FEATURE_NOT_EXISTS);
        }
        BomsFeatureLibraryEntity featureEntity = featureEntityList.get(0);
        List<BomsFeatureLibraryEntity> optionEntityList = bomsFeatureLibraryDao.queryByParentFeatureCodeAndType(featureEntity.getFeatureCode(), FeatureTypeEnum.OPTION.getType());
        // 获取最新版已发布的Driving列
        Set<String> currentReleasedDrivingOptionCodeSet = currentRuleList.stream().filter(i -> Objects.equals(i.getStatus(), ConfigurationRuleStatusEnum.RELEASED.getStatus()))
                .filter(i -> CollectionUtils.isNotEmpty(i.getRuleOptionList())).map(i -> i.getRuleOptionList().get(0).getDrivingOptionCode()).collect(Collectors.toSet());
        GetGroupAndRuleRespDto.RuleDrivingFeature ruleDrivingFeature = GetGroupAndRuleAssembler.assembleDrivingFeature(featureEntity);
        // Option排序
        ruleDrivingFeature.setOptionList(optionEntityList.stream().sorted(Comparator.comparing(BomsFeatureLibraryEntity::getFeatureCode)).map(optionEntity -> {
            GetGroupAndRuleRespDto.RuleDrivingOption ruleDrivingOption = GetGroupAndRuleAssembler.assembleDrivingOption(optionEntity);
            ruleDrivingOption.setHide(currentReleasedDrivingOptionCodeSet.contains(ruleDrivingOption.getOptionCode()));
            return ruleDrivingOption;
        }).toList());
        return Lists.newArrayList(ruleDrivingFeature);
    }

    private List<GetGroupAndRuleRespDto.RuleConstrainedFeature> buildConstrainedFeatureOption(List<RuleGroupDto> ruleGroupDtoList, BomsConfigurationRuleGroupEntity ruleGroupEntity) {
        // 获取所有Rule版本的Constrained Feature
        List<String> constrainedFeatureCodeList = ruleGroupDtoList.stream().flatMap(i -> i.getRuleList().stream())
                .flatMap(i -> i.getRuleOptionList().stream()).map(BomsConfigurationRuleOptionEntity::getConstrainedFeatureCode).distinct().toList();
        // 查询Feature/Option信息
        List<BomsFeatureLibraryEntity> featureEntityList = bomsFeatureLibraryDao.queryByFeatureOptionCodeList(Lists.newArrayList(constrainedFeatureCodeList));
        if (CollectionUtils.isEmpty(featureEntityList) || featureEntityList.size() != constrainedFeatureCodeList.size()) {
            throw new BusinessException(ConfigErrorCode.FEATURE_FEATURE_NOT_EXISTS);
        }
        List<BomsFeatureLibraryEntity> optionEntityList = bomsFeatureLibraryDao.queryByParentFeatureCodeListAndType(constrainedFeatureCodeList, FeatureTypeEnum.OPTION.getType());
        Map<String, List<BomsFeatureLibraryEntity>> optionEntityGroup = LambdaUtil.groupBy(optionEntityList, BomsFeatureLibraryEntity::getParentFeatureCode);
        return featureEntityList.stream().sorted(Comparator.comparing(BomsFeatureLibraryEntity::getFeatureCode)).map(featureEntity -> {
            List<BomsFeatureLibraryEntity> childOptionList = optionEntityGroup.get(featureEntity.getFeatureCode());
            if (CollectionUtils.isEmpty(childOptionList)) {
                return null;
            }
            GetGroupAndRuleRespDto.RuleConstrainedFeature ruleConstrainedFeature = GetGroupAndRuleAssembler.assembleConstrainedFeature(featureEntity);
            ruleConstrainedFeature.setOptionList(childOptionList.stream().sorted(Comparator.comparing(BomsFeatureLibraryEntity::getFeatureCode)).map(option -> {
                GetGroupAndRuleRespDto.RuleConstrainedOption ruleConstrainedOption = GetGroupAndRuleAssembler.assembleConstrainedOption(option);
//                ruleConstrainedOption.setCanDelete();
//                ruleConstrainedOption.setConfigList();
                return ruleConstrainedOption;
            }).toList());
            return ruleConstrainedFeature;
        }).filter(Objects::nonNull).toList();
    }

}
