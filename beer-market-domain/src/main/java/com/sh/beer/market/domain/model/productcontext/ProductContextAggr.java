package com.sh.beer.market.domain.model.productcontext;

import com.sh.beer.market.domain.model.AbstractDo;
import com.sh.beer.market.domain.model.configurationrule.context.AggrRoot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author
 * @date 2023/8/10
 */
@Data
@SuperBuilder
@AllArgsConstructor
public class ProductContextAggr extends AbstractDo implements AggrRoot<Long> {

    /*private String modelCode;

    private String modelYear;

    private String optionCode;

    private String featureCode;

    @Override
    public Long getUniqId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelCode, modelYear, optionCode, featureCode);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ProductContextAggr aggr = (ProductContextAggr) obj;
        return modelCode.equals(aggr.modelCode) && modelYear.equals(aggr.modelYear) && optionCode.equals(aggr.optionCode) && featureCode.equals(aggr.getFeatureCode());
    }
*/
}
