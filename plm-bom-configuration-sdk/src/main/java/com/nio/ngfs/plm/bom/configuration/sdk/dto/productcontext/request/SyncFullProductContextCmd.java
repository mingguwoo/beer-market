package com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author bill.wang
 * @date 2023/10/31
 */
@Component
@RequiredArgsConstructor
@Data
public class SyncFullProductContextCmd implements Cmd {

    private String modelCode;
}
