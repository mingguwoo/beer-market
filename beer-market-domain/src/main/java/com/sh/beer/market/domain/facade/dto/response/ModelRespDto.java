package com.sh.beer.market.domain.facade.dto.response;

import com.sh.beer.market.sdk.dto.common.Dto;
import lombok.Data;

import java.util.List;

/**
 * @author
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
