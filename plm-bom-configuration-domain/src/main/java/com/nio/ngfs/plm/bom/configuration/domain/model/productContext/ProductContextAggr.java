package com.nio.ngfs.plm.bom.configuration.domain.model.productContext;

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
public class ProductContextAggr extends AbstractDo implements AggrRoot<String>, Cloneable{

    private String modelCode;

    private String modelYear;

    private String optionCode;

    private String featureCode;

    @Override
    public String getUniqId() {
        return null;
    }
}
