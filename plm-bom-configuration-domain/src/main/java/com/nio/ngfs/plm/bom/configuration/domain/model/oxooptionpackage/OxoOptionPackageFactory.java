package com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage;


import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public class OxoOptionPackageFactory {


    public static List<OxoOptionPackageAggr> buildOptionPackages(List<OxoHeadQry> oxoHeads,
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

        List<OxoOptionPackageAggr> optionPackageAggrs = Lists.newArrayList();
        headIds.forEach(headId -> {
            rowIds.forEach(rowId -> {
                OxoOptionPackageAggr oxoOptionPackageAggr = new OxoOptionPackageAggr();
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

    public static List<OxoOptionPackageAggr> createOxoOptionPackageAggrList (List<OxoFeatureOptionAggr> oxoFeatureOptionAggrList, BaseVehicleAggr baseVehicleAggr){
        List<OxoOptionPackageAggr> resList =
                oxoFeatureOptionAggrList.stream().map(oxoFeatureOptionAggr -> {
                    OxoOptionPackageAggr oxoPackageInfoAggr = new OxoOptionPackageAggr();
            oxoPackageInfoAggr.setFeatureOptionId(oxoFeatureOptionAggr.getId());
            oxoPackageInfoAggr.setBaseVehicleId(baseVehicleAggr.getId());
            oxoPackageInfoAggr.setPackageCode("Default");
            oxoPackageInfoAggr.setBrand(ConfigConstants.brandName.get());
            oxoPackageInfoAggr.setCreateUser(baseVehicleAggr.getCreateUser());
            oxoPackageInfoAggr.setBrand(ConfigConstants.brandName.get());
            return oxoPackageInfoAggr;
        }).toList();
        return resList;
    }


    public static List<OxoOptionPackageAggr> createOxoOptionPackageAggrList(List<OxoEditCmd> cmdLists, String userName) {

        String brand = ConfigConstants.brandName.get();
        return cmdLists.stream().map(x -> {
            OxoOptionPackageAggr packageAggr = new OxoOptionPackageAggr();
            packageAggr.setFeatureOptionId(x.getRowId());
            packageAggr.setBaseVehicleId(x.getHeadId());
            packageAggr.setPackageCode(x.getPackageCode());
            packageAggr.setBrand(brand);
            packageAggr.setCreateUser(userName);
            packageAggr.setDescription(x.getDescription());
            packageAggr.setUpdateUser(userName);
            return packageAggr;

        }).toList();


    }

}
