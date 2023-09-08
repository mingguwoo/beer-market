package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption;

import com.nio.bom.share.domain.model.Identifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/8/11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductConfigOptionId implements Identifier {

    /**
     * PC主键id
     */
    private Long pcId;

    /**
     * Option Code
     */
    private String optionCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductConfigOptionId that = (ProductConfigOptionId) o;
        return Objects.equals(pcId, that.pcId) && Objects.equals(optionCode, that.optionCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pcId, optionCode);
    }

}
