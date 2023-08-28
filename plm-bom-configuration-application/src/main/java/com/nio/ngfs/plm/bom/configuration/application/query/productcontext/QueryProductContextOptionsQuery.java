package com.nio.ngfs.plm.bom.configuration.application.query.productcontext;

import com.nio.ngfs.plm.bom.configuration.application.query.productcontext.service.ProductContextQueryService;
import com.nio.ngfs.plm.bom.configuration.application.service.ProductContextApplicationService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.QueryProductContextOptionsQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ProductContextOptionsRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author bill.wang
 * @date 2023/8/25
 */
@Component
@RequiredArgsConstructor
public class QueryProductContextOptionsQuery {

    private final ProductContextQueryService productContextQueryService;

    public ProductContextOptionsRespDto execute(QueryProductContextOptionsQry qry){
        return productContextQueryService.queryProductContextOptions();
    }
}
