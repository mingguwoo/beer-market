package com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.kafka;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/9/15
 */
@Data
public class SyncProductContextModelFeatureKafkaCmd implements Cmd {

    private List<String> modelCodeList;

    private String featureCode;

    private String featureSeq;

    private String may;
}
