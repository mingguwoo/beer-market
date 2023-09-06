package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodelyear.ProductConfigModelYearAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigModelYearEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@Mapper
public interface ModelYearConfigMapper extends MapstructMapper<ProductConfigModelYearAggr, BomsProductConfigModelYearEntity> {

    ModelYearConfigMapper INSTANCE = Mappers.getMapper(ModelYearConfigMapper.class);

}
