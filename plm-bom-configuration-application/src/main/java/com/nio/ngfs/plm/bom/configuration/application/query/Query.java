package com.nio.ngfs.plm.bom.configuration.application.query;

import com.nio.ngfs.plm.bom.configuration.application.Application;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;

/**
 * 查询Application
 *
 * @author xiaozhou.tu
 * @date 2023/7/3
 */
public interface Query<Query extends Qry, Response> extends Application<Query, Response> {
}
