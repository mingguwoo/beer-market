package com.nio.ngfs.plm.bom.configuration.remote.dto.enovia;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/30
 */
@Data
public class SyncProductContextModelFeatureOptionDto {

    private String model;

    private List<ProductContextFeatureDto> feature;

}
