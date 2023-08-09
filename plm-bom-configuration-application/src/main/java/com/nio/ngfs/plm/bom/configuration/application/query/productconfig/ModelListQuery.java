package com.nio.ngfs.plm.bom.configuration.application.query.productconfig;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.productconfig.assemble.ProductConfigAssembler;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsModelYearConfigDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsModelYearConfigEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.ModelListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.ModelListRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@Component
@RequiredArgsConstructor
public class ModelListQuery extends AbstractQuery<ModelListQry, List<ModelListRespDto>> {

    private final BomsModelYearConfigDao modelYearConfigDao;

    @Override
    protected List<ModelListRespDto> executeQuery(ModelListQry modelListQry) {
        List<BomsModelYearConfigEntity> modelYearConfigEntityList = modelYearConfigDao.queryAll();
        return LambdaUtil.groupBy(modelYearConfigEntityList, i -> Objects.equals(i.getOxoRelease(), CommonConstants.YES),
                        BomsModelYearConfigEntity::getModel)
                .entrySet().stream()
                .map(entry -> ProductConfigAssembler.assemble(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(ModelListRespDto::getModel))
                .toList();
    }

}
