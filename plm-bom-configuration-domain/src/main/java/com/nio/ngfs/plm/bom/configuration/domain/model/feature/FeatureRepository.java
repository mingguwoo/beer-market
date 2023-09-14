package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public interface FeatureRepository extends Repository<FeatureAggr, FeatureId> {

    /**
     * 根据id查找聚合根
     *
     * @param id id
     * @return 聚合根
     */
    FeatureAggr getById(Long id);

    /**
     * 根据parentFeatureCode和类型查找
     *
     * @param parentFeatureCode parentFeatureCode
     * @param type              类型
     * @return FeatureAggr列表
     */
    List<FeatureAggr> queryByParentFeatureCodeAndType(String parentFeatureCode, String type);

    /**
     * 根据parentFeatureCode列表和类型查找
     *
     * @param parentFeatureCodeList parentFeatureCode列表
     * @param type                  类型
     * @return FeatureAggr列表
     */
    List<FeatureAggr> queryByParentFeatureCodeListAndType(List<String> parentFeatureCodeList, String type);

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
     * @return FeatureAggr列表
     */
    List<FeatureAggr> queryByFeatureCode(String featureCode);

    /**
     * 根据Display Name、Catalog、Type查询
     *
     * @param displayName Display Name
     * @param catalog     Catalog
     * @param type        Type
     * @return FeatureAggr列表
     */
    List<FeatureAggr> queryByDisplayNameCatalogAndType(String displayName, String catalog, String type);

    /**
     * 根据Feature/Option Code列表批量聚合根
     *
     * @param featureOptionCodeList Feature/Option Code列表
     * @return FeatureAggr列表
     */
    List<FeatureAggr> queryByFeatureOptionCodeList(List<String> featureOptionCodeList);


    /**
     * 根据Feature/Option Code列表批量聚合根
     *
     * @param featureOptionCodeList Feature/Option Code列表
     * @return FeatureAggr列表
     */
    List<FeatureAggr> queryByFeatureOptionCodeListNoActive(List<String> featureOptionCodeList);

    /**
     * 根据Feature或Option Code查询
     *
     * @param featureOptionCode Feature/Option Code
     * @return FeatureAggr
     */
    FeatureAggr getByFeatureOrOptionCode(String featureOptionCode);


    /**
     * 根据 featureCodes not in  Feature库中
     * @param featureCodes
     * @return
     */
    List<FeatureAggr> findFeatureLibraryNotFeatureCodes(List<String> featureCodes);

}
