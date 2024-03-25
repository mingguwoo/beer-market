package com.sh.beer.market.application.query.basevehicle;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023/7/27
 */
@Component
@RequiredArgsConstructor
public class QueryCopyFromModelQuery {

    /*private final BomsBaseVehicleDao bomsBaseVehicleDao;
    private final BaseVehicleQueryService baseVehicleQueryService;


    public List<BaseVehicleRespDto> execute (QueryCopyFromModelsQry qry){
        List<BomsBaseVehicleEntity> entityList =  bomsBaseVehicleDao.queryCopyFromModel(qry.getModelCode());
        //转换为Dto
        List<BaseVehicleRespDto> dtoList = entityList.stream().map(entity->{
            BaseVehicleRespDto dto = new BaseVehicleRespDto();
            BeanUtils.copyProperties(entity,dto);
            return dto;
        }).collect(Collectors.toList());
        //补全中英文信息并返回结果
        return baseVehicleQueryService.completeBaseVehicle(dtoList);
    }*/
}
