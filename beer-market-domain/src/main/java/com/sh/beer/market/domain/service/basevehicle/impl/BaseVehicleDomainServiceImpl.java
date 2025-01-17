package com.sh.beer.market.domain.service.basevehicle.impl;

import com.sh.beer.market.domain.service.basevehicle.BaseVehicleDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author
 * @date 2023/7/19
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BaseVehicleDomainServiceImpl implements BaseVehicleDomainService {

    /*private final BaseVehicleRepository baseVehicleRepository;

    private final FeatureRepository featureRepository;

    @Override
    public void checkBaseVehicleUnique(BaseVehicleAggr baseVehicleAggr) {
        List<BaseVehicleAggr> existedBaseVehicleAggrList = baseVehicleRepository.queryByModelCodeModelYearRegionDriveHandSalesVersion(baseVehicleAggr.getModelCode(), baseVehicleAggr.getModelYear(),
                baseVehicleAggr.getRegionOptionCode(), baseVehicleAggr.getDriveHand(), baseVehicleAggr.getSalesVersion());
        if (CollectionUtils.isNotEmpty(existedBaseVehicleAggrList)) {
            //如果是edit自身记录，不报错，直接跳过
            if (!checkIsEdit(existedBaseVehicleAggrList, baseVehicleAggr)) {
                throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_REPEAT);
            }
        }
    }

    *//**
     * 校验BaseVehicle是否存在
     *//*
    @Override
    public void checkBaseVehicleExist(BaseVehicleAggr baseVehicleAggr) {
        if (Objects.isNull(baseVehicleAggr)) {
            throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_NOT_EXISTS);
        }
    }


    @Override
    public BaseVehicleAggr getBaseVehicleByBaseVehicleId(String baseVehicleId) {
        return baseVehicleRepository.queryBaseVehicleByBaseVehicleId(baseVehicleId);
    }

    private boolean checkIsEdit(List<BaseVehicleAggr> existedBaseVehicleAggrList, BaseVehicleAggr baseVehicleAggr) {
        //判断是不是edit
        if (Objects.nonNull(baseVehicleAggr.getBaseVehicleId())) {
            existedBaseVehicleAggrList.forEach(existedBaseVehicleAggr -> {
                //如果不是自身记录，报错
                if (!Objects.equals(baseVehicleAggr.getBaseVehicleId(), existedBaseVehicleAggr.getBaseVehicleId())) {
                    throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_REPEAT);
                }
            });
            return true;
        }
        return false;
    }


    @Override
    public List<OxoHeadQry> queryByModel(String modelCode, Boolean isMaturity) {

        //获取表头数据
        List<BaseVehicleAggr> baseVehicleAggrs = baseVehicleRepository.queryByModel(modelCode, isMaturity);


        List<OxoHeadQry> headQry = Lists.newArrayList();

        if (CollectionUtils.isEmpty(baseVehicleAggrs)) {
            return headQry;
        }

        // 获取中英文名称
        List<String> optionCodes = BaseVehicleFactory.convertOptionCodes(baseVehicleAggrs);

        List<FeatureAggr> featureAggrs = featureRepository.queryByFeatureOptionCodeList(optionCodes);

        if (CollectionUtils.isEmpty(featureAggrs)) {
            return headQry;
        }

        //查询AF00信息 排序
        Map<String, String> featureAggrMap =
                featureRepository.queryByParentFeatureCodeAndType(ConfigConstants.FEATURE_CODE_AF00, FeatureTypeEnum.OPTION.getType())
                        .stream().collect(Collectors.toMap(FeatureAggr::getDisplayName, x -> x.getFeatureId().getFeatureCode()));

        Map<String, List<FeatureAggr>> featureIdListMap = featureAggrs.stream()
                .filter(x -> StringUtils.equals(x.getFeatureId().getType(), FeatureTypeEnum.OPTION.getType()))
                .collect(Collectors.groupingBy(x -> x.getFeatureId().getFeatureCode()));

        Map<String, List<BaseVehicleAggr>> basicVehicleAggrMaps =
                baseVehicleAggrs.stream()
                        .collect(Collectors.groupingBy(BaseVehicleAggr::getModelYear));


        *//**
         * 表头排序规则（从左至右）
         * 层级1-按照AF00的Option首字母正常升序（预期结果：MY2018/G1.1/G1.2/G1.3/G1.F/G1.5/G1.6)
         * 层级2-按照AD00的Option首字母正常升序
         * 层级3-按照AN00的Option首字母正常升序
         * 层级4-按照19AA的Option首字母正常升序
         *//*
        basicVehicleAggrMaps.forEach((k, v) -> {
            OxoHeadQry oxoHeadQry = new OxoHeadQry();
            oxoHeadQry.setModelCode(modelCode);
            oxoHeadQry.setModelYear(k);
            Map<String, List<BaseVehicleAggr>> regionVehicleAggrMaps =
                    v.stream().sorted(Comparator.comparing(BaseVehicleAggr::getRegionOptionCode))
                            .collect(Collectors.groupingBy(BaseVehicleAggr::getRegionOptionCode,LinkedHashMap::new,Collectors.toList()));

            List<OxoHeadQry.RegionInfo> regionInfos = new ArrayList<>();

            regionVehicleAggrMaps.forEach((region, regionLists) -> {
                if (featureIdListMap.containsKey(region)) {
                    OxoHeadQry.RegionInfo regionInfo = new OxoHeadQry.RegionInfo();
                    regionInfo.setRegionName(featureIdListMap.get(region).get(0).getDisplayName());
                    regionInfo.setRegionCode(region);

                    Map<String, List<BaseVehicleAggr>> driveVehicleAggrMaps =
                            regionLists.stream().sorted(Comparator.comparing(BaseVehicleAggr::getDriveHand))
                                    .collect(Collectors.groupingBy(BaseVehicleAggr::getDriveHand,LinkedHashMap::new,Collectors.toList()));

                    List<OxoHeadQry.DriveHandInfo> driveHandInfos = Lists.newArrayList();
                    driveVehicleAggrMaps.forEach((drive, driveLists) -> {
                        if (featureIdListMap.containsKey(drive)) {
                            OxoHeadQry.DriveHandInfo driveHandInfo = new OxoHeadQry.DriveHandInfo();
                            driveHandInfo.setDriveHandCode(drive);
                            driveHandInfo.setDriveHandName(featureIdListMap.get(drive).get(0).getDisplayName());

                            Map<String, List<BaseVehicleAggr>> salesVehicleAggrMaps =
                                    driveLists.stream().sorted(Comparator.comparing(BaseVehicleAggr::getSalesVersion))
                                            .collect(Collectors.groupingBy(BaseVehicleAggr::getSalesVersion,LinkedHashMap::new,Collectors.toList()));

                            List<OxoHeadQry.SalesVersionInfo> salesVersionInfos = Lists.newArrayList();

                            salesVehicleAggrMaps.forEach((saleVersion, saleLists) -> {
                                if (featureIdListMap.containsKey(saleVersion)) {
                                    saleLists.forEach(sales -> {
                                        OxoHeadQry.SalesVersionInfo salesVersionInfo = new OxoHeadQry.SalesVersionInfo();
                                        salesVersionInfo.setSalesName(featureIdListMap.get(saleVersion).get(0).getChineseName());
                                        salesVersionInfo.setSalesCode(saleVersion);
                                        salesVersionInfo.setHeadId(sales.getId());
                                        salesVersionInfos.add(salesVersionInfo);
                                    });
                                }
                            });
                            driveHandInfo.setSalesVersionInfos(salesVersionInfos);
                            driveHandInfos.add(driveHandInfo);
                        }
                        regionInfo.setDriveHands(driveHandInfos);
                    });
                    regionInfos.add(regionInfo);
                }
            });
            oxoHeadQry.setRegionInfos(regionInfos);
            headQry.add(oxoHeadQry);
        });
        return headQry.stream().sorted(Comparator.comparing(x -> featureAggrMap.get(x.getModelYear()))).toList();
    }*/
}
