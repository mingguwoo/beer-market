package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.OxoPackageInfoAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoOptionPackageEntity;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public interface BomsOxoOptionPackageDao extends IService<BomsOxoOptionPackageEntity> {



    List<BomsOxoOptionPackageEntity> queryOxoOptionPackageByRowIds(List<Long> rowIds);



    void insertOxoOptionPackages(List<OxoPackageInfoAggr> oxoPackages);
}
