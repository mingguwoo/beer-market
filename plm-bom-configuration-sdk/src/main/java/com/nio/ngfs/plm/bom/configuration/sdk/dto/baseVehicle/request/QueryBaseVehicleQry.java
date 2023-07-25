package com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
@Data
public class QueryBaseVehicleQry implements Qry {

    private String modelCode;

    private Set<String> modelYear;

    private String regionOptionCode;

    private String driveHand;

    private String salesVersion;

    private String status;
}
