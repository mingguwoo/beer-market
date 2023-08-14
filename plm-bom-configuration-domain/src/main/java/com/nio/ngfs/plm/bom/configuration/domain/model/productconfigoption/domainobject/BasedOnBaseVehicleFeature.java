package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.domainobject;

import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.GsonUtils;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.enums.BasedOnBaseVehicleTypeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/14
 */
@Slf4j
@Data
public class BasedOnBaseVehicleFeature {

    private String featureCode;

    private List<BasedOnBaseVehicleOption> optionList;

    public BasedOnBaseVehicleTypeEnum getBasedOnBaseVehicleType() {
        if (optionList.stream().allMatch(BasedOnBaseVehicleOption::isDefault)) {
            return BasedOnBaseVehicleTypeEnum.ALL_Default;
        } else if (optionList.stream().allMatch(i -> i.isDefault() || i.isUnavailable())) {
            return BasedOnBaseVehicleTypeEnum.ALL_Default_AND_Unavailable;
        } else if (optionList.stream().allMatch(BasedOnBaseVehicleOption::isUnavailable)) {
            return BasedOnBaseVehicleTypeEnum.ALL_Unavailable;
        } else if (optionList.stream().anyMatch(BasedOnBaseVehicleOption::isAvailable)) {
            return BasedOnBaseVehicleTypeEnum.EXIST_Available;
        }
        log.error("getBasedOnBaseVehicleType error, all option package code is empty, featureCode={} optionList={}",
                featureCode, GsonUtils.toJson(optionList));
        throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_BASED_ON_BASE_VEHICLE_TYPE_ERROR);
    }

}
