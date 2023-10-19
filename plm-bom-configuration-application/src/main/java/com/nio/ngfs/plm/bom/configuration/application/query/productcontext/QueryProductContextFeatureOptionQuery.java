package com.nio.ngfs.plm.bom.configuration.application.query.productcontext;

import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.QueryProductContextFeatureOptionQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.QueryProductContextFeatureOptionRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/19
 */
@Component
@RequiredArgsConstructor
public class QueryProductContextFeatureOptionQuery extends AbstractQuery<QueryProductContextFeatureOptionQry, List<QueryProductContextFeatureOptionRespDto>> {

    @Override
    protected List<QueryProductContextFeatureOptionRespDto> executeQuery(QueryProductContextFeatureOptionQry qry) {
        return null;
    }

}
