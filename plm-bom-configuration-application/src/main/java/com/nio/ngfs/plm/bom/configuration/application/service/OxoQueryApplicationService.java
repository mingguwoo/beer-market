package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoCompareQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;

/**
 * @author wangchao.wang
 */
public interface OxoQueryApplicationService {



    /**
     * 根据 车型查询oxo working版本信息
     * @param modelCode
     * @para version
     * @return
     */
    OxoListQry queryOxoInfoByModelCode(String modelCode, String version, Boolean isMaturity);


    /**
     * 对比2个版本
     * @param oxoCompareQry
     * @return
     */
    OxoListQry compareVersion(OxoCompareQry oxoCompareQry);
}
