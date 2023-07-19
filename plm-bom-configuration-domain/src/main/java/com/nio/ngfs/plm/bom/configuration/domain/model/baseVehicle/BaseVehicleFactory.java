package com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.AddBaseVehicleCmd;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * @author bill.wang
 * @date 2023/7/18
 */
public class BaseVehicleFactory {
    
    private static final String id = RedisKeyConstant.BASE_VEHICLE_ID_KEY;

    private static final RedisTemplate redistemplate = new RedisTemplate<>();
    public static BaseVehicleAggr createBaseVehicle(AddBaseVehicleCmd cmd){
        return BaseVehicleAggr.builder()
                .baseVehicleId(createBaseVehicleId())
                .model(cmd.getModel())
                .modelYear(cmd.getModelYear())
                .region(cmd.getRegion())
                .regionEn(cmd.getRegionEn())
                .regionCn(cmd.getRegionCn())
                .driveHand(cmd.getDriveHand())
                .driveHandEn(cmd.getDriveHandEn())
                .driveHandCn(cmd.getDriveHandCn())
                .salesVersion(cmd.getSalesVersion())
                .salesVersionEn(cmd.getSalesVersionEn())
                .salesVersionCn(cmd.getSalesVersionCn())
                .createUser(cmd.getCreateUser())
                .build();
    }
    public static String createBaseVehicleId() {
        String baseVehicleId = ConfigConstants.BASE_VEHICLE_ID_PREFIX + redistemplate.opsForValue().increment(id, CommonConstants.INT_ONE);
        return baseVehicleId;
    }
}
