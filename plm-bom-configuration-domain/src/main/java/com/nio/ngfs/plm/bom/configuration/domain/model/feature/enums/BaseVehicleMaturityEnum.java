package com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums;

import com.nio.bom.share.utils.EnumUtils;
import com.nio.ngfs.plm.bom.configuration.common.enums.StatusEnum;

/**
 * @author bill.wang
 * @date 2023/7/19
 */
public enum BaseVehicleMaturityEnum {

    /**
     * Base Vehicle的Maturity属性
     */
    U("U"),
    P("P");

    private final String maturity;

    public String getMaturity() {
        return maturity;
    }

    public static BaseVehicleMaturityEnum getByMaturity(String maturity) {
        return EnumUtils.getEnum(BaseVehicleMaturityEnum.class, BaseVehicleMaturityEnum::getMaturity, maturity);
    }

    BaseVehicleMaturityEnum(String maturity) {
        this.maturity = maturity;
    }

}

