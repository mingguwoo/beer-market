package com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature;

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
public class ProductContextFeatureAggr extends AbstractDo implements AggrRoot<Long>{

    private String modelCode;

    private String featureCode;

    private String featureGroup;

    private String type;

    private int featureSeq;
    @Override
    public Long getUniqId() {
        return id;
    }
}