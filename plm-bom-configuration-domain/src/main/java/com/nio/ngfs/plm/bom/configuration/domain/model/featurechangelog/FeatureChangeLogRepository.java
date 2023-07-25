package com.nio.ngfs.plm.bom.configuration.domain.model.featurechangelog;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/25
 */
public interface FeatureChangeLogRepository extends Repository<FeatureChangeLogAggr, Long> {

    /**
     * 批量保存
     *
     * @param changeLogAggrList 列表
     */
    void batchSave(List<FeatureChangeLogAggr> changeLogAggrList);

}
