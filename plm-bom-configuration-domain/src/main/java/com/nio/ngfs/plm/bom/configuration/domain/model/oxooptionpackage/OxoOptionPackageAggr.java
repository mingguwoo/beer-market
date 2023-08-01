package com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage;

import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.enums.OxoOptionPackageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Slf4j
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OxoOptionPackageAggr extends AbstractDo implements AggrRoot<Long> {

    /**
     * oxo行id
     */
    private Long featureOptionId;

    /**
     * oxo列id
     */
    private Long baseVehicleId;

    /**
     * Default ●  Unavailable - Available ○
     */
    private String packageCode;

    /**
     * 描述
     */
    private String description;

    /**
     * 品牌
     */
    private String brand;

    @Override
    public Long getUniqId() {
        return id;
    }

    /**
     * 删除打点信息
     */
    public boolean deleteOptionPackage() {
        if (isPackageUnavailable()) {
            return false;
        }
        setPackageCode(OxoOptionPackageTypeEnum.UNAVAILABLE.getType());
        return true;
    }

    public boolean isPackageUnavailable() {
        return Objects.equals(packageCode, OxoOptionPackageTypeEnum.UNAVAILABLE.getType());
    }

}
