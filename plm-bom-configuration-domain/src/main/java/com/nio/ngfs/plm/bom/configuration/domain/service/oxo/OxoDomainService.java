package com.nio.ngfs.plm.bom.configuration.domain.service.oxo;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.OxoPackageInfoAggr;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoAddCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import java.util.List;

/**
 * @author wangchao.wang
 */
public interface OxoDomainService {




    void insertOxoOptionPackageInfos(List<OxoPackageInfoAggr> packageInfoAggrs);




    /**
     * 添加oxo 下拉code列表
     * @return
     */
    OxoAddCmd queryFeatureList(OxoBaseCmd cmd);


    /**
     *  查询邮件group
     * @return
     */
    List<String> queryEmailGroup();
}
