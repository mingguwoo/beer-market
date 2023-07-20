package com.nio.ngfs.plm.bom.configuration.domain.service.impl;

import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.BaseVehicleDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author bill.wang
 * @date 2023/7/19
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BaseVehicleDomainDomainServiceImpl implements BaseVehicleDomainService {

    private final BaseVehicleRepository baseVehicleRepository;

    @Override
    public void checkBaseVehicleUnique(BaseVehicleAggr baseVehicleAggr) {
        List<BaseVehicleAggr> existedBaseVehicleAggrList = baseVehicleRepository.queryByModelModelYearRegionDriveHandSalesVersion(baseVehicleAggr.getModelCode(), baseVehicleAggr.getModelYear(),
                baseVehicleAggr.getRegion(), baseVehicleAggr.getDriveHand(), baseVehicleAggr.getSalesVersion());
        if (CollectionUtils.isNotEmpty(existedBaseVehicleAggrList)){
            //如果是edit且是自身记录，不报错，直接跳过
            if (checkEditMaturity(existedBaseVehicleAggrList,baseVehicleAggr)){
                return;
            }
            throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_REPEAT);
        }
    }

    @Override
    public BaseVehicleAggr getBaseVehicleByBaseVehicleId(String baseVehicleId) {
        BaseVehicleAggr baseVehicleAggr = baseVehicleRepository.queryBaseVehicleByBaseVehicleId(baseVehicleId);
        if (Objects.isNull(baseVehicleAggr)){
            throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_NOT_EXISTS);
        }
        return baseVehicleAggr;
    }

    private boolean checkEditMaturity(List<BaseVehicleAggr> existedBaseVehicleAggrList, BaseVehicleAggr baseVehicleAggr){
        //判断是不是edit
        if (Objects.nonNull(baseVehicleAggr.getBaseVehicleId())){
            existedBaseVehicleAggrList.forEach(existedBaseVehicleAggr-> {
                //如果是自身记录，跳过
                if (Objects.equals(baseVehicleAggr.getBaseVehicleId(),existedBaseVehicleAggr.getBaseVehicleId())){
                    return;
                }
                throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_REPEAT);
            });
        }
        return true;
    }
}
