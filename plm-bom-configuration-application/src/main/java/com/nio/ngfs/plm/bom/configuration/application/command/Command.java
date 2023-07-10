package com.nio.ngfs.plm.bom.configuration.application.command;

import com.nio.ngfs.plm.bom.configuration.application.Application;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;

/**
 * 命令Application
 *
 * @author xiaozhou.tu
 * @date 2023/7/3
 */
public interface Command<C extends Cmd, Response> extends Application<C, Response> {
}
