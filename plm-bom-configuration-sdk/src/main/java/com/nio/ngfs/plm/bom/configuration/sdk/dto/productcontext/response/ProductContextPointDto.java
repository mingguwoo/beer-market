package com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

/**
 * @author bill.wang
 * @date 2023/8/11
 */
@Data
public class ProductContextPointDto implements Dto {

    private Long rowId;

    private Long columnId;
}
