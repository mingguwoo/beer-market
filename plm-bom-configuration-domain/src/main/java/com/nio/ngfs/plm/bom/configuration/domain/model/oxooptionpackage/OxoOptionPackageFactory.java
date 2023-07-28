package com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;

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
}
