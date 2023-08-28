package com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/25
 */
@Data
public class ProductContextOptionsRespDto implements Dto {

    private List<String> modelCode;

    private List<String> groupCode;

}
