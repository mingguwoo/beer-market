package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response;


import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Data
public class OxoListRespDto implements Dto {

        /**
         * 行信息
         */
        private List<OxoRowsQry>  oxoRowsResps;

        /**
         * 表头信息
         */
        private List<OxoHeadQry> oxoHeadResps;
}
