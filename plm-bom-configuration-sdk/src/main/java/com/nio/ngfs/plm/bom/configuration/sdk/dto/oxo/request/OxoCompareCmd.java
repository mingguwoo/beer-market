package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request;


import lombok.Data;

/**
 * @author wangchao.wang
 */
@Data
public class OxoCompareCmd extends OxoBaseCmd{

    private String baseVersion;

    private String compareVersion;

    /**
     * 只展示不一样的结果
     */
    private boolean showDiff;

}
