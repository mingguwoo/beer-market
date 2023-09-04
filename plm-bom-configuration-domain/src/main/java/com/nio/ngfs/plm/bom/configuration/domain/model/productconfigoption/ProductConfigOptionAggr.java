package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.enums.ProductConfigOptionSelectStatusEnum;
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
     * 勾选状态，取值Select、Unselect
     */
    private String selectStatus;

    /**
     * 勾选是否可编辑，取值Yes、No
     */
    private String selectCanEdit;

    /**
     * 类型，0-正常，1-From Base Vehicle，2-From PC
     */
    private Integer type;

    @Override
    public ProductConfigOptionId getUniqId() {
        return productConfigOptionId;
    }

    public void changeSelectStatus(boolean select) {
        setSelectStatus(select ? ProductConfigOptionSelectStatusEnum.SELECT.getStatus() :
                ProductConfigOptionSelectStatusEnum.UNSELECT.getStatus());
    }

    public String getPcId() {
        return productConfigOptionId.getPcId();
    }

    public String getOptionCode() {
        return productConfigOptionId.getOptionCode();
    }

    public boolean isSelect() {
        return Objects.equals(selectStatus, ProductConfigOptionSelectStatusEnum.SELECT.getStatus());
    }

    public boolean isSelectCanEdit() {
        return Objects.equals(selectCanEdit, CommonConstants.YES);
    }

}
