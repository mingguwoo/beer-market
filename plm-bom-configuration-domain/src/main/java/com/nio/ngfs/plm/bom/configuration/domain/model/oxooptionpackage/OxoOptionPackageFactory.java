package com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage;


import com.google.common.collect.Lists;
import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public static List<OxoOptionPackageAggr> createOxoOptionPackageAggrList (List<OxoFeatureOptionAggr> oxoFeatureOptionAggrList, BaseVehicleAggr baseVehicleAggr,String modelYearCode){
        //判断是否oxo中有对应model year的行信息
        if (!oxoFeatureOptionAggrList.stream().filter(aggr->(Objects.equals(aggr.getFeatureCode().substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO),ConfigConstants.FEATURE_CODE_AF00.substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO)))).map(aggr->aggr.getFeatureCode())
                .collect(Collectors.toSet()).contains(modelYearCode)){
                throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_NO_MODEL_YEAR_OPTON_IN_OXO.getCode(),String.format(ConfigErrorCode.BASE_VEHICLE_NO_MODEL_YEAR_OPTON_IN_OXO.getMessage(),modelYearCode));
        }
        List<OxoOptionPackageAggr> resList =
            oxoFeatureOptionAggrList.stream().map(oxoFeatureOptionAggr -> {
            OxoOptionPackageAggr oxoPackageInfoAggr = new OxoOptionPackageAggr();
            oxoPackageInfoAggr.setFeatureOptionId(oxoFeatureOptionAggr.getId());
            oxoPackageInfoAggr.setBaseVehicleId(baseVehicleAggr.getId());
            //如果是被选中的点
            if (Objects.equals(oxoFeatureOptionAggr.getFeatureCode(),baseVehicleAggr.getDriveHand())
            || Objects.equals(oxoFeatureOptionAggr.getFeatureCode(),baseVehicleAggr.getRegionOptionCode())
            || Objects.equals(oxoFeatureOptionAggr.getFeatureCode(),baseVehicleAggr.getSalesVersion())
            || Objects.equals(oxoFeatureOptionAggr.getFeatureCode(),modelYearCode)){
                oxoPackageInfoAggr.setPackageCode("Default");
            }
            else{
                oxoPackageInfoAggr.setPackageCode("Unavailable");
            }
            oxoPackageInfoAggr.setBrand(ConfigConstants.brandName.get());
            oxoPackageInfoAggr.setCreateUser(baseVehicleAggr.getCreateUser());
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
