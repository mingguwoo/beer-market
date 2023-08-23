package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigOptionEntity;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
public interface BomsProductConfigOptionDao extends IService<BomsProductConfigOptionEntity> {

    /**
     * 根据PC Id和Option Code查询
     *
     * @param pcId       PC Id
     * @param optionCode Option Code
     * @return BomsProductConfigOptionEntity
     */
    BomsProductConfigOptionEntity getByPcIdAndOptionCode(String pcId, String optionCode);

    /**
     * 根据PC Id查询
     *
     * @param pcId PC Id
     * @return BomsProductConfigOptionEntity列表
     */
    List<BomsProductConfigOptionEntity> queryByPcId(String pcId);

    /**
     * 根据PC Id列表查询
     *
     * @param pcIdList PC Id列表
     * @return BomsProductConfigOptionEntity列表
     */
    List<BomsProductConfigOptionEntity> queryByPcIdList(List<String> pcIdList);

}
