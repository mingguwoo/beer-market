package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;

/**
 * @author wangchao.wang
 */
public interface OxoCompareApplicationService {


    /**
     * 对比版本
     * @param baseQry      基础
     * @param compareQry   对比
     * @param showDiff     只展示不一样的结果
     * @return
     */
    OxoListRespDto compareVersion(OxoListRespDto baseQry,OxoListRespDto compareQry,
                              boolean showDiff);


    /**
     * 发送对比邮件
     * @param compareOxoListQry
     */
    void sendCompareEmail(OxoListRespDto compareOxoListQry, OxoVersionSnapshotAggr oxoVersionSnapshot);
}
