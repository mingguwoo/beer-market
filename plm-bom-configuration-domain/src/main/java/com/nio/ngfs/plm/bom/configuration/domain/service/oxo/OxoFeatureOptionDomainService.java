package com.nio.ngfs.plm.bom.configuration.domain.service.oxo;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public interface OxoFeatureOptionDomainService {

    /**
     * 重新排序Feature/Option
     *
     * @param oxoFeatureOptionAggrList OxoFeatureOptionAggr列表
     * @param targetFeatureCode        目标Feature Code
     * @param moveFeatureCodeList      移动的Feature Code列表
     */
    void renewSortFeatureOption(List<OxoFeatureOptionAggr> oxoFeatureOptionAggrList, String targetFeatureCode, List<String> moveFeatureCodeList);

    /**
     * 检查Feature/Option是否可删除
     *
     * @param featureOptionAggrList featureOptionAggr列表
     */
    void checkFeatureOptionDelete(List<OxoFeatureOptionAggr> featureOptionAggrList);

    /**
     * 筛选掉重复/有冲突的无需打的点
     */
    List<OxoOptionPackageAggr> filterRepeatCopyfromPoints(List<OxoOptionPackageAggr> points, List<OxoFeatureOptionAggr> driveHandRegionSalesVersionRows);


    List<String>  checkOxoBasicVehicleOptions(String modelCode);





}
