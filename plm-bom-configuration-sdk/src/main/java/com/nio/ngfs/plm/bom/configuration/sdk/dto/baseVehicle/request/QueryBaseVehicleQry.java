package com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
@Data
public class QueryBaseVehicleQry implements Qry {

    private String modelCode;

    private List<String> modelYear;

    private String regionOptionCode;

    private String driveHand;

    private String salesVersion;

    private String status;
}
