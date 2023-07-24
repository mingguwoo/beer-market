package com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request;

import lombok.Data;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/24
 */
@Data
public class FeatureSyncDto {

    private FeatureOptionSyncReqDto.FeatureSyncType type;

    private String groupCode;

    private String featureCode;

    private String displayName;

    private String chineseName;

    private String description;

    private String selectionType;

    private String catalog;

    private List<OptionSyncDto> optionList;

}
