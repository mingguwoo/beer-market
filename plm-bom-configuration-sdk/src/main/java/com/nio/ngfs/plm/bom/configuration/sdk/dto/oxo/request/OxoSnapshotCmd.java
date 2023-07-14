package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request;


import javax.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class OxoSnapshotCmd extends OxoBaseCmd{


    /**
     * formal 大版本AA AB
     * informal 小版本 AA.1 AA.2
     */
     private String type;


     @Size(max = 64)
     private String title;


     @Size(max = 300)
     private String changeContent;



     private List<String> emailUsers;
}
