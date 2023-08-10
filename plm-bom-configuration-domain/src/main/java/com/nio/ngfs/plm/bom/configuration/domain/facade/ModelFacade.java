package com.nio.ngfs.plm.bom.configuration.domain.facade;

/**
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
public interface ModelFacade {

    /**
     * 根据车型获取品牌
     *
     * @param model 车型
     * @return 品牌
     */
    String getBrandByModel(String model);

}
