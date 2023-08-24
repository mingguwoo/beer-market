package com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot;

import com.alibaba.fastjson.JSONObject;
import com.nio.bom.share.utils.GZIPUtils;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoSnapshotCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

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
        oxoFeatureOptionAggr.setEmailGroup(CollectionUtils.isNotEmpty(editGroupCmd.getEmailUsers()) ?
                String.join(",",editGroupCmd.getEmailUsers()) : StringUtils.EMPTY);
        oxoFeatureOptionAggr.setCreateUser(editGroupCmd.getUserName());
        oxoFeatureOptionAggr.setUpdateUser(editGroupCmd.getUserName());

        return oxoFeatureOptionAggr;
    }
}
