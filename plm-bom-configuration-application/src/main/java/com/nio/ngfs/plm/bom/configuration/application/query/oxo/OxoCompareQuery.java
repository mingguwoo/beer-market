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


    private final OxoCompareApplicationService oxoCompareDomainService;


    private final OxoQueryApplicationService oxoQueryApplicationService;


    @Override
    public OxoListQry execute(OxoCompareQry oxoCompareQry) {


        String modelCode = oxoCompareQry.getModelCode();

        String baseVersion = oxoCompareQry.getBaseVersion();

        String compareVersion = oxoCompareQry.getCompareVersion();

        if (StringUtils.containsIgnoreCase(baseVersion, compareVersion)) {
            throw new BusinessException(ConfigErrorCode.BASIC_VERSION_ERROR);
        }


        //查询oxo  info数据
        OxoListQry baseVersionQry = oxoQueryApplicationService.queryOxoInfoByModelCode(modelCode, baseVersion, false);

        if(CollectionUtils.isEmpty(baseVersionQry.getOxoHeadResps()) ||CollectionUtils.isEmpty(baseVersionQry.getOxoHeadResps())){
            throw new BusinessException(MessageFormat.format(ConfigErrorCode.VERSION_ERROR.getMessage(),baseVersion));
        }

        OxoListQry compareVersionQry = oxoQueryApplicationService.queryOxoInfoByModelCode(modelCode, compareVersion, false);

        if(CollectionUtils.isEmpty(compareVersionQry.getOxoHeadResps()) ||CollectionUtils.isEmpty(compareVersionQry.getOxoHeadResps())){
            throw new BusinessException(MessageFormat.format(ConfigErrorCode.VERSION_ERROR.getMessage(),compareVersion));
        }

        return oxoCompareDomainService.compareVersion(baseVersionQry, compareVersionQry, oxoCompareQry.isShowDiff());

    }
}
