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
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.RuleOptionMatrixValueEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.*;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.*;
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
    private final BomsProductContextFeatureDao bomsProductContextFeatureDao;

    @Override
    protected GetGroupAndRuleRespDto executeQuery(GetGroupAndRuleQry qry) {
        BomsConfigurationRuleGroupEntity ruleGroupEntity = bomsConfigurationRuleGroupDao.getById(qry.getGroupId());
        if (ruleGroupEntity == null) {
            return new GetGroupAndRuleRespDto();
        }
        List<BomsConfigurationRuleEntity> ruleEntityList = bomsConfigurationRuleDao.queryByGroupId(ruleGroupEntity.getId());
        List<BomsConfigurationRuleOptionEntity> ruleOptionEntityList = bomsConfigurationRuleOptionDao.queryByRuleIdList(LambdaUtil.map(ruleEntityList, BomsConfigurationRuleEntity::getId));
        String model = getModelCode(ruleGroupEntity);
        // Rule按Rule Number进行分组
        List<RuleGroupDto> ruleGroupDtoList = buildRuleGroupDto(ruleEntityList, ruleOptionEntityList);
        GetGroupAndRuleRespDto respDto = GetGroupAndRuleAssembler.assemble(ruleGroupEntity);
        // 构建Driving Feature/Option列
        respDto.setRuleDrivingFeatureList(buildDrivingFeatureOption(model, ruleGroupDtoList, ruleGroupEntity));
        // 构建Constrained Feature/Option行
        respDto.setRuleConstrainedFeatureList(buildConstrainedFeatureOption(model, ruleGroupDtoList, respDto));
        return respDto;
    }

    private String getModelCode(BomsConfigurationRuleGroupEntity entity) {
        return entity.getDefinedBy().split(" ")[0];
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

    private List<GetGroupAndRuleRespDto.RuleDrivingFeature> buildDrivingFeatureOption(String model, List<RuleGroupDto> ruleGroupDtoList,
                                                                                      BomsConfigurationRuleGroupEntity ruleGroupEntity) {
        // 选取最后一个版本的Rule列表
        List<BomsConfigurationRuleEntity> latestRuleList = ruleGroupDtoList.stream().map(i -> i.getRuleList().get(i.getRuleList().size() - 1)).toList();
        List<String> drivingFeatureCodeList = latestRuleList.stream().flatMap(i -> i.getRuleOptionList().stream()).map(BomsConfigurationRuleOptionEntity::getDrivingFeatureCode).distinct().toList();
        if (CollectionUtils.isEmpty(drivingFeatureCodeList)) {
            return Lists.newArrayList();
        }
        if (drivingFeatureCodeList.size() != 1) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_BOTH_WAY_DRIVING_FEATURE_ONLY_SELECT_ONE);
        }
        String drivingFeatureCode = drivingFeatureCodeList.get(0);
        if (StringUtils.isNotBlank(ruleGroupEntity.getDrivingFeature()) && !Objects.equals(ruleGroupEntity.getDrivingFeature(), drivingFeatureCode)) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_DRIVING_FEATURE_NOT_THE_SAME);
        }
        // 查询Feature/Option信息
        List<BomsFeatureLibraryEntity> featureEntityList = queryFeatureOptionByProductContext(model, Lists.newArrayList(drivingFeatureCode));
        // 获取最新版已发布的Driving列
        Set<String> currentReleasedDrivingOptionCodeSet = latestRuleList.stream().filter(i -> Objects.equals(i.getStatus(), ConfigurationRuleStatusEnum.RELEASED.getStatus()))
                .filter(i -> CollectionUtils.isNotEmpty(i.getRuleOptionList())).map(i -> i.getRuleOptionList().get(0).getDrivingOptionCode()).collect(Collectors.toSet());
        return featureEntityList.stream().map(featureEntity -> {
            // 构建Driving Feature
            GetGroupAndRuleRespDto.RuleDrivingFeature ruleDrivingFeature = GetGroupAndRuleAssembler.assembleDrivingFeature(featureEntity);
            // 构建Driving Option
            ruleDrivingFeature.setOptionList(featureEntity.getChildren().stream().sorted(Comparator.comparing(BomsFeatureLibraryEntity::getFeatureCode)).map(optionEntity -> {
                GetGroupAndRuleRespDto.RuleDrivingOption ruleDrivingOption = GetGroupAndRuleAssembler.assembleDrivingOption(optionEntity);
                ruleDrivingOption.setReleased(currentReleasedDrivingOptionCodeSet.contains(ruleDrivingOption.getOptionCode()));
                return ruleDrivingOption;
            }).toList());
            return ruleDrivingFeature;
        }).filter(i -> CollectionUtils.isNotEmpty(i.getOptionList())).toList();
    }

    private List<GetGroupAndRuleRespDto.RuleConstrainedFeature> buildConstrainedFeatureOption(String model, List<RuleGroupDto> ruleGroupDtoList, GetGroupAndRuleRespDto respDto) {
        // 计算已发布的Constrained Option行
        Set<String> releasedConstrainedOptionCodeSet = ruleGroupDtoList.stream().flatMap(i -> i.getRuleList().stream())
                .filter(i -> Objects.equals(i.getStatus(), ConfigurationRuleStatusEnum.RELEASED.getStatus()))
                .flatMap(i -> i.getRuleOptionList().stream())
                .filter(i -> !Objects.equals(i.getMatrixValue(), RuleOptionMatrixValueEnum.UNAVAILABLE.getCode()))
                .map(BomsConfigurationRuleOptionEntity::getConstrainedOptionCode)
                .collect(Collectors.toSet());
        // 获取Status为In Work最新版Rule的打点，按ConstrainedOption进行分组
        Map<String, List<BomsConfigurationRuleOptionEntity>> ruleConstrainedOptionGroup = ruleGroupDtoList.stream().map(i -> i.getRuleList().get(i.getRuleList().size() - 1))
                .filter(i -> Objects.equals(i.getStatus(), ConfigurationRuleStatusEnum.IN_WORK.getStatus()))
                .flatMap(i -> i.getRuleOptionList().stream())
                .collect(Collectors.groupingBy(BomsConfigurationRuleOptionEntity::getConstrainedOptionCode));
        // 获取所有Rule版本的Constrained Feature
        List<String> constrainedFeatureCodeList = ruleGroupDtoList.stream().flatMap(i -> i.getRuleList().stream())
                .flatMap(i -> i.getRuleOptionList().stream()).map(BomsConfigurationRuleOptionEntity::getConstrainedFeatureCode).distinct().toList();
        // 查询Feature/Option信息
        List<BomsFeatureLibraryEntity> featureEntityList = queryFeatureOptionByProductContext(model, constrainedFeatureCodeList);
        List<GetGroupAndRuleRespDto.RuleDrivingOption> drivingOptionList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(respDto.getRuleDrivingFeatureList())) {
            drivingOptionList.addAll(respDto.getRuleDrivingFeatureList().get(0).getOptionList());
        }
        return featureEntityList.stream().sorted(Comparator.comparing(BomsFeatureLibraryEntity::getFeatureCode)).map(featureEntity -> {
            GetGroupAndRuleRespDto.RuleConstrainedFeature ruleConstrainedFeature = GetGroupAndRuleAssembler.assembleConstrainedFeature(featureEntity);
            ruleConstrainedFeature.setOptionList(featureEntity.getChildren().stream().sorted(Comparator.comparing(BomsFeatureLibraryEntity::getFeatureCode)).map(option -> {
                GetGroupAndRuleRespDto.RuleConstrainedOption ruleConstrainedOption = GetGroupAndRuleAssembler.assembleConstrainedOption(option);
                ruleConstrainedOption.setCanDelete(!releasedConstrainedOptionCodeSet.contains(ruleConstrainedOption.getOptionCode()));
                ruleConstrainedOption.setConfigList(buildRuleRowColumnConfig(drivingOptionList, ruleConstrainedOptionGroup.get(ruleConstrainedOption.getOptionCode())));
                return ruleConstrainedOption;
            }).toList());
            return ruleConstrainedFeature;
        }).filter(i -> CollectionUtils.isNotEmpty(i.getOptionList())).toList();
    }

    private List<GetGroupAndRuleRespDto.RuleRowColumnConfig> buildRuleRowColumnConfig(List<GetGroupAndRuleRespDto.RuleDrivingOption> drivingOptionList,
                                                                                      List<BomsConfigurationRuleOptionEntity> ruleOptionList) {
        Map<String, BomsConfigurationRuleOptionEntity> ruleOptionMap = LambdaUtil.toKeyMap(ruleOptionList, BomsConfigurationRuleOptionEntity::getDrivingOptionCode);
        return drivingOptionList.stream().filter(i -> !i.isReleased()).map(drivingOption -> {
            GetGroupAndRuleRespDto.RuleRowColumnConfig ruleRowColumnConfig = new GetGroupAndRuleRespDto.RuleRowColumnConfig();
            ruleRowColumnConfig.setUniqueId(drivingOption.getUniqueId());
            ruleRowColumnConfig.setDrivingOptionCode(drivingOption.getOptionCode());
            ruleRowColumnConfig.setMatrixValue(Optional.ofNullable(ruleOptionMap.get(drivingOption.getOptionCode()))
                    .map(BomsConfigurationRuleOptionEntity::getMatrixValue).orElse(RuleOptionMatrixValueEnum.UNAVAILABLE.getCode()));
            return ruleRowColumnConfig;
        }).toList();
    }

    private List<BomsFeatureLibraryEntity> queryFeatureOptionByProductContext(String model, List<String> featureCodeList) {
        // 查询Feature信息
        featureCodeList = featureCodeList.stream().distinct().toList();
        List<BomsFeatureLibraryEntity> featureEntityList = bomsFeatureLibraryDao.queryByFeatureOptionCodeList(Lists.newArrayList(featureCodeList));
        if (featureEntityList.size() != featureCodeList.size()) {
            throw new BusinessException(ConfigErrorCode.FEATURE_FEATURE_NOT_EXISTS);
        }
        // 查询Feature下的Option信息
        List<BomsFeatureLibraryEntity> optionEntityList = bomsFeatureLibraryDao.queryByParentFeatureCodeListAndType(featureCodeList, FeatureTypeEnum.OPTION.getType());
        // 查询Product Context下的Option行
        List<BomsProductContextFeatureEntity> contextFeatureList = bomsProductContextFeatureDao.queryByModelCodeAndFeatureCode(model, LambdaUtil.map(optionEntityList,
                BomsFeatureLibraryEntity::getFeatureCode));
        Set<String> contextFeatureSet = contextFeatureList.stream().map(BomsProductContextFeatureEntity::getFeatureCode).collect(Collectors.toSet());
        optionEntityList = optionEntityList.stream().filter(i -> contextFeatureSet.contains(i.getFeatureCode())).toList();
        Map<String, List<BomsFeatureLibraryEntity>> optionEntityGroup = LambdaUtil.groupBy(optionEntityList, BomsFeatureLibraryEntity::getParentFeatureCode);
        return featureEntityList.stream().peek(i -> i.setChildren(optionEntityGroup.get(i.getFeatureCode()))).filter(i -> CollectionUtils.isNotEmpty(i.getChildren())).toList();
    }

}
