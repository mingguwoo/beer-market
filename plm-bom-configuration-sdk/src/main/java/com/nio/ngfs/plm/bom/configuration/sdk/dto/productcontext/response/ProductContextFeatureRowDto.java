package com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/11
 */
@Data
public class ProductContextFeatureRowDto implements Dto {

    private Long rowId;

    private String featureCode;

    private String displayName;

    private String catalog;

    private List<ProductContextOptionRowDto> optionRowList;


    public ProductContextFeatureRowDto() {
        optionRowList = new ArrayList<>();
    }
}
