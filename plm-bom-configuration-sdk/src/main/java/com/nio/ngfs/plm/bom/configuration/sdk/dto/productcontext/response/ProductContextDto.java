package com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

/**
 * @author bill.wang
 * @date 2023/8/11
 */
@Data
public class ProductContextDto implements Dto {

    private String modelCode;

    private String modelYear;

    private String optionCode;

    private String featureCode;

    private String createUser;

    private String updateUser;

    private String createTime;

    private String updateTime;
}
