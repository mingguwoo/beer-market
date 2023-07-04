package com.nio.ngfs.plm.bom.configuration.application;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;

/**
 * 应用（请求-响应模型）
 *
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public interface Application<Request extends Dto, Response> {

    /**
     * 应用执行
     *
     * @param request 请求
     * @return 响应
     */
    Response execute(Request request);

}
