package com.nio.ngfs.plm.bom.configuration.application.query.oxo.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.nio.bom.share.utils.GZIPUtils;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/8/21
 */
public class OxoQueryUtil {

    /**
     * 解析oxoSnapShot字符串
     */
    public static OxoListRespDto resolveSnapShot(String oxoSnapShot) {
        return JSONObject.parseObject(Objects.requireNonNull(JSONArray.parse(GZIPUtils.uncompress(oxoSnapShot))).toString(), OxoListRespDto.class);
    }

    /**
     * 从OXO Release中获取Base Vehicle的ID列表
     */
    public static List<Long> getBaseVehicleIdListFromOxoRelease(OxoListRespDto oxoListRespDto) {
        if (oxoListRespDto == null || CollectionUtils.isEmpty(oxoListRespDto.getOxoHeadResps())) {
            return Collections.emptyList();
        }
        List<Long> baseVehicleIdList = Lists.newArrayList();
        oxoListRespDto.getOxoHeadResps().forEach(head -> {
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
