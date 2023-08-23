package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/28
 */
public interface OxoOptionPackageApplicationService {


    /**
     * 添加 打点信息
     */
    void  insertOxoOptionPackageDefault(List<OxoFeatureOptionAggr> oxoFeatureOptions,
                                        String modelCode,
                                        String userName);


    /**
     *   场景1.OXO中某个Option在Model Year下的所有Base Vehicle中全都为实心圈
     *   --若该Option在Model Year相应的Product Configuration单车中没有全都勾选
     *   --则提示：Option xxx Is Not Consistent With Product Configuration【Model Year:xxx】!
     *   场景2.OXO中某个Option在Model Year下的所有Base Vehicle中全都为"-"
     *   --若该Option在Model Year相应的Product Configuration单车中存在勾选
     */

    List<String> checkOxoCompleteBaseVehicle(String modelCode);
}
