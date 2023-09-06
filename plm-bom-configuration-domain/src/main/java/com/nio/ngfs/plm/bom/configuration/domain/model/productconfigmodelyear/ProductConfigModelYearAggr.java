package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodelyear;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductConfigModelYearAggr extends AbstractDo implements AggrRoot<ProductConfigModelYearId> {

    /**
     * 唯一标识
     */
    private ProductConfigModelYearId productConfigModelYearId;

    /**
     * OXO是否Release，取值Yes、No
     */
    private String oxoRelease;

    @Override
    public ProductConfigModelYearId getUniqId() {
        return productConfigModelYearId;
    }

    public String getModel() {
        return productConfigModelYearId.getModel();
    }

    public String getModelYear() {
        return productConfigModelYearId.getModelYear();
    }

    /**
     * OXO发布
     */
    public boolean oxoReleased() {
        if (Objects.equals(oxoRelease, CommonConstants.YES)) {
            return false;
        }
        setOxoRelease(CommonConstants.YES);
        return true;
    }

}
