package com.nio.ngfs.plm.bom.configuration.application.query.productconfig;

import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.productconfig.assemble.QueryProductConfigAssembler;
import com.nio.ngfs.plm.bom.configuration.common.util.ModelYearComparator;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.GetPcOptionListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.GetPcOptionListRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * 查询PC选项列表
 *
 * @author xiaozhou.tu
 * @date 2023/8/23
 */
@Component
@RequiredArgsConstructor
public class GetPcOptionListQuery extends AbstractQuery<GetPcOptionListQry, List<GetPcOptionListRespDto>> {

    private final BomsProductConfigDao productConfigDao;

    @Override
    protected List<GetPcOptionListRespDto> executeQuery(GetPcOptionListQry qry) {
        List<BomsProductConfigEntity> productConfigEntityList = productConfigDao.queryByModelAndModelYearList(qry.getModel(), qry.getModelYearList());
        return productConfigEntityList.stream()
                // 按Model Year、创建时间（倒排）排序
                .sorted(Comparator.comparing(BomsProductConfigEntity::getModelYear, ModelYearComparator.INSTANCE)
                        .thenComparing(Comparator.comparing(BomsProductConfigEntity::getCreateTime).reversed()))
                // 组装结果
                .map(QueryProductConfigAssembler::assembleOptionList)
                .toList();
    }

}
