package com.nio.ngfs.plm.bom.configuration.application.query.oxo;

import com.nio.ngfs.plm.bom.configuration.application.query.Query;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoFeatureOptionApplicationService;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoQueryApplicationService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoInfoQuery implements Query<OxoBaseCmd, OxoListQry> {



    private final OxoQueryApplicationService oxoQueryApplicationService;


    @Override
    public OxoListQry execute(OxoBaseCmd oxoListQry) {
        return oxoQueryApplicationService.queryOxoInfoByModelCode(
                oxoListQry.getModelCode(),oxoListQry.getVersion(),false);
    }
}
