package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request;


import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class OxoEditInfoCmd extends OxoBaseCmd{


       @NotEmpty
       @Valid
       private List<OxoEditCmd> editCmds;




}
