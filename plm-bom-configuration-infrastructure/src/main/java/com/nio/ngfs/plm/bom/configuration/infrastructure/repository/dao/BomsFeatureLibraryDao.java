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
     * 根据FeatureCodes查询
     * @param featureCodes
     * @return
     */
    List<BomsFeatureLibraryEntity> queryByFeatureCodes(List<String> featureCodes);


    /**
     * 根据Display Name、Catalog、Type查询
     *
     * @param displayName Display Name
     * @param catalog     Catalog
     * @param type        Type
     * @return BomsFeatureLibraryEntity列表
     */
    List<BomsFeatureLibraryEntity> queryByDisplayNameCatalogAndType(String displayName, String catalog, String type);

    /**
     * 获取Group列表
     *
     * @return BomsFeatureLibraryEntity列表
     */
    List<BomsFeatureLibraryEntity> getGroupList();


    /**
     *
     * @param featureCodes
     * @return
     */
    List<BomsFeatureLibraryEntity> findFeatureLibraryNotFeatureCodes(List<String> featureCodes);


    /**
     * 根据Feature/Option Code列表批量查询
     *
     * @param featureOptionCodeList Feature/Option Code列表
     * @return BomsFeatureLibraryEntity列表
     */
    List<BomsFeatureLibraryEntity> queryByFeatureOptionCodeList(List<String> featureOptionCodeList);



    /**
     * 根据Feature/Option Code列表批量查询，Active状态
     *
     * @param featureOptionCodeList Feature/Option Code列表
     * @return BomsFeatureLibraryEntity列表
     */
    List<BomsFeatureLibraryEntity> queryByFeatureOptionCodeListActive(List<String> featureOptionCodeList);

    /**
     * 根据Feature或Option Code查询
     *
     * @param featureOptionCode Feature/Option Code
     * @return BomsFeatureLibraryEntity
     */
    BomsFeatureLibraryEntity getByFeatureOrOptionCode(String featureOptionCode);

    /**
     * 根据类型查找
     * @param catalog
     * @return
     */
    List<BomsFeatureLibraryEntity> queryFeatureByCatalog(String catalog);

}
