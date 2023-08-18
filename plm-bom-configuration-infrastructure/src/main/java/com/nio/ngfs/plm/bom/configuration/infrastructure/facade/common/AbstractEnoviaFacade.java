package com.nio.ngfs.plm.bom.configuration.infrastructure.facade.common;

import com.nio.bom.share.utils.FeignInvokeUtils;
import com.nio.bom.share.utils.GsonUtils;
import com.nio.ngfs.plm.bom.configuration.remote.dto.common.PlmEnoviaResult;
import lombok.extern.slf4j.Slf4j;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author xiaozhou.tu
 * @date 2023/8/18
 */
@Slf4j
public abstract class AbstractEnoviaFacade {

    /**
     * 调用3DE Enovia接口
     */
    protected <Req, Resp> void invokeEnovia(Function<Req, PlmEnoviaResult<Resp>> invokeFunction, Req request, String invokeName,
                                            BiConsumer<PlmEnoviaResult<Resp>, Exception> errorCallback) {
        try {
            log.info("{} request={}", invokeName, GsonUtils.toJson(request));
            PlmEnoviaResult<Resp> response = FeignInvokeUtils.invokeWithRetry(invokeFunction, request, invokeName, PlmEnoviaResult::isSuccess);
            log.info("{} response={}", invokeName, GsonUtils.toJson(response));
            if (response.isSuccess()) {
                return;
            }
            log.error("{} fail msg={}", invokeName, response.getMsg());
            errorCallback.accept(response, null);
        } catch (Exception e) {
            log.error(String.format("%s error", invokeName), e);
            errorCallback.accept(null, e);
        }
    }

}
