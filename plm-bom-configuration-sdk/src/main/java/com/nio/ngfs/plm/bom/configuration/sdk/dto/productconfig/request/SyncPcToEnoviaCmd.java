package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/30
 */
@Data
public class SyncPcToEnoviaCmd implements Cmd {

    @NotEmpty(message = "PC Id List is empty")
    private List<String> pcIdList;

}
