package com.nio.ngfs.plm.bom.configuration.domain.facade;

import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.response.ModelRespDto;

import java.util.List;
import java.util.Set;

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

    /**
     * 根据车型获取年款
     * @param model
     * @return
     */
    List<String> getModelYearByModel(String model);

    /**
     *  根据品牌获取车型列表
     * @param brand
     * @return
     */
    Set<String> getModelListByBrand(String brand);

    /**
     * 获取所有车型列表
     *
     * @return 车型列表
     */
    List<ModelRespDto> getAllModelList();

}
