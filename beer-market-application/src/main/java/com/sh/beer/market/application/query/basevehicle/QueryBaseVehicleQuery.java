package com.sh.beer.market.application.query.basevehicle;


import com.sh.beer.market.application.query.AbstractQuery;
import com.sh.beer.market.sdk.dto.configurationrule.request.GetGroupAndRuleQry;
import com.sh.beer.market.sdk.dto.configurationrule.response.GetGroupAndRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author
 * @date 2023/7/24
 */
@Component
@RequiredArgsConstructor
public class QueryBaseVehicleQuery extends AbstractQuery<GetGroupAndRuleQry, List<GetGroupAndRuleRespDto>> {

    /*private final BomsBaseVehicleDao bomsBaseVehicleDao;
    private final BaseVehicleQueryService baseVehicleQueryService;
    private final ModelFacade modelFacade;*/


    @Override
    protected void validate(GetGroupAndRuleQry queryBaseVehicleQry) {
        /*if (queryBaseVehicleQry.getStatus() != null && StatusEnum.getByStatus(queryBaseVehicleQry.getStatus()) == null) {
            throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_STATUS_INVALID);
        }*/
    }

    @Override
    public List<GetGroupAndRuleRespDto> executeQuery(GetGroupAndRuleQry qry) {
        /*List<BomsBaseVehicleEntity> entityList = bomsBaseVehicleDao.queryAll();
        List<BaseVehicleRespDto> dtoList = LambdaUtil.map(entityList, BaseVehicleAssembler::assemble);
        List<BaseVehicleRespDto> filteredDto = filter(dtoList, qry);
        //调取featureDomainDao查询region,drive hand, sales version所有选项,再根据featureCode去筛选
        return baseVehicleQueryService.completeBaseVehicle(filteredDto);*/
        return null;
    }

    /*private List<BaseVehicleRespDto> filter(List<BaseVehicleRespDto> dtoList, QueryBaseVehicleQry qry){
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
    }*/

}
