package com.nio.ngfs.plm.bom.configuration.domain.model.oxo;


import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangchao.wang
 */
@Slf4j
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OxoPackageInfoAggr extends AbstractDo  implements AggrRoot<Long>, Cloneable {



    private Long featureOptionId;

    private Long baseVehicleId;

    private String packageCode;

    private String description;

    private String brand;

    @Override
    public Long getUniqId() {
        return getId();
    }

    @Override
    public OxoPackageInfoAggr clone() {
        try {
            OxoPackageInfoAggr clone = (OxoPackageInfoAggr) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
