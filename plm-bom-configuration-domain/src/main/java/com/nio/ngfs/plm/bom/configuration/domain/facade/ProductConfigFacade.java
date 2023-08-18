package com.nio.ngfs.plm.bom.configuration.domain.facade;

import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncAddPcDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncDeletePcDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncUpdatePcDto;

/**
 * @author xiaozhou.tu
 * @date 2023/8/18
 */
public interface ProductConfigFacade {

    /**
     * 新增PC同步到3DE
     *
     * @param dto dto
     */
    void syncAddPcToEnovia(SyncAddPcDto dto);

    /**
     * 更新PC同步到3DE
     *
     * @param dto dto
     */
    void syncUpdatePcToEnovia(SyncUpdatePcDto dto);

    /**
     * 删除PC同步到3DE
     *
     * @param dto dto
     */
    void syncDeletePcToEnovia(SyncDeletePcDto dto);

}
