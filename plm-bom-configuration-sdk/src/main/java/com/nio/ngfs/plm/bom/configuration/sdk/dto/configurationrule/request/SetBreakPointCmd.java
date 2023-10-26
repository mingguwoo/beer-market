package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request;


import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Data
public class SetBreakPointCmd implements Cmd {



      private String userName;

      private List<Long> ruleIds;

      private String effIn;

      private String effOut;




}
