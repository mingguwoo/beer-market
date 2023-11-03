package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/16
 */
@Data
public class SyncProductConfigModelOptionCmd implements Cmd {

    /**
     * 是否所有车型的最新Release OXO
     */
    private boolean allLatestReleaseOxo;

    /**
     * 指定车型列表的最新Release OXO
     */
    private List<String> latestReleaseOxoModelList;

}
