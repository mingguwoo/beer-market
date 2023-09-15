package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.annotation.NotLogResult;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.command.v36code.AddDigitCommand;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmV36CodeLibraryClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.AddDigitCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.AddDigitRespDto;
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

    private final AddDigitCommand addDigitCommand;

    @Override
    @NotLogResult
    public ResultInfo<AddDigitRespDto> addDigit(@Valid @RequestBody AddDigitCmd cmd) {
        return ResultInfo.success(addDigitCommand.execute(cmd));
    }

}
