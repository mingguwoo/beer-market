package com.nio.ngfs.plm.bom.configuration.remote.dto.enovia;

import lombok.Data;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/30
 */
@Data
public class SyncProductContextModelFeatureDto {

    private List<String> modelCodeList;

    private String featureCode;

    private String featureSeq;

    private String may;


}
