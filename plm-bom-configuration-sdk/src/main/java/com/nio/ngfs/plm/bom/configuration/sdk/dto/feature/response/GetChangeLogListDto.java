package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/7/18
 */
@Data
public class GetChangeLogListDto implements Dto {

    private String changeAttribute;

    private String oldValue;

    private String newValue;

    private String updateUser;

    private String updateTime;

}
