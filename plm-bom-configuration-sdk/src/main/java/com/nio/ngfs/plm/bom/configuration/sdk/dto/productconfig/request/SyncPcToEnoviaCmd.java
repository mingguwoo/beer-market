package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/10/30
 */
@Data
public class SyncPcToEnoviaCmd implements Cmd {

    private String pcId;

}
