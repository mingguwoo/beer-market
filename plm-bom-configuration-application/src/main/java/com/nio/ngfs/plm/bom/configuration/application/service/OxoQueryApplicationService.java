package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoCompareQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;

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
    OxoListRespDto queryOxoInfoByModelCode(String modelCode, String version, Boolean isMaturity);


    /**
     * 对比2个版本
     * @param oxoCompareQry
     * @return
     */
    OxoListRespDto compareVersion(OxoCompareQry oxoCompareQry);
}
