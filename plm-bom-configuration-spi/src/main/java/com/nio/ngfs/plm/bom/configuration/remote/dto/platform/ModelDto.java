package com.nio.ngfs.plm.bom.configuration.remote.dto.platform;

import lombok.Data;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
@Data
public class ModelDto {

    private Long id;

    private String model;

    private Integer state;

    private List<String> modelYear;

    private String brand;

}
