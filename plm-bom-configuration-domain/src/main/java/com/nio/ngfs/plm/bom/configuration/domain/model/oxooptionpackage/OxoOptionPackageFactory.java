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


    public  static List<OxoOptionPackageAggr> buildOptionPackages(List<OxoHeadQry> oxoHeads,
                                                          List<Long> rowIds,
                                                          String packageCode,
                                                          String brandName,
                                                          String userName) {


        List<Long> headIds = Lists.newArrayList();
        oxoHeads.forEach(oxoHeadQry -> {
            oxoHeadQry.getRegionInfos().forEach(x -> {
                x.getDriveHands().forEach(y -> {
                    y.getSalesVersionInfos().forEach(z -> {
                        headIds.add(z.getHeadId());
                    });
                });
            });
        });

        List<OxoOptionPackageAggr> optionPackageAggrs=Lists.newArrayList();
        headIds.forEach(headId -> {
            rowIds.forEach(rowId -> {
                OxoOptionPackageAggr oxoOptionPackageAggr=new OxoOptionPackageAggr();
                oxoOptionPackageAggr.setPackageCode(packageCode);
                oxoOptionPackageAggr.setFeatureOptionId(rowId);
                oxoOptionPackageAggr.setBaseVehicleId(headId);
                oxoOptionPackageAggr.setCreateUser(userName);
                oxoOptionPackageAggr.setUpdateUser(userName);
                oxoOptionPackageAggr.setBrand(brandName);
                optionPackageAggrs.add(oxoOptionPackageAggr);
            });
        });
        return optionPackageAggrs;
    }

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