package com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request;

import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/7/24
 */
@Data
public class OptionSyncDto {

    private String optionCode;

    private String displayName;

    private String chineseName;

    private String description;

    private FeatureOptionSyncReqDto.FeatureSyncType type;

}
