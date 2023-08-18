package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request;


import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import lombok.Data;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Data
public class OxoEditInfoCmd extends OxoBaseCmd{


       private List<OxoEditCmd> editCmds;


       private List<OxoRowsQry> editOxoRows;
}
