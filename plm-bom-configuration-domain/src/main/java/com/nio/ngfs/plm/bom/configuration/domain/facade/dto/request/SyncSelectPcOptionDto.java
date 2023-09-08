package com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request;

import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/9/8
 */
@Data
public class SyncSelectPcOptionDto {

    private String pcId;

    private String optionCode;

    private String featureCode;

}
