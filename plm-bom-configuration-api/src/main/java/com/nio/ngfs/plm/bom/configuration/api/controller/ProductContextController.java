package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.annotation.NeedAuthorization;
import com.nio.bom.share.annotation.NotLogResult;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.command.productcontext.SyncFullProductContextCommand;
import com.nio.ngfs.plm.bom.configuration.application.query.productcontext.ExportProductContextQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.productcontext.GetProductContextQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.productcontext.QueryFeatureOptionQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.productcontext.QueryProductContextOptionsQuery;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmProductContextClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.GetProductContextRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ProductContextOptionsRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.QueryFeatureOptionRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.SyncFullProductContextRespDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductContextController implements PlmProductContextClient {

    private final GetProductContextQuery getProductContextQuery;
    private final ExportProductContextQuery exportProductContextQuery;
    private final QueryProductContextOptionsQuery queryProductContextOptionsQuery;
    private final QueryFeatureOptionQuery queryFeatureOptionQuery;
    private final SyncFullProductContextCommand syncFullProductContextCommand;

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<GetProductContextRespDto> getProductContext(@Valid @RequestBody GetProductContextQry qry) {
        return ResultInfo.success(getProductContextQuery.execute(qry));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<ProductContextOptionsRespDto> queryProductContextOptions(@Valid @RequestBody QueryProductContextOptionsQry qry) {
        return ResultInfo.success(queryProductContextOptionsQuery.execute(qry));
    }

    @Override
    @NotLogResult
    public ResultInfo<List<QueryFeatureOptionRespDto>> queryFeatureOption(@Valid @RequestBody QueryFeatureOptionQry qry) {
        return ResultInfo.success(queryFeatureOptionQuery.execute(qry));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<SyncFullProductContextRespDto> syncFullProductContext(SyncFullProductContextCmd cmd) {
        return ResultInfo.success(syncFullProductContextCommand.execute(cmd));
    }

    @NeedAuthorization
    @NotLogResult
    @PostMapping("productContext/exportProductContext")
    public void exportProductContext(@Valid @RequestBody ExportProductContextQry qry, HttpServletResponse response){
        exportProductContextQuery.execute(qry,response);
    }



}
