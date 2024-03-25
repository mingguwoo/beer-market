package com.sh.beer.market.domain.facade.dto.request;


import lombok.Data;

import java.util.Map;

/**
 * @author
 */
@Data
public class EmailParamDto {



      private String templateNo;


      private String subject="";


      private String receiverEmail;


      private Map<String, Object> variables;
}
