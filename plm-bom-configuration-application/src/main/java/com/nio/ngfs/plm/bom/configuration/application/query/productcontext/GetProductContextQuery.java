package com.nio.ngfs.plm.bom.configuration.application.query.productcontext;

import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.productcontext.assemble.ProductContextAssembler;
import com.nio.ngfs.plm.bom.configuration.application.query.productcontext.service.ProductContextQueryService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductContextDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductContextFeatureDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextFeatureEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.GetProductContextQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.GetProductContextRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ProductContextDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/11
 */
@Component
@RequiredArgsConstructor
public class GetProductContextQuery {

    private final BomsProductContextDao bomsProductContextDao;
    private final BomsProductContextFeatureDao bomsProductContextFeatureDao;
    private final ProductContextQueryService productContextQueryService;

    public GetProductContextRespDto execute(GetProductContextQry qry){
        List<BomsProductContextEntity> bomsProductContextEntities = bomsProductContextDao.queryByModelCode(qry.getModelCode());
        //获取全部点
        List<BomsProductContextFeatureEntity> bomsProductContextFeatureEntities = bomsProductContextFeatureDao.queryByModelCode(qry.getModelCode());
        List<ProductContextDto> pointList = LambdaUtil.map(bomsProductContextEntities, ProductContextAssembler::assemble);
        return productContextQueryService.filterAndBuildResponse(pointList, bomsProductContextFeatureEntities,qry);
    }

}
