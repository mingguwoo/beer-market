package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request;


import lombok.Data;

import java.util.List;

@Data
public class OxoEditInfoCmd extends OxoBaseCmd{

       private List<OxoEditCmd> editCmds;
}
