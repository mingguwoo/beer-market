package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

/**
 * @author bill.wang
 * @date 2023/10/23
 */
@Data
public class QueryConfigurationRuleQry implements Qry {

    private String model;

    private String modelYear;

    private String searchContent;
}
