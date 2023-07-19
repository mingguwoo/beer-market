package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoOptionPackageInfoEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoRowInfoEntity;

import java.util.List;

public interface BomsOxoOptionPackageInfoDao  extends IService<BomsOxoOptionPackageInfoEntity> {


    void insertOxoOptionPackageInfos(List<BomsOxoOptionPackageInfoEntity> oxoOptionPackageInfos);
}
