package com.nio.ngfs.plm.bom.configuration.application;

/**
 * 应用（请求-响应模型）
 *
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public interface Application<Request, Response> {

    /**
     * 处理业务动作
     *
     * @param request 请求
     * @return 响应
     */
    Response doAction(Request request);

}
