package com.nio.ngfs.plm.bom.configuration.application.query.basevehicle;

import com.nio.bom.share.enums.StatusEnum;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.basevehicle.assemble.BaseVehicleAssembler;
import com.nio.ngfs.plm.bom.configuration.application.query.basevehicle.service.BaseVehicleQueryService;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ModelFacade;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsBaseVehicleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBaseVehicleEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request.QueryBaseVehicleQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.response.BaseVehicleRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author bill.wang
 * @date 2023/7/24
 */
@Component
@RequiredArgsConstructor
public class QueryBaseVehicleQuery extends AbstractQuery<QueryBaseVehicleQry, List<BaseVehicleRespDto>> {

    private final BomsBaseVehicleDao bomsBaseVehicleDao;
    private final BaseVehicleQueryService baseVehicleQueryService;
    private final ModelFacade modelFacade;


    @Override
    protected void validate(QueryBaseVehicleQry queryBaseVehicleQry) {
        if (queryBaseVehicleQry.getStatus() != null && StatusEnum.getByStatus(queryBaseVehicleQry.getStatus()) == null) {
            throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_STATUS_INVALID);
        }
    }

    @Override
    public List<BaseVehicleRespDto> executeQuery(QueryBaseVehicleQry qry) {
        List<BomsBaseVehicleEntity> entityList = bomsBaseVehicleDao.queryAll();
        List<BaseVehicleRespDto> dtoList = LambdaUtil.map(entityList, BaseVehicleAssembler::assemble);
        List<BaseVehicleRespDto> filteredDto = filter(dtoList, qry);
        //调取featureDomainDao查询region,drive hand, sales version所有选项,再根据featureCode去筛选
        return baseVehicleQueryService.completeBaseVehicle(filteredDto);
    }

    private List<BaseVehicleRespDto> filter(List<BaseVehicleRespDto> dtoList, QueryBaseVehicleQry qry){
        String brandName = ConfigConstants.brandName.get();
        Set<String> modelName = modelFacade.getModelListByBrand(brandName);
        dtoList = dtoList.stream()
                .filter(dto-> StringUtils.isBlank(qry.getModelCode()) || Objects.equals(qry.getModelCode(),dto.getModelCode()))
                .filter(dto-> CollectionUtils.isEmpty(qry.getModelYear()) || qry.getModelYear().contains(dto.getModelYear()))
                .filter(dto-> StringUtils.isBlank(qry.getStatus()) || Objects.equals(qry.getStatus(),dto.getStatus()))
                .filter(dto-> StringUtils.isBlank(qry.getSalesVersion()) || Objects.equals(qry.getSalesVersion(),dto.getSalesVersion()))
                .filter(dto-> StringUtils.isBlank(qry.getRegionOptionCode()) || Objects.equals(qry.getRegionOptionCode(),dto.getRegionOptionCode()))
                .filter(dto-> StringUtils.isBlank(qry.getDriveHand()) || Objects.equals(qry.getDriveHand(),dto.getDriveHand()))
                .filter(dto->StringUtils.isBlank(qry.getMaturity()) || Objects.equals(qry.getMaturity(),dto.getMaturity()))
                .filter(dto->modelName.contains(dto.getModelCode()))
                .toList();
        return dtoList;
    }

}
