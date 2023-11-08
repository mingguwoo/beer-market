package com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule;

import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.AddRuleCmd;

import java.util.Date;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
public interface ConfigurationRuleDomainService {

    /**
     * 获取并校验聚合根
     *
     * @param id id
     * @return 聚合根
     */
    ConfigurationRuleAggr getAndCheckAggr(Long id);

    /**
     * 创建新的Rule
     *
     * @param cmd 命令
     * @return ConfigurationRuleAggr列表
     */
    List<ConfigurationRuleAggr> createNewRule(AddRuleCmd cmd);

    /**
     * 生成RuleNumber
     *
     * @param ruleAggrList 聚合根列表
     */
    void generateRuleNumber(List<ConfigurationRuleAggr> ruleAggrList);

    /**
     * 处理双向Rule
     *
     * @param ruleAggrList 聚合根列表
     * @return 新的聚合根列表
     */
    List<ConfigurationRuleAggr> handleBothWayRule(List<ConfigurationRuleAggr> ruleAggrList);

    /**
     * 查找另一个双向Rule
     *
     * @param ruleAggr 聚合根
     * @return 双向Rule聚合根
     */
    ConfigurationRuleAggr findAnotherBothWayRule(ConfigurationRuleAggr ruleAggr);

    /**
     * 查找另一个双向Rule
     *
     * @param ruleAggr          其中一个双向Rule
     * @param groupRuleAggrList Group下的Rule列表
     * @return 另一个双向Rule
     */
    ConfigurationRuleAggr findAnotherBothWayRule(ConfigurationRuleAggr ruleAggr, List<ConfigurationRuleAggr> groupRuleAggrList);

    /**
     * 批量查找双向Rule
     *
     * @param ruleAggrList 聚合根列表
     * @return 另一个双向Rule的聚合根列表
     */
    List<ConfigurationRuleAggr> batchFindAnotherBothWayRule(List<ConfigurationRuleAggr> ruleAggrList);

    /**
     * 校验Rule Driving下的Constrained打点不重复
     *
     * @param ruleAggrList 聚合根列表
     */
    void checkRuleDrivingConstrainedRepeat(List<ConfigurationRuleAggr> ruleAggrList, List<String> messageList);

    /**
     * 针对每一个Driving列，校验Constrained Feature下只能有一个Option为实心圆或-
     *
     * @param ruleAggrList 聚合根列表
     */
    void checkOptionMatrixByConstrainedFeature(List<ConfigurationRuleAggr> ruleAggrList);

    /**
     * 处理双向Rule Release
     */
    void releaseBothWayRule(List<ConfigurationRuleAggr> ruleAggrList);


    /**
     * Eff-in值校验
     * @param ruleIds
     * @param effIn
     * @param effOut
     * @return
     */
    void  checkNextRevConfigurationRule(List<Long> ruleIds, Date effIn, Date effOut);

    /**
     * 更新生效 失效时间
     * @param ruleIds
     * @param updateInfo
     */
    void updateEffInOrEffOut(List<Long> ruleIds, ConfigurationRuleAggr updateInfo);


    /**
     * remove之前 校验
     * @param ruleAggrList
     */
    void checkConfigurationRuleRemove(List<ConfigurationRuleAggr> ruleAggrList);


    /**
     * 硬校验 rule
     * @param configurationRuleAggrs
     */
    List<Long> checkHardRule(List<ConfigurationRuleAggr> configurationRuleAggrs);


    /**
     * 软校验 rule
     * @param configurationRuleAggrs
     * @return
     */
    List<Long> checkSoftRule(List<ConfigurationRuleAggr> configurationRuleAggrs);
}
