package com.nio.ngfs.plm.bom.configuration.application.query.oxo.common;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.nio.bom.share.utils.GZIPUtils;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/21
 */
public class OxoQueryUtil {

    /**
     * 解析oxoSnapShot字符串
     */
    public static OxoListQry resolveSnapShot(String oxoSnapShot) {
        return JSON.parseObject(GZIPUtils.uncompress(oxoSnapShot), OxoListQry.class);
    }

    /**
     * 从OXO Release中获取Base Vehicle的ID列表
     */
    public static List<Long> getBaseVehicleIdListFromOxoRelease(OxoListQry oxoListQry) {
        if (oxoListQry == null || CollectionUtils.isEmpty(oxoListQry.getOxoHeadResps())) {
            return Collections.emptyList();
        }
        List<Long> baseVehicleIdList = Lists.newArrayList();
        oxoListQry.getOxoHeadResps().forEach(head -> {
            head.getRegionInfos().forEach(region -> {
                region.getDriveHands().forEach(driveHand -> {
                    driveHand.getSalesVersionInfos().forEach(salesVersion -> {
                        baseVehicleIdList.add(salesVersion.getHeadId());
                    });
                });
            });
        });
        return baseVehicleIdList;
    }

}
