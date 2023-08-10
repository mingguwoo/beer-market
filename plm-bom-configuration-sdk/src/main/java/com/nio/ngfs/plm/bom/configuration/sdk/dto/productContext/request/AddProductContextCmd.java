package com.nio.ngfs.plm.bom.configuration.sdk.dto.productContext.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
@Data
public class AddProductContextCmd implements Cmd {

    private String oxoSnapshot;
}
