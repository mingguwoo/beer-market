package com.nio.ngfs.plm.bom.configuration.domain.model.modelyearconfig;

import com.nio.bom.share.domain.model.Identifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelYearConfigId implements Identifier {

    /**
     * Model
     */
    private String model;

    /**
     * Model Year
     */
    private String modelYear;

}
