package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request;


import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

/**
 * @author wangchao.wang
 */
@Data
public class BreakPointCheckCmd implements Cmd {


     private String model;

     private String modelYear;



}
