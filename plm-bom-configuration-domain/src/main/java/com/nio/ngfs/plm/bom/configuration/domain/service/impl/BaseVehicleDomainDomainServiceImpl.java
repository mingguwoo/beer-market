package com.nio.ngfs.plm.bom.configuration.domain.service.impl;

import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.BaseVehicleDomainService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author bill.wang
 * @date 2023/7/19
 */
@Service
@RequiredArgsConstructor
public class BaseVehicleDomainDomainServiceImpl implements BaseVehicleDomainService {

    private final BaseVehicleRepository baseVehicleRepository;

    @Override
    public void checkBaseVehicleUnique(BaseVehicleAggr baseVehicleAggr) {
        List<BaseVehicleAggr> baseVehicleAggrs = baseVehicleRepository.queryByModelModelYearRegionDriveHandSalesVersion(baseVehicleAggr.getModel(), baseVehicleAggr.getModelYear(),
                baseVehicleAggr.getRegion(), baseVehicleAggr.getDriveHand(), baseVehicleAggr.getSalesVersion());
        if (CollectionUtils.isNotEmpty(baseVehicleAggrs)){
                throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_REPEAT);
        }
    }
}
