package com.nio.ngfs.plm.bom.configuration.application.query.oxo;

import com.nio.ngfs.plm.bom.configuration.application.query.Query;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoQueryApplicationService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoCompareQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoCompareQuery implements Query<OxoCompareQry, OxoListRespDto> {



    private final OxoQueryApplicationService oxoQueryApplicationService;


    @Override
    public OxoListRespDto execute(OxoCompareQry oxoCompareQry) {
        return oxoQueryApplicationService.compareVersion(oxoCompareQry);

    }
}
