package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@Data
public class GetModelListRespDto implements Dto {

    private String model;

    private List<String> modelYearList;

}
