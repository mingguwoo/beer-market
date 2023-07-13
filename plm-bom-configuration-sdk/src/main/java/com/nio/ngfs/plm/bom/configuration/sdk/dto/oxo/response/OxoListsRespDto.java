package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response;


import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

import java.util.List;

@Data
public class OxoListsRespDto implements Dto {


        /**
         * 行信息
         */
        private List<OxoRowsRespDto>  oxoRowsRespDtos;


        /**
         * 表头信息
         */
        private List<OxoHeadRespDto> oxoHeadRespDtos;
}
