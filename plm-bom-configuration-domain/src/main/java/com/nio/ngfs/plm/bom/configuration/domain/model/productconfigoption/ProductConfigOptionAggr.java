package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption;

import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.bom.share.enums.YesOrNoEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.enums.ProductConfigOptionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/8/11
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductConfigOptionAggr extends AbstractDo implements AggrRoot<ProductConfigOptionId> {

    /**
     * 唯一标识
     */
    private ProductConfigOptionId productConfigOptionId;

    /**
     * Feature Code
     */
    private String featureCode;

    /**
     * 勾选状态，0-Unselect，1-Select
     */
    private Integer selectStatus;

    /**
     * From Base Vehicle下是否可人工编辑，0-No，1-Yes
     */
    private Integer selectCanEdit;

    /**
     * 类型，0-正常，1-From Base Vehicle，2-From PC
     */
    private Integer type;

    @Override
    public ProductConfigOptionId getUniqId() {
        return productConfigOptionId;
    }

    public Long getPcId() {
        return productConfigOptionId.getPcId();
    }

    public String getOptionCode() {
        return productConfigOptionId.getOptionCode();
    }

    /**
     * 变更勾选状态
     */
    public void changeSelectStatus(boolean select) {
        setSelectStatus(select ? YesOrNoEnum.YES.getCode() : YesOrNoEnum.NO.getCode());
    }

    /**
     * 是否勾选
     */
    public boolean isSelect() {
        return Objects.equals(selectStatus, YesOrNoEnum.YES.getCode());
    }

    /**
     * 勾选是否可编辑
     */
    public boolean isSelectCanEdit() {
        return Objects.equals(selectCanEdit, YesOrNoEnum.YES.getCode());
    }

    /**
     * 是否From BaseVehicle
     */
    public boolean isFromBaseVehicle() {
        return Objects.equals(type, ProductConfigOptionTypeEnum.FROM_BASE_VEHICLE.getType());
    }

}
