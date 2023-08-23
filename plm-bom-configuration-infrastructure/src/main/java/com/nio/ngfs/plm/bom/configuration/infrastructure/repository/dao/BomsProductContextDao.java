package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextEntity;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/9
 */
public interface BomsProductContextDao extends IService<BomsProductContextEntity> {

    /**
     * 根据modelCode获取Product Context数据
     */

    List<BomsProductContextEntity> queryByModelCode(String modelCode);

    /**
     * 根据modelCode和modelYear列表查询
     *
     * @param modelCode     modelCode
     * @param modelYearList modelYear列表
     * @return BomsProductContextEntity列表
     */
    List<BomsProductContextEntity> queryByModelAndModelYearList(String modelCode, List<String> modelYearList);

}
