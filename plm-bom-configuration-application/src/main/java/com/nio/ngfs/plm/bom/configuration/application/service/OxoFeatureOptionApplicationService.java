package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Set;

/**
 * @author xiaozhou.tu
 * @date 2023/7/28
 */
public interface OxoFeatureOptionApplicationService {

    /**
     * 查询同一排序分组的Feature/Option
     *
     * @param model             车型
     * @param featureOptionCode Feature/Option Code
     * @return OxoFeatureOptionAggr列表
     */
    List<OxoFeatureOptionAggr> querySameSortGroupFeatureOption(String model, String featureOptionCode);

    /**
     * 在最新Release版本OXO中查询存在的Feature/Option
     *
     * @param modelCode 车型
     * @param featureOptionAggrList OxoFeatureOptionAggr列表
     * @return Feature/Option Code集合
     */
    Set<String> queryExistFeatureOptionInLastedReleaseSnapshot(String modelCode, List<OxoFeatureOptionAggr> featureOptionAggrList);

    /**
     * 构建Feature/Option行的子节点列表
     *
     * @param featureOptionAggrList OxoFeatureOptionAggr列表
     */
    void buildFeatureOptionWithChildren(List<OxoFeatureOptionAggr> featureOptionAggrList);

    /**
     * 校验并删除打点
     *
     * @param featureOptionAggrList OxoFeatureOptionAggr列表
     * @param modelCode 车型
     * @return OxoOptionPackageAggr列表
     */
    Pair<List<OxoOptionPackageAggr>, List<String>> checkAndDeleteOptionPackage(String modelCode, List<OxoFeatureOptionAggr> featureOptionAggrList);




    /**
     * 查询 未选中  featureCode
     * @param modelCode
     * @return
     */
    List<FeatureAggr> queryFeaturesByModel(String modelCode);


    /**
     *  根据车型 查询规则
     * @param modelCode
     * @return
     */
    List<String> checkRules(String modelCode);


    /**
     * 更新软删除
     * @param modelCode
     */
    void updateSoftDelete(String modelCode);


    /**
     *   场景1.OXO中某个Option在Model Year下的所有Base Vehicle中全都为实心圈
     *   --若该Option在Model Year相应的Product Configuration单车中没有全都勾选
     *   --则提示：Option xxx Is Not Consistent With Product Configuration【Model Year:xxx】!
     *   场景2.OXO中某个Option在Model Year下的所有Base Vehicle中全都为"-"
     *   --若该Option在Model Year相应的Product Configuration单车中存在勾选
     * @param modelCode
     * @return
     */
    List<String>  checkOxoFeatureCode(String modelCode);
}
