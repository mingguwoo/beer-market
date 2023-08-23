package com.nio.ngfs.plm.bom.configuration.domain.model.productcontext;

import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
/**
 * @author bill.wang
 * @date 2023/8/10
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductContextAggr extends AbstractDo implements AggrRoot<Long>{

    private String modelCode;

    private String modelYear;

    private String optionCode;

    private String featureCode;

    @Override
    public Long getUniqId() {
        return id;
    }

    @Override
    public int hashCode(){
        return (modelCode+modelYear+optionCode+featureCode).hashCode();
    }
    @Override
    public boolean equals(final Object obj){
        ProductContextAggr aggr = (ProductContextAggr) obj;
        return modelCode.equals(aggr.modelCode) && modelYear.equals(aggr.modelYear) && optionCode.equals(aggr.optionCode) && featureCode.equals(aggr.getFeatureCode());
    }
}
