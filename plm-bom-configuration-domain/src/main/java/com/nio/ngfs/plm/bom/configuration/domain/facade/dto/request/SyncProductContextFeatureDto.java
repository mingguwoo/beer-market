package com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request;

import lombok.Data;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/9/15
 */
@Data
public class SyncProductContextFeatureDto {

        private String featureCode;

        List<SyncProductContextOptionDto> option;
}
