package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/5
 */
public interface BomsFeatureLibraryDao extends IService<BomsFeatureLibraryEntity> {

    /**
     * 根据featureCode和类型查找
     *
     * @param featureCode Feature编码
     * @param type        类型
     * @return BomsFeatureLibraryEntity
     */
    BomsFeatureLibraryEntity getByFeatureCodeAndType(String featureCode, String type);

    /**
     * 根据parentFeatureCode查找
     *
     * @param parentFeatureCode parentFeatureCode
     * @param type              类型
     * @return BomsFeatureLibraryEntity列表
     */
    List<BomsFeatureLibraryEntity> queryByParentFeatureCodeAndType(String parentFeatureCode, String type);

    /**
     * 查找所有
     *
     * @return BomsFeatureLibraryEntity列表
     */
    List<BomsFeatureLibraryEntity> queryAll();

}
