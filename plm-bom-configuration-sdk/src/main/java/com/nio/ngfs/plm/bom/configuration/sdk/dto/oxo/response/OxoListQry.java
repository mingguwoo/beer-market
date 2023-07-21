package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response;


import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

import java.util.List;

@Data
public class OxoListQry implements Qry {

        /**
         * 行信息
         */
        private List<OxoRowsQry>  oxoRowsResps;

        /**
         * 表头信息
         */
        private List<OxoHeadQry> oxoHeadResps;
}
