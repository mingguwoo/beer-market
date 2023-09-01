package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.annotation.NeedAuthorization;
import com.nio.bom.share.annotation.NotLogResult;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.query.productcontext.ExportProductContextQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.productcontext.GetProductContextQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.productcontext.QueryProductContextOptionsQuery;
import com.nio.ngfs.plm.bom.configuration.application.task.productContext.ImportProductContextTask;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmProductContextClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.ExportProductContextQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.GetProductContextQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.QueryProductContextOptionsQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.GetProductContextRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ImportProductContextRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ProductContextOptionsRespDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
    private final ImportProductContextTask importProductContextTask;

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

    @NeedAuthorization
    @NotLogResult
    @PostMapping("productContext/exportProductContext")
    public void exportProductContext(@Valid @RequestBody ExportProductContextQry qry, HttpServletResponse response){
        exportProductContextQuery.execute(qry,response);
    }

    @NotLogResult
    @PostMapping("/productContext/importProductContext")
    public ResultInfo<ImportProductContextRespDto> importProductContext(@RequestPart("file") MultipartFile file) {
        return ResultInfo.success(importProductContextTask.execute(file));
    }

}
