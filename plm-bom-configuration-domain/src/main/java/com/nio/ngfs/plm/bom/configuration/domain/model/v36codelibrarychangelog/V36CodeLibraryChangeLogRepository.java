package com.nio.ngfs.plm.bom.configuration.domain.model.v36codelibrarychangelog;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/9/20
 */
public interface V36CodeLibraryChangeLogRepository extends Repository<V36CodeLibraryChangeLogAggr, Long> {

    /**
     * 批量保存
     *
     * @param changeLogAggrList 列表
     */
    void batchSave(List<V36CodeLibraryChangeLogAggr> changeLogAggrList);

}
