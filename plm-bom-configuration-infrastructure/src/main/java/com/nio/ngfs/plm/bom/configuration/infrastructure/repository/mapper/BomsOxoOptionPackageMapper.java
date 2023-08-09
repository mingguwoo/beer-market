package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoOptionPackageEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * oxo 打点信息表 Mapper 接口
 * </p>
 *
 * @author xiaozhou.tu
 * @since 2023-07-27
 */
public interface BomsOxoOptionPackageMapper extends BaseMapper<BomsOxoOptionPackageEntity> {




    void insertOxoOptionPackages(@Param("oxoOptionPackages") List<BomsOxoOptionPackageEntity> entity);

}
