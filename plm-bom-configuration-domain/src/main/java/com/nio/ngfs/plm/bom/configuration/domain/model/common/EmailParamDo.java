package com.nio.ngfs.plm.bom.configuration.domain.model.common;


import lombok.Data;

import java.util.Map;

/**
 * @author wangchao.wang
 */
@Data
public class EmailParamDo {



      private String templateNo;


      private String subject;


      private String receiverEmail;


      private Map<String, Object> variables;
}
