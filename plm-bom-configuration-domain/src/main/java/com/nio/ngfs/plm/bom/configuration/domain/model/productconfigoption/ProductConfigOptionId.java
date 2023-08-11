package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption;

import com.nio.bom.share.domain.model.Identifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
     * PC id
     */
    private String pcId;

    /**
     * Option Code
     */
    private String optionCode;

}
