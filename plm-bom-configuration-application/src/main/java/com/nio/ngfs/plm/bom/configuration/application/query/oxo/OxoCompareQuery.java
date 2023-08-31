package com.nio.ngfs.plm.bom.configuration.application.query.oxo;

import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.application.query.Query;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoCompareApplicationService;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoQueryApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoCompareQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoCompareQuery implements Query<OxoCompareQry, OxoListQry> {



    private final OxoQueryApplicationService oxoQueryApplicationService;


    @Override
    public OxoListQry execute(OxoCompareQry oxoCompareQry) {
        return oxoQueryApplicationService.compareVersion(oxoCompareQry);

    }
}
