package com.nio.ngfs.plm.bom.configuration.application.query.productconfig;

import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.productconfig.assemble.ProductConfigAssembler;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.GetBasedOnPcListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.GetBasedOnPcListRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 查询Based On PC列表
 *
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
@Component
@RequiredArgsConstructor
public class GetBasedOnPcListQuery extends AbstractQuery<GetBasedOnPcListQry, List<GetBasedOnPcListRespDto>> {

    private final BomsProductConfigDao productConfigDao;

    @Override
    protected List<GetBasedOnPcListRespDto> executeQuery(GetBasedOnPcListQry qry) {
        List<BomsProductConfigEntity> productConfigEntityList = productConfigDao.queryByModel(qry.getModel());
        return LambdaUtil.map(productConfigEntityList,
                i -> StringUtils.isBlank(qry.getSearch()) || i.getName().contains(qry.getSearch()),
                ProductConfigAssembler::assemble);
    }

}
