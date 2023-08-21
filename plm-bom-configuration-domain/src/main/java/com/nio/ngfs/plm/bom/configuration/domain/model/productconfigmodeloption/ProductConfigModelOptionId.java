package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodeloption;

import com.nio.bom.share.domain.model.Identifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaozhou.tu
 * @date 2023/8/21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductConfigModelOptionId implements Identifier {

    /**
     * 车型
     */
    private String modelCode;

    /**
     * Option Code
     */
    private String optionCode;

}
