package com.nio.ngfs.plm.bom.configuration.domain.service.oxo;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;

/**
 * @author wangchao.wang
 */
public interface OxoCompareDomainService {


    /**
     * 对比版本
     * @param baseQry      基础
     * @param compareQry   对比
     * @param showDiff     只展示不一样的结果
     * @return
     */
    OxoListQry compareVersion(OxoListQry baseQry,OxoListQry compareQry,
                              boolean showDiff);







}
