package com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request;

import lombok.Data;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/24
 */
@Data
public class FeatureOptionSyncReqDto {

    private String owner;

    private FeatureSyncDto feature;

    private List<OptionSyncDto> optionList;

}
