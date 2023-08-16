package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.annotation.NeedAuthorization;
import com.nio.bom.share.annotation.NotLogResult;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.query.productcontext.GetProductContextQuery;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmProductContextClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.GetProductContextQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.GetProductContextRespDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductContextController implements PlmProductContextClient {

    private final GetProductContextQuery getProductContextQuery;

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<GetProductContextRespDto> getProductContext(GetProductContextQry qry) {
        return ResultInfo.success(getProductContextQuery.execute(qry));
    }
}
