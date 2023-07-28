package com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage;

import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.OxoPackageInfoAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public class OxoOptionPackageFactory {

    public static List<OxoPackageInfoAggr> createOxoOptionPackageAggrList (List<OxoFeatureOptionAggr> oxoFeatureOptionAggrList, BaseVehicleAggr baseVehicleAggr){
        List<OxoPackageInfoAggr> resList =
                oxoFeatureOptionAggrList.stream().map(oxoFeatureOptionAggr -> {
            OxoPackageInfoAggr oxoPackageInfoAggr = new OxoPackageInfoAggr();
            oxoPackageInfoAggr.setFeatureOptionId(oxoFeatureOptionAggr.getId());
            oxoPackageInfoAggr.setBaseVehicleId(baseVehicleAggr.getId());
            oxoPackageInfoAggr.setPackageCode("default");
            oxoPackageInfoAggr.setBrand(ConfigConstants.brandName.get());
            return oxoPackageInfoAggr;
        }).toList();
        return resList;
    }
}
