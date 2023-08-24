package com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

/**
 * @author bill.wang
 * @date 2023/8/11
 */
@Data
public class ProductContextColumnDto implements Dto {

    private Long columnId;

    private String modelCode;

    private String modelYear;


}
