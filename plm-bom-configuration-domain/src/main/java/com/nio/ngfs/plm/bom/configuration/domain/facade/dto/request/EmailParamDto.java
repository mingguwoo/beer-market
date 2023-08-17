package com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request;


import lombok.Data;

import java.util.Map;

/**
 * @author wangchao.wang
 */
@Data
public class EmailParamDto {



      private String templateNo;


      private String subject="";


      private String receiverEmail;


      private Map<String, Object> variables;
}
