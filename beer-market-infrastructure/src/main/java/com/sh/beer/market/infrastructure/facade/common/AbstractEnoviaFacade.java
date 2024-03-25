package com.sh.beer.market.infrastructure.facade.common;


import lombok.extern.slf4j.Slf4j;

/**
 * @author
 * @date 2023/8/18
 */
@Slf4j
public abstract class AbstractEnoviaFacade {

    /**
     * 调用3DE Enovia接口
     */
    /*protected <Req, Resp> void invokeEnovia(Function<Req, PlmEnoviaResult<Resp>> invokeFunction, Req request, String invokeName,
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
    }*/

}
