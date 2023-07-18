package com.nio.ngfs.plm.bom.configuration.application.query.feature;

import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.Query;
import com.nio.ngfs.plm.bom.configuration.application.query.feature.assemble.FeatureChangeLogAssembler;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureChangeLogDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureChangeLogEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.GetChangeLogListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.GetChangeLogListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/18
 */
@Component
@RequiredArgsConstructor
public class GetChangeLogListQuery implements Query<GetChangeLogListQry, List<GetChangeLogListDto>> {

    private final BomsFeatureChangeLogDao bomsFeatureChangeLogDao;

    @Override
    public List<GetChangeLogListDto> execute(GetChangeLogListQry qry) {
        List<BomsFeatureChangeLogEntity> changeLogEntityList = bomsFeatureChangeLogDao.queryByFeatureId(qry.getFeatureId());
        return LambdaUtil.map(changeLogEntityList, FeatureChangeLogAssembler::assemble);
    }

}
