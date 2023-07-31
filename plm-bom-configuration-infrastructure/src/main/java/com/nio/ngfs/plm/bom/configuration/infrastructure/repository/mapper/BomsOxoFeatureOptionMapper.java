package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoFeatureOptionEntity;

import java.util.List;

/**
 * <p>
 * oxo Feature/Option行信息表 Mapper 接口
 * </p>
 *
 * @author xiaozhou.tu
 * @since 2023-07-27
 */
public interface BomsOxoFeatureOptionMapper extends BaseMapper<BomsOxoFeatureOptionEntity> {



    void insertOxoRows(List<BomsOxoFeatureOptionEntity> bomsOxoRowInfoEntitys);

}
