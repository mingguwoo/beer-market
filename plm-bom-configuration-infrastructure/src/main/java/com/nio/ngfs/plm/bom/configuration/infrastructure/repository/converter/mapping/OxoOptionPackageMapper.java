package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoOptionPackageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Mapper
public interface OxoOptionPackageMapper extends MapstructMapper<OxoOptionPackageAggr, BomsOxoOptionPackageEntity> {

    OxoOptionPackageMapper INSTANCE = Mappers.getMapper(OxoOptionPackageMapper.class);

}
