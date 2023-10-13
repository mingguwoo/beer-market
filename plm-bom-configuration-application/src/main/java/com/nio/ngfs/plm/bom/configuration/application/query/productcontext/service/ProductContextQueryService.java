package com.nio.ngfs.plm.bom.configuration.application.query.productcontext.service;

import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextFeatureEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.GetProductContextQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.GetProductContextRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ProductContextDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ProductContextOptionsRespDto;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/11
 */
public interface ProductContextQueryService {

    /**
     * 筛选得到符合条件的productContext并组装结果并返回
     */
    GetProductContextRespDto filterAndBuildResponse(List<ProductContextDto> pointList, List<BomsProductContextFeatureEntity> rowList, GetProductContextQry qry);

    /**
     * 获取model code和group
     * @return
     */
    ProductContextOptionsRespDto queryProductContextOptions();

    }
