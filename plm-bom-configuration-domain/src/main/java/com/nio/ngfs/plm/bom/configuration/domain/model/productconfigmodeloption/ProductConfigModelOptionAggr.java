package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodeloption;

import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author xiaozhou.tu
 * @date 2023/8/21
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductConfigModelOptionAggr extends AbstractDo implements AggrRoot<ProductConfigModelOptionId> {

    /**
     * 唯一标识
     */
    private ProductConfigModelOptionId productConfigModelOptionId;

    /**
     * Feature Code
     */
    private String featureCode;

    @Override
    public ProductConfigModelOptionId getUniqId() {
        return productConfigModelOptionId;
    }

    public String getModelCode() {
        return productConfigModelOptionId.getModelCode();
    }

    public String getOptionCode() {
        return productConfigModelOptionId.getOptionCode();
    }

}
