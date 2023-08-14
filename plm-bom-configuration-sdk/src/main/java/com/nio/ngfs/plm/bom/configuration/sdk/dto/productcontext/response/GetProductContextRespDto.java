package com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
@Data
public class GetProductContextRespDto implements Dto {

    //行信息
    private List<ProductContextFeatureRowDto> productContextFeatureRowDtoList;

    //列信息
    private List<ProductContextColumnDto> productContextColumnDtoList;

    //记录打点信息
    private List<ProductContextPointDto> productContextPointDtoList;
}
