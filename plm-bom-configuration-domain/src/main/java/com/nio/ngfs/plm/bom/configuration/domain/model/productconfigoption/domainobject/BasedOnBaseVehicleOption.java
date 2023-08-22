package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.domainobject;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.enums.OxoOptionPackageTypeEnum;
import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/8/14
 */
@Data
public class BasedOnBaseVehicleOption {

    private String optionCode;

    private String featureCode;

    private String packageCode;

    public boolean isDefault() {
        return OxoOptionPackageTypeEnum.DEFALUT.getType().equals(packageCode);
    }

    public boolean isAvailable() {
        return OxoOptionPackageTypeEnum.AVAILABLE.getType().equals(packageCode);
    }

    public boolean isUnavailable() {
        return OxoOptionPackageTypeEnum.UNAVAILABLE.getType().equals(packageCode);
    }

}
