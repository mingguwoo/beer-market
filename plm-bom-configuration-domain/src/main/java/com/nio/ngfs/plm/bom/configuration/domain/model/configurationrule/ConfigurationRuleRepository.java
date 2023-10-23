package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
public interface ConfigurationRuleRepository extends Repository<ConfigurationRuleAggr, Long> {

    /**
     * 批量保存
     *
     * @param aggrList 聚合根列表
     */
    void batchSave(List<ConfigurationRuleAggr> aggrList);

    /**
     * 批量删除
     *
     * @param aggrList 聚合根列表
     */
    void batchRemove(List<ConfigurationRuleAggr> aggrList);

    /**
     * 根据Group Id查询
     *
     * @param groupId Group Id
     * @return 聚合根列表
     */
    List<ConfigurationRuleAggr> queryByGroupId(Long groupId);

    /**
     * 申请Rule Number
     *
     * @param size 申请数量
     * @return Rule Number列表
     */
    List<String> applyRuleNumber(int size);

}
