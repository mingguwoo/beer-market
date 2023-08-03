package com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot;

import com.alibaba.fastjson.JSONObject;
import com.nio.bom.share.utils.GZIPUtils;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoSnapshotCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public class OxoVersionSnapshotFactory {



    public static OxoVersionSnapshotAggr buildOxoFeatureOptions(OxoListQry oxoListQry, String version, OxoSnapshotCmd editGroupCmd){


        OxoVersionSnapshotAggr oxoFeatureOptionAggr=new OxoVersionSnapshotAggr();
        oxoFeatureOptionAggr.setModelCode(editGroupCmd.getModelCode());
        oxoFeatureOptionAggr.setVersion(version);
        oxoFeatureOptionAggr.setOxoSnapshot(GZIPUtils.compress(JSONObject.toJSONString(oxoListQry)));

        oxoFeatureOptionAggr.setType(editGroupCmd.getType());
        oxoFeatureOptionAggr.setBrand(ConfigConstants.brandName.get());
        oxoFeatureOptionAggr.setTitle(editGroupCmd.getTitle());
        oxoFeatureOptionAggr.setChangeContent(editGroupCmd.getChangeContent());
        oxoFeatureOptionAggr.setEmailGroup(String.join(",",editGroupCmd.getEmailUsers()));
        oxoFeatureOptionAggr.setCreateUser(editGroupCmd.getUserName());
        oxoFeatureOptionAggr.setUpdateUser(editGroupCmd.getUserName());

        return oxoFeatureOptionAggr;
    }
}
