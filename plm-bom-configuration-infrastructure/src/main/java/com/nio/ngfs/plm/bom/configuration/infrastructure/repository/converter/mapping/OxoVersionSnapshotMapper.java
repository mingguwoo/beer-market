package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoVersionSnapshotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Mapper
public interface OxoVersionSnapshotMapper extends MapstructMapper<OxoVersionSnapshotAggr, BomsOxoVersionSnapshotEntity> {

    OxoVersionSnapshotMapper INSTANCE = Mappers.getMapper(OxoVersionSnapshotMapper.class);

}
