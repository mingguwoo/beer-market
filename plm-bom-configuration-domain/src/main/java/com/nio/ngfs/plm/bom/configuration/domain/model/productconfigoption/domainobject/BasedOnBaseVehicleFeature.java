package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.domainobject;

import com.nio.bom.share.constants.CommonConstants;
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
        if (optionList.size() == 1 && optionList.get(0).isDefault()) {
            return BasedOnBaseVehicleTypeEnum.ONLY_Default;
        } else if (isOnlyDefaultAndUnavailable()) {
            return BasedOnBaseVehicleTypeEnum.ONLY_Default_AND_Unavailable;
        } else if (optionList.stream().allMatch(BasedOnBaseVehicleOption::isUnavailable)) {
            return BasedOnBaseVehicleTypeEnum.ALL_Unavailable;
        } else if (optionList.stream().anyMatch(BasedOnBaseVehicleOption::isAvailable)) {
            return BasedOnBaseVehicleTypeEnum.EXIST_Available;
        }
        log.error("getBasedOnBaseVehicleType error, all option package code is empty, featureCode={} optionList={}",
                featureCode, GsonUtils.toJson(optionList));
        throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_BASED_ON_BASE_VEHICLE_TYPE_ERROR);
    }

    private boolean isOnlyDefaultAndUnavailable() {
        if (optionList.size() != CommonConstants.INT_TWO) {
            return false;
        }
        BasedOnBaseVehicleOption firstOption = optionList.get(0);
        BasedOnBaseVehicleOption secondOption = optionList.get(1);
        return (firstOption.isDefault() && secondOption.isUnavailable()) ||
                (firstOption.isUnavailable() && secondOption.isDefault());
    }

}
