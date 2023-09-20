package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsV36CodeLibraryChangeLogEntity;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/9/20
 */
public interface BomsV36CodeLibraryChangeLogDao extends IService<BomsV36CodeLibraryChangeLogEntity> {

    /**
     * 根据Id查询
     * @param codeId
     * @return
     */
    List<BomsV36CodeLibraryChangeLogEntity> queryByCodeId(Long codeId);
}
