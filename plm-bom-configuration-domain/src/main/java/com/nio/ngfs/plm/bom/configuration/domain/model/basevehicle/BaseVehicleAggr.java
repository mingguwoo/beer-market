package com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle;

import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.bom.share.enums.StatusEnum;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.enums.BaseVehicleMaturityEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.AddBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.ChangeBaseVehicleStatusCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.EditBaseVehicleCmd;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    private String regionOptionCode;

    private String driveHand;

    private String salesVersion;

    private String maturity;

    private String status;

    @Override
    public String getUniqId() {
        return baseVehicleId;
    }

    /**
     * 新增Base Vehicle
     */
    public void addBaseVehicle(AddBaseVehicleCmd cmd){
        //校验maturity并赋值
        checkAndSetMaturity(cmd.getMaturity());
        //赋值Status
        setStatus(StatusEnum.ACTIVE.getStatus());
    }

    /**
     * 编辑Base Vehicle
     */
    public void editBaseVehicle(EditBaseVehicleCmd cmd){
        //校验model，model year是否被改了
        checkModelCodeAndModelYear(cmd);
        //赋值
        setRegionOptionCode(cmd.getRegionOptionCode());
        setDriveHand(cmd.getDriveHand());
        setSalesVersion(cmd.getSalesVersion());
        setUpdateUser(cmd.getUpdateUser());
        changeMaturity(cmd.getMaturity());
    }

    /**
     * 校验BaseVehicle要修改的Status是否符合规范
     */
    public void changeBaseVehicleStatus(ChangeBaseVehicleStatusCmd cmd){
        checkStatus(cmd);
        setStatus(cmd.getStatus());
    }

    /**
     * 校验Model Code和Model Year是否更改
     */
    private void checkModelCodeAndModelYear(EditBaseVehicleCmd cmd) {
        if (!modelCode.equals(cmd.getModelCode()) || !modelYear.equals(cmd.getModelYear())){
            throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_MODEL_CODE_MODEL_YEAR_INVALID);
        }
    }


    /**
     * 校验Maturity修改是否符合规范并修改
     */
    public void changeMaturity(String maturity) {
        BaseVehicleMaturityEnum oldMaturity = BaseVehicleMaturityEnum.getByMaturity(maturity);
        BaseVehicleMaturityEnum newMaturity = BaseVehicleMaturityEnum.getByMaturity(maturity);
        if (oldMaturity == null || newMaturity == null) {
            throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_MATURITY_INVALID);
        }
        //maturity不可由P更新为U
        if ( (oldMaturity.getMaturity() == BaseVehicleMaturityEnum.P.getMaturity()) && (newMaturity.getMaturity() == BaseVehicleMaturityEnum.U.getMaturity()) ){
            throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_MATURITY_CHANGE_INVALID);
        }
        setMaturity(maturity);
    }

    /**
     * 校验BaseVehicle的Maturity是否符合规范并赋值
     */
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

    /**
     * 校验BaseVehicle要修改的Status是否符合规范
     */
    private void checkStatus(ChangeBaseVehicleStatusCmd cmd){
        if (Objects.isNull(StatusEnum.getByStatus(cmd.getStatus()))){
            throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_STATUS_INVALID);
        }
    }

    /**
     * 将BaseVehicle的regionOptionCode，salesVersion，driveHand组成一个codeList并返回
     */
    public List<String> buildCodeList(){
        return Arrays.asList(getRegionOptionCode(),getDriveHand(),getSalesVersion());
    }
}
