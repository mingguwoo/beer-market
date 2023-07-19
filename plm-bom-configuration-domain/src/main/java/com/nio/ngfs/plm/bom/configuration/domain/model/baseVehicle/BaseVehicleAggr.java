package com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle;

import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.AddBaseVehicleCmd;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luke.zhu
 * @date 07/12/2023
 */
@Slf4j
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseVehicleAggr extends AbstractDo implements AggrRoot<String>, Cloneable {

    private String baseVehicleId;

    private String model;

    private String modelYear;

    private String region;

    private String regionEn;

    private String regionCn;

    private String driveHand;

    private String driveHandEn;

    private String driveHandCn;

    private String salesVersion;

    private String salesVersionEn;

    private String salesVersionCn;

    private String maturity;

    private String owner;

    private String status;

    @Override
    public String getUniqId() {
        return baseVehicleId;
    }

    public void addBaseVehicle(AddBaseVehicleCmd cmd){
        //校验maturity
        //校验status
        //赋值
    }
}