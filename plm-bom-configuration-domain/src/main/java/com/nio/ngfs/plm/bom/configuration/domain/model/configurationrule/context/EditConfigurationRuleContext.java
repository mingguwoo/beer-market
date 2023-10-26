package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.context;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 编辑Rule上下文
 *
 * @author xiaozhou.tu
 * @date 2023/10/25
 */
@Data
@NoArgsConstructor
public class EditConfigurationRuleContext {

    /**
     * Rule Group
     */
    private ConfigurationRuleGroupAggr ruleGroup;

    /**
     * 编辑的Rule列表
     */
    private List<EditConfigurationRule> editRuleList = Lists.newArrayList();

    /**
     * 待新增的Rule列表
     */
    private List<ConfigurationRuleAggr> addRuleList = Lists.newArrayList();

    /**
     * 待更新的Rule列表
     */
    private List<ConfigurationRuleAggr> updateRuleList = Lists.newArrayList();

    /**
     * 待删除的Rule列表
     */
    private List<ConfigurationRuleAggr> deleteRuleList = Lists.newArrayList();

    /**
     * 错误信息列表
     */
    private List<String> errorMessageList = Lists.newArrayList();

    public void addErrorMessage(String errorMessage) {
        errorMessageList.add(errorMessage);
    }

    public List<ConfigurationRuleAggr> getAddOrUpdateRuleList() {
        List<ConfigurationRuleAggr> addOrUpdateRuleAggrList = Lists.newArrayList();
        addOrUpdateRuleAggrList.addAll(addRuleList);
        addOrUpdateRuleAggrList.addAll(updateRuleList);
        return addOrUpdateRuleAggrList;
    }

    @Data
    public static class EditConfigurationRule {

        /**
         * Rule的Driving Option Code
         */
        private String drivingOptionCode;

        /**
         * Released状态的Rule列表，按版本正排
         */
        private List<ConfigurationRuleAggr> releasedRuleList;

        /**
         * 当前In Work状态的Rule
         */
        private ConfigurationRuleAggr inWorkRule;

        /**
         * 打点（前端传入的最新的打点列表）
         */
        private List<ConfigurationRuleOptionDo> ruleOptionList;

        /**
         * 打点是否为空或全部-
         */
        private boolean isOptionEmptyOrAllUnavailable;

        public ConfigurationRuleAggr getLatestReleasedRule() {
            return releasedRuleList.get(releasedRuleList.size() - 1);
        }

    }

}
