package com.sh.beer.market.application.query;


import com.sh.beer.market.application.Application;
import com.sh.beer.market.sdk.dto.common.Qry;

/**
 * 查询Application
 *
 * @author
 * @date 2023/7/3
 */
public interface Query<Q extends Qry, Response> extends Application<Q, Response> {
}
