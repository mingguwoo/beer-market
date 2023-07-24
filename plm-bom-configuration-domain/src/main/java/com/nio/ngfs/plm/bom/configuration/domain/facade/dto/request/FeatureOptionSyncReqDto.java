package com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request;

import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/7/24
 */
@Data
public class FeatureOptionSyncReqDto {

    private String updateUser;

    private FeatureSyncDto feature;

    public enum FeatureSyncType {

        /**
         * 同步类型
         */
        ADD,
        UPDATE,
        NO_CHANGE

    }

}
