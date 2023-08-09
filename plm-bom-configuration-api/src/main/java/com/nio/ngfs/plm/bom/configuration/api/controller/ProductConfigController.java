package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.annotation.NotLogResult;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.query.productconfig.ModelListQuery;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmProductConfigClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.ModelListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.ModelListRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@RestController
@RequiredArgsConstructor
public class ProductConfigController implements PlmProductConfigClient {

    private final ModelListQuery modelListQuery;

    @Override
    @NotLogResult
    public ResultInfo<List<ModelListRespDto>> modelList(@Valid @RequestBody ModelListQry qry) {
        return ResultInfo.success(modelListQuery.execute(qry));
    }

}
