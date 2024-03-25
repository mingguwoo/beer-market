package com.sh.beer.market.application.command;

import com.sh.beer.market.application.Application;
import com.sh.beer.market.sdk.dto.common.Cmd;

/**
 * 命令Application
 *
 * @author
 * @date 2023/7/3
 */
public interface Command<C extends Cmd, Response> extends Application<C, Response> {
}
