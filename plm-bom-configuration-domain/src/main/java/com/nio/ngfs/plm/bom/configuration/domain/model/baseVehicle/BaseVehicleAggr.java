package com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle;

import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.bom.share.enums.StatusEnum;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.BaseVehicleMaturityEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.AddBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.EditBaseVehicleCmd;
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

    private String modelCode;

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

    private String status;

    @Override
    public String getUniqId() {
        return baseVehicleId;
    }

    public void addBaseVehicle(AddBaseVehicleCmd cmd){
        //校验maturity并赋值
        checkAndSetMaturity(cmd.getMaturity());
        //赋值Status
        setStatus(StatusEnum.ACTIVE.getStatus());
    }

    private void checkAndSetMaturity(String maturity){
        //maturity为空默认赋值U
        if (maturity == null) {
            maturity = BaseVehicleMaturityEnum.U.getMaturity();
        }
        if (BaseVehicleMaturityEnum.getByMaturity(maturity) == null){
            throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_MATURITY_INVALID);
        }
        setMaturity(maturity);
    }

    public void editBaseVehicle(EditBaseVehicleCmd cmd){
        //校验model，model year是否被改了
        checkModelAndModelYear(cmd);
        //赋值
        changeMaturity(cmd);
        setRegion(cmd.getRegion());
        setDriveHand(cmd.getDriveHand());
        setSalesVersion(cmd.getSalesVersion());
        setUpdateUser(cmd.getUpdateUser());
    }

    private void checkModelAndModelYear(EditBaseVehicleCmd cmd) {
        if (!modelCode.equals(cmd.getModelCode()) || !modelYear.equals(cmd.getModelYear())){
            throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_MODEL_CODE_MODEL_YEAR_INVALID);
        }
    }

    private void changeMaturity(EditBaseVehicleCmd cmd) {
        BaseVehicleMaturityEnum oldMaturity = BaseVehicleMaturityEnum.getByMaturity(maturity);
        BaseVehicleMaturityEnum newMaturity = BaseVehicleMaturityEnum.getByMaturity(cmd.getMaturity());
        if (oldMaturity == null || newMaturity == null) {
            throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_MATURITY_INVALID);
        }
        //maturity不可由P更新为U
        if ( (oldMaturity.getMaturity() == BaseVehicleMaturityEnum.P.getMaturity()) && (newMaturity.getMaturity() == BaseVehicleMaturityEnum.U.getMaturity()) ){
            throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_MATURITY_CHANGE_INVALID);
        }
        setMaturity(cmd.getMaturity());
    }

}
