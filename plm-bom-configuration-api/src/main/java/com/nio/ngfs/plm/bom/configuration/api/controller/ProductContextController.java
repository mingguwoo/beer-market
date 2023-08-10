package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.annotation.NeedAuthorization;
import com.nio.bom.share.annotation.NotLogResult;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.command.productContext.AddProductContextCommand;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmProductContextClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productContext.request.AddProductContextCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productContext.response.AddProductContextRespDto;
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

    private final AddProductContextCommand addProductContextCommand;

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<AddProductContextRespDto> addProductContext(AddProductContextCmd cmd) {
        return ResultInfo.success(addProductContextCommand.execute(cmd));
    }
}
