package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/7/4
 */
@Data
public class EditGroupCmd implements Cmd {

    private String featureCode;

    private String displayName;

    private String chineseName;

    private String description;

    private String status;

    private String updateUser;

}
