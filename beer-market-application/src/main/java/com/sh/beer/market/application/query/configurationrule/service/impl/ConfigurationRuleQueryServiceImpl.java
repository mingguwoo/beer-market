package com.sh.beer.market.application.query.configurationrule.service.impl;


import com.sh.beer.market.application.query.configurationrule.service.ConfigurationRuleQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023/10/23
 */
@Component
@RequiredArgsConstructor
public class ConfigurationRuleQueryServiceImpl implements ConfigurationRuleQueryService {

    /*private final BomsFeatureLibraryDao bomsFeatureLibraryDao;
    private final BomsConfigurationRuleGroupDao bomsConfigurationRuleGroupDao;
    private final ConfigurationRuleRepository configurationRuleRepository;

    @Override
    public Map<String, BomsFeatureLibraryEntity> queryFeatureOptionMap() {
        Map<String, BomsFeatureLibraryEntity> respMap = new HashMap<>();
        List<BomsFeatureLibraryEntity> entityList = bomsFeatureLibraryDao.queryAll();
        entityList.forEach(entity -> {
            respMap.put(entity.getFeatureCode(), entity);
        });
        return respMap;
    }


    @Override
    public RuleViewInfoRespDto queryView(Long groupId) {

        BomsConfigurationRuleGroupEntity entity = bomsConfigurationRuleGroupDao.getById(groupId);

        if (Objects.isNull(entity)) {
            throw new BusinessException(ConfigErrorCode.ID_ERROR);
        }

        RuleViewInfoRespDto ruleViewInfoRespDto = buildRuleViewBasicInformation(entity);
        List<ConfigurationRuleAggr> ruleEntities = configurationRuleRepository.queryByGroupId(groupId);
        if (CollectionUtils.isEmpty(ruleEntities)) {
            return ruleViewInfoRespDto;
        }
        //获取optionName
        Map<String, String> optionNameMaps = findOptionName(ruleEntities);
        Map<String, List<ConfigurationRuleAggr>> ruleMap =
                ruleEntities.stream().collect(Collectors.groupingBy(ConfigurationRuleAggr::getRuleNumber));
        //组装
        RuleViewHeadInfoRespDto headInfoRespDto = new RuleViewHeadInfoRespDto();
        String driveFeatureCode = ruleEntities.stream().filter(x -> CollectionUtils.isNotEmpty(x.getOptionList()))
                .findFirst().orElse(new ConfigurationRuleAggr()).getOptionList().get(0).getDrivingFeatureCode();
        headInfoRespDto.setDriveFeatureCode(driveFeatureCode);
        headInfoRespDto.setDriveFeatureName(optionNameMaps.get(driveFeatureCode));
        List<RuleViewHeadInfoRespDto.DriveOptionInfo> optionHeadList = Lists.newArrayList();

        List<String> driveOptionCodes = Lists.newArrayList();
        ruleMap.forEach((ruleNumber, ruleAggrs) -> {
            List<ConfigurationRuleAggr> configurationRuleSorts =
                    ruleAggrs.stream().sorted(Comparator.comparing(ConfigurationRuleAggr::getRuleVersion).reversed()).toList();
            configurationRuleSorts.forEach(ruleSort -> {
                // 横轴排序
                List<String> drivingOptionCodes = ruleSort.getOptionList().stream().map(ConfigurationRuleOptionDo::getDrivingOptionCode).distinct().sorted(Comparator.reverseOrder()).toList();
                drivingOptionCodes.forEach(drivingOptionCode -> {
                    RuleViewHeadInfoRespDto.DriveOptionInfo driveOptionInfo = new RuleViewHeadInfoRespDto.DriveOptionInfo();
                    driveOptionInfo.setDriveOptionCode(drivingOptionCode);
                    driveOptionInfo.setDriveOptionName(optionNameMaps.get(drivingOptionCode));
                    driveOptionInfo.setRevision(ruleSort.getRuleVersion());
                    driveOptionInfo.setEffOut(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_SLASH, ruleSort.getEffOut()));
                    driveOptionInfo.setEffIn(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_SLASH, ruleSort.getEffIn()));
                    driveOptionInfo.setChangeType(ruleSort.getChangeType());
                    driveOptionInfo.setRuleId(ruleSort.getId());
                    optionHeadList.add(driveOptionInfo);
                    driveOptionCodes.add(drivingOptionCode);
                });
            });
        });
        ruleViewInfoRespDto.getRuleViewBasicInformationRespDto().setDrivingCriterias(driveOptionCodes.stream().distinct().toList());
        headInfoRespDto.setOptionHeadList(optionHeadList);
        ruleViewInfoRespDto.setHeadInfoRespDto(headInfoRespDto);
        ruleViewInfoRespDto.setRuleViewConstrainedLists(buildRuleViewConstrained(ruleEntities,headInfoRespDto,optionNameMaps));
        return ruleViewInfoRespDto;
    }


    public List<RuleViewConstrainedRespDto> buildRuleViewConstrained
            (List<ConfigurationRuleAggr> ruleEntities,
             RuleViewHeadInfoRespDto headInfoRespDto,
             Map<String, String> optionNameMaps) {

        List<RuleViewHeadInfoRespDto.DriveOptionInfo>  optionHeadList = headInfoRespDto.getOptionHeadList();

        List<RuleViewConstrainedRespDto> ruleViewConstrained = Lists.newArrayList();

        List<ConfigurationRuleOptionDo> configurationRuleOptionDos = Lists.newArrayList();
        ruleEntities.forEach(rule -> {
            configurationRuleOptionDos.addAll(rule.getOptionList());
        });

        List<String> constrainFeatureCodes = configurationRuleOptionDos.stream().map(ConfigurationRuleOptionDo::getConstrainedFeatureCode).distinct().sorted().toList();

        constrainFeatureCodes.forEach(constrainFeatureCode -> {
            RuleViewConstrainedRespDto ruleViewConstrainedRespDto = new RuleViewConstrainedRespDto();
            ruleViewConstrainedRespDto.setConstrainedFeatureCode(constrainFeatureCode);
            ruleViewConstrainedRespDto.setConstrainedFeatureName(optionNameMaps.get(constrainFeatureCode));

            List<RuleViewConstrainedRespDto.RuleViewConstrainedOption> constrainedOptionList = Lists.newArrayList();

            configurationRuleOptionDos.stream().filter(x -> StringUtils.equals(x.getConstrainedFeatureCode(), constrainFeatureCode))
                    .map(ConfigurationRuleOptionDo::getConstrainedOptionCode).distinct().sorted().toList().forEach(constrainOptionCode -> {
                        RuleViewConstrainedRespDto.RuleViewConstrainedOption constrainOption = new RuleViewConstrainedRespDto.RuleViewConstrainedOption();
                        constrainOption.setConstrainedOptionCode(constrainOptionCode);
                        constrainOption.setConstrainedOptionName(optionNameMaps.get(constrainOptionCode));
                        List<RuleViewConstrainedRespDto.RulePackageInfo> rulePackageInfos=Lists.newArrayList();
                        optionHeadList.forEach(driveOptionInfo -> {
                            ConfigurationRuleOptionDo ruleOptionDo = configurationRuleOptionDos.stream().filter(
                                            x -> StringUtils.equals(x.getDrivingOptionCode(), driveOptionInfo.getDriveOptionCode()) &&
                                                    StringUtils.equals(x.getConstrainedOptionCode(), constrainOptionCode)
                                                    && Objects.equals(x.getRuleId(), driveOptionInfo.getRuleId()))
                                    .findFirst().orElse(null);
                            RuleViewConstrainedRespDto.RulePackageInfo rulePackageInfo= new RuleViewConstrainedRespDto.RulePackageInfo();
                            rulePackageInfo.setConstrainedOptionCode(constrainOptionCode);
                            rulePackageInfo.setDriveFeatureCode(headInfoRespDto.getDriveFeatureCode());
                            rulePackageInfo.setDriveOptionCode(driveOptionInfo.getDriveOptionCode());
                            if (Objects.isNull(ruleOptionDo)) {
                                rulePackageInfo.setId(0L);
                                rulePackageInfo.setPackageCode("-");
                            }else{
                                rulePackageInfo.setId(ruleOptionDo.getRuleId());
                                rulePackageInfo.setPackageCode(RuleOptionMatrixValueEnum.getByCode(ruleOptionDo.getMatrixValue()).getMatrixValue());
                            }
                            rulePackageInfos.add(rulePackageInfo);
                        });
                        constrainOption.setPackageCodes(rulePackageInfos);
                        constrainedOptionList.add(constrainOption);
                    });

            ruleViewConstrainedRespDto.setConstrainedOptionList(constrainedOptionList);
            ruleViewConstrained.add(ruleViewConstrainedRespDto);
        });
        return ruleViewConstrained;
    }


    public Map<String, String> findOptionName(List<ConfigurationRuleAggr> ruleEntities) {
        Set<String> optionCodes = new HashSet<>(128);

        ruleEntities.forEach(rule -> {
            rule.getOptionList().forEach(option -> {
                optionCodes.add(option.getDrivingFeatureCode());
                optionCodes.add(option.getDrivingOptionCode());
                optionCodes.add(option.getConstrainedOptionCode());
                optionCodes.add(option.getConstrainedFeatureCode());
            });
        });
        List<BomsFeatureLibraryEntity> entityList = bomsFeatureLibraryDao.queryByFeatureCodes(optionCodes.stream().distinct().toList());
        return entityList.stream().collect(Collectors.toMap(BomsFeatureLibraryEntity::getFeatureCode, BomsFeatureLibraryEntity::getDisplayName));

    }

    private RuleViewInfoRespDto buildRuleViewBasicInformation(BomsConfigurationRuleGroupEntity entity) {
        RuleViewInfoRespDto ruleViewInfoRespDto = new RuleViewInfoRespDto();
        RuleViewBasicInformationRespDto basicInformationRespDto = new RuleViewBasicInformationRespDto();
        basicInformationRespDto.setChineseName(entity.getChineseName());
        basicInformationRespDto.setDescription(entity.getDescription());
        basicInformationRespDto.setPurpose(entity.getPurpose());
        basicInformationRespDto.setDisplayName(entity.getDisplayName());
        ruleViewInfoRespDto.setRuleViewBasicInformationRespDto(basicInformationRespDto);
        return ruleViewInfoRespDto;
    }*/
}
