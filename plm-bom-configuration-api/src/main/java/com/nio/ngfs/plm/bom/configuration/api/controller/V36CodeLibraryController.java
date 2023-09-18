package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.annotation.NotLogResult;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.command.v36code.AddV36DigitCommand;
import com.nio.ngfs.plm.bom.configuration.application.command.v36code.AddV36OptionCommand;
import com.nio.ngfs.plm.bom.configuration.application.command.v36code.EditV36DigitCommand;
import com.nio.ngfs.plm.bom.configuration.application.command.v36code.EditV36OptionCommand;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmV36CodeLibraryClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.AddDigitCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.AddOptionCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.EditDigitCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.EditOptionCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.AddDigitRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.AddOptionRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.EditDigitRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.EditOptionRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@RestController
@RequiredArgsConstructor
public class V36CodeLibraryController implements PlmV36CodeLibraryClient {

    private final AddV36DigitCommand addV36DigitCommand;
    private final EditV36DigitCommand editV36DigitCommand;
    private final AddV36OptionCommand addV36OptionCommand;
    private final EditV36OptionCommand editV36OptionCommand;

    @Override
    @NotLogResult
    public ResultInfo<AddDigitRespDto> addDigit(@Valid @RequestBody AddDigitCmd cmd) {
        return ResultInfo.success(addV36DigitCommand.execute(cmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<EditDigitRespDto> editDigit(@Valid @RequestBody EditDigitCmd cmd) {
        return ResultInfo.success(editV36DigitCommand.execute(cmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<AddOptionRespDto> addOption(@Valid @RequestBody AddOptionCmd cmd) {
        return ResultInfo.success(addV36OptionCommand.execute(cmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<EditOptionRespDto> editOption(@Valid @RequestBody EditOptionCmd cmd) {
        return ResultInfo.success(editV36OptionCommand.execute(cmd));
    }

}
