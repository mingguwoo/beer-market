package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request;


import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author wangchao.wang
 */
@Data
public class SetBreakPointCmd implements Cmd {

      private String userName;

      private List<Long> ruleIds;

      @DateTimeFormat(pattern = "yyyy-MM-dd")
      private String effIn;

      @DateTimeFormat(pattern = "yyyy-MM-dd")
      private String effOut;




}
