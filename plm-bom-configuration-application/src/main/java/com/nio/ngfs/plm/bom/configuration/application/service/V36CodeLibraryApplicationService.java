package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryAggr;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
public interface V36CodeLibraryApplicationService {

    /**
     * 校验Sales Feature List是否有效
     *
     * @param aggr 聚合根
     */
    void checkSalesFeatureList(V36CodeLibraryAggr aggr);

}
