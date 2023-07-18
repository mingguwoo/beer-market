package com.nio.ngfs.plm.bom.configuration.application.query.feature;

import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.Query;
import com.nio.ngfs.plm.bom.configuration.application.query.feature.assemble.FeatureDtoAssembler;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.ListFeatureLibraryQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.FeatureLibraryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Component
@RequiredArgsConstructor
public class ListFeatureLibraryQuery implements Query<ListFeatureLibraryQry, List<FeatureLibraryDto>> {

    private final FeatureRepository featureRepository;

    @Override
    public List<FeatureLibraryDto> execute(ListFeatureLibraryQry qry) {
        // 1、可以直接调Repository查询
        // 2、也可以调DomainService
        // 3、也可以抽一层QueryService（通用化，多个请求都需要用到）
        List<FeatureAggr> featureAggrList = featureRepository.queryAll();
        return LambdaUtil.map(featureAggrList, FeatureDtoAssembler::assemble);
    }

}
