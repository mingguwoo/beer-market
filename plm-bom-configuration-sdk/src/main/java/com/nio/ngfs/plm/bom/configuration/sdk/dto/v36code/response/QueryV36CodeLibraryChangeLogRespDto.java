package com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response;

import lombok.Data;

/**
 * @author bill.wang
 * @date 2023/9/20
 */
@Data
public class QueryV36CodeLibraryChangeLogRespDto {

    private String changeAttribute;

    private String oldValue;

    private String newValue;

    private String updateUser;

    private String updateTime;
}
