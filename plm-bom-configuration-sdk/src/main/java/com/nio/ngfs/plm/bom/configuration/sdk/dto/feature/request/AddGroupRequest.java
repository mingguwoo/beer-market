package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request;

import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Data
public class AddGroupRequest {

    private String groupCode;

    private String displayName;

    private String chineseName;

    private String description;

}
