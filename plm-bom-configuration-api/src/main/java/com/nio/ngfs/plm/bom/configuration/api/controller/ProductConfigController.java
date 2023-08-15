package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.annotation.NotLogResult;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.command.productconfig.AddPcCommand;
import com.nio.ngfs.plm.bom.configuration.application.command.productconfig.EditPcCommand;
import com.nio.ngfs.plm.bom.configuration.application.query.productconfig.GetBasedOnPcListQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.productconfig.GetModelListQuery;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmProductConfigClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.AddPcCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.EditPcCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.GetBasedOnPcListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.GetModelListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.AddPcRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.EditPcRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.GetBasedOnPcListRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.GetModelListRespDto;
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

    private final GetModelListQuery getModelListQuery;
    private final GetBasedOnPcListQuery getBasedOnPcListQuery;
    private final AddPcCommand addPcCommand;
    private final EditPcCommand editPcCommand;

    @Override
    @NotLogResult
    public ResultInfo<List<GetModelListRespDto>> getModelList(@Valid @RequestBody GetModelListQry qry) {
        return ResultInfo.success(getModelListQuery.execute(qry));
    }

    @Override
    @NotLogResult
    public ResultInfo<List<GetBasedOnPcListRespDto>> getBasedOnPcList(@Valid @RequestBody GetBasedOnPcListQry qry) {
        return ResultInfo.success(getBasedOnPcListQuery.execute(qry));
    }

    @Override
    @NotLogResult
    public ResultInfo<AddPcRespDto> addPc(@Valid @RequestBody AddPcCmd cmd) {
        return ResultInfo.success(addPcCommand.execute(cmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<EditPcRespDto> editPc(@Valid @RequestBody EditPcCmd cmd) {
        return ResultInfo.success(editPcCommand.execute(cmd));
    }

}
