package com.nio.ngfs.plm.bom.configuration.application.query.oxo.common;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.nio.bom.share.utils.GZIPUtils;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;
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
    public static OxoListRespDto resolveSnapShot(String oxoSnapShot) {
        return JSON.parseObject(GZIPUtils.uncompress(oxoSnapShot), OxoListRespDto.class);
    }

    /**
     * 从OXO Release中获取Base Vehicle的ID列表
     */
    public static List<Long> getBaseVehicleIdListFromOxoRelease(OxoListRespDto OxoListRespDto) {
        if (OxoListRespDto == null || CollectionUtils.isEmpty(OxoListRespDto.getOxoHeadResps())) {
            return Collections.emptyList();
        }
        List<Long> baseVehicleIdList = Lists.newArrayList();
        OxoListRespDto.getOxoHeadResps().forEach(head -> {
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
