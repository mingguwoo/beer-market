package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author wangchao.wang
 */
@Data
public class OxoEditCmd extends OxoBaseCmd{




     @NotBlank(message = "headId不能为空")
     private Long headId;


     private Long rowId;


     private String packageCode;


     private String description;

     /**
      * 评论
      */
     private String comments;



     private String ruleCheck;




}


