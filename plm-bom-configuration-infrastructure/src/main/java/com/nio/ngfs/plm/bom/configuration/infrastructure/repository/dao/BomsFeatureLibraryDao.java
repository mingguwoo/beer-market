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
     * 根据parentFeatureCode列表查找
     *
     * @param parentFeatureCodeList parentFeatureCode列表
     * @param type                  类型
     * @return BomsFeatureLibraryEntity列表
     */
    List<BomsFeatureLibraryEntity> queryByParentFeatureCodeListAndType(List<String> parentFeatureCodeList, String type);

    /**
     * 查找所有
     *
     * @return BomsFeatureLibraryEntity列表
     */
    List<BomsFeatureLibraryEntity> queryAll();

    /**
     * 批量更新状态
     *
     * @param idList     id列表
     * @param status     状态
     * @param updateUser 更新人
     */
    void batchUpdateStatus(List<Long> idList, String status, String updateUser);

    /**
     * 根据FeatureCode查询
     *
     * @param featureCode FeatureCode
     * @return BomsFeatureLibraryEntity列表
     */
    List<BomsFeatureLibraryEntity> queryByFeatureCode(String featureCode);

    /**
     * 根据Display Name、Catalog、Type查询
     *
     * @param displayName Display Name
     * @param catalog     Catalog
     * @param type        Type
     * @return BomsFeatureLibraryEntity列表
     */
    List<BomsFeatureLibraryEntity> queryByDisplayNameCatalogAndType(String displayName, String catalog, String type);

}
