package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.nio.ngfs.plm.bom.configuration.application.service.OxoOptionPackageApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.enums.OxoOptionPackageTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.basevehicle.BaseVehicleDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/28
 */
@Service
@RequiredArgsConstructor
public class OxoOptionPackageApplicationServiceImpl implements OxoOptionPackageApplicationService {


    private final BaseVehicleDomainService baseVehicleDomainService;

    private final OxoFeatureOptionRepository oxoFeatureOptionRepository;

    private final OxoOptionPackageRepository oxoOptionPackageRepository;



    private final BaseVehicleRepository baseVehicleRepository;

    @Override
    public void insertOxoOptionPackageDefault(List<OxoFeatureOptionAggr> oxoFeatureOptions,String modelCode,String userName) {

        //车型
        List<String> optionCodes = oxoFeatureOptions.stream().filter(x -> StringUtils.equals(FeatureTypeEnum.OPTION.getType(),x.getType()))
                .map(OxoFeatureOptionAggr::getFeatureCode).distinct().toList();

        //查询表头数据
        List<OxoHeadQry> oxoHeads = baseVehicleDomainService.queryByModel(modelCode,false);

        //查询行数据
        List<OxoFeatureOptionAggr> entityList = oxoFeatureOptionRepository.queryByModelAndFeatureCodeList(modelCode, optionCodes);


        String brandName = ConfigConstants.brandName.get();

        //则系统自动将Option在各个Base Vehicle下的打点赋值为实心圈
        oxoOptionPackageRepository.insertOxoOptionPackages(OxoOptionPackageFactory.buildOptionPackages(oxoHeads,
                entityList.stream().map(OxoFeatureOptionAggr::getId).distinct().toList(),
                OxoOptionPackageTypeEnum.DEFALUT.getType(), brandName, userName));

    }

    @Override
    public List<String> checkOxoCompleteBaseVehicle(String modelCode) {

        //查询头
        List<BaseVehicleAggr> baseVehicles=  baseVehicleRepository.queryByModelCodeAndModelYear(modelCode,null);


        //查询行信息
        List<OxoFeatureOptionAggr>  oxoFeatureOptions= oxoFeatureOptionRepository.queryFeatureListsByModelAndSortDelete(modelCode,null);


        List<Long> rowIds = oxoFeatureOptions.stream().map(OxoFeatureOptionAggr::getId).distinct().toList();

        List<Long> headIds = baseVehicles.stream().map(BaseVehicleAggr::getId).distinct().toList();


        //查询打点信息
        List<OxoOptionPackageAggr> oxoOptionPackages=  oxoOptionPackageRepository.queryByFeatureOptionIdsAndHeadIdsList(rowIds,headIds);


        //根据modelYear
//        Map<String,List<BaseVehicleAggr>> basicVehicles = baseVehicles.stream().collect
//                (Collectors.toMap(BaseVehicleAggr::getModelYear, x -> x.getFeatureId().getFeatureCode()));




        return null;
    }
}
