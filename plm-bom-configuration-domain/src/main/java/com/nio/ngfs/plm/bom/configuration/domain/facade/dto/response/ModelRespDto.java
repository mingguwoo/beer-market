package com.nio.ngfs.plm.bom.configuration.domain.facade.dto.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/11
 */
@Data
public class ModelRespDto implements Dto {

    private Long id;

    private String model;

    private Integer state;

    private List<String> modelYear;

    private String brand;

}
