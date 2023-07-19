package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoOptionPackageInfoEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoRowInfoEntity;

import java.util.List;

/**
 * @author wangchao.wang
 */
public interface BomsOxoOptionPackageInfoMapper extends BaseMapper<BomsOxoOptionPackageInfoEntity> {


    void insertOxoOptionPackageInfos(List<BomsOxoOptionPackageInfoEntity> oxoOptionPackageInfos);
}
