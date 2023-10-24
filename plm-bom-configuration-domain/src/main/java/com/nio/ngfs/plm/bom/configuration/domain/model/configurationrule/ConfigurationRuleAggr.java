package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.Lists;
import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRuleChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRulePurposeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRuleStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Configuration Rule
 *
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationRuleAggr extends AbstractDo implements AggrRoot<Long> {

    /**
     * Rule Number
     */
    private String ruleNumber;

    /**
     * Rule的版本
     */
    private String ruleVersion;

    /**
     * Group Id
     */
    private Long groupId;

    /**
     * 创建Rule的目的，1-（Sales —> Eng），2-（Sales —> Sales），3-（Sales <—> Sales），4-（Sales X Sales），5-（Base Vehicle —> Sales）
     */
    private Integer purpose;

    /**
     * Rule的类型，Inclusive、Exclusive、Default、Available
     */
    private String ruleType;

    /**
     * Rule的变更类型，Add、Modify、Remove
     */
    private String changeType;

    /**
     * Rule的状态，In Work、Released
     */
    private String status;

    /**
     * Rule的制造生效时间
     */
    private Date effIn;

    /**
     * Rule的制造失效时间
     */
    private Date effOut;

    /**
     * Rule的发布时间
     */
    private Date releaseDate;

    /**
     * 双向Rule对的id
     */
    private Long rulePairId;

    private transient List<ConfigurationRuleOptionDo> optionList = Lists.newArrayList();

    private transient boolean bothWayPairMatch = false;

    @Override
    public Long getUniqId() {
        return id;
    }

    /**
     * 新增Rule
     */
    public void add() {
        checkPurpose();
        // 字段赋值
        setRuleVersion(ConfigConstants.VERSION_AA);
        setRuleType(getRulePurposeEnum().getRuleType().getRuleType());
        setChangeType(ConfigurationRuleChangeTypeEnum.ADD.getChangeType());
        setStatus(ConfigurationRuleStatusEnum.IN_WORK.getStatus());
        if (isBothWayRule()) {
            setRulePairId(IdWorker.getId());
        }
        optionList.forEach(ConfigurationRuleOptionDo::add);
    }

    /**
     * 删除Rule
     */
    public void delete() {
        ConfigurationRulePurposeEnum purposeEnum = ConfigurationRulePurposeEnum.getAndCheckByCode(purpose);
        if (!purposeEnum.isCanDeleteRule()) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_RULE_CAN_NOT_DELETE);
        }
        if (!isStatus(ConfigurationRuleStatusEnum.IN_WORK)) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_RULE_CAN_NOT_DELETE);
        }
    }

    /**
     * 发布
     */
    public boolean release() {
        if (isStatus(ConfigurationRuleStatusEnum.IN_WORK)) {
            setStatus(ConfigurationRuleStatusEnum.RELEASED.getStatus());
            return true;
        }
        return false;
    }

    /**
     * 是否可以Release
     */
    public boolean canRelease() {
        return isStatus(ConfigurationRuleStatusEnum.IN_WORK);
    }

    /**
     * 复制双向Rule
     */
    public ConfigurationRuleAggr copyBothWayRule() {
        // 校验打点一对一
        if (optionList.size() > 1) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_BOTH_WAY_RULE_SELECT_ONE_CONSTRAINED);
        }
        ConfigurationRuleAggr copyRuleAggr = new ConfigurationRuleAggr();
        BeanUtils.copyProperties(this, copyRuleAggr);
        copyRuleAggr.setOptionList(LambdaUtil.map(optionList, ConfigurationRuleOptionDo::copyBothWayRuleOption));
        return copyRuleAggr;
    }

    /**
     * 校验Purpose
     */
    private void checkPurpose() {
        ConfigurationRulePurposeEnum.getAndCheckByCode(purpose);
    }

    /**
     * 获取Purpose枚举
     */
    public ConfigurationRulePurposeEnum getRulePurposeEnum() {
        return ConfigurationRulePurposeEnum.getByCode(purpose);
    }

    /**
     * 是否指定状态
     */
    public boolean isStatus(ConfigurationRuleStatusEnum statusEnum) {
        return Objects.equals(status, statusEnum.getStatus());
    }

    /**
     * 是否双向Rule
     */
    public boolean isBothWayRule(ConfigurationRuleAggr another) {
        return !Objects.equals(this.getId(), another.getId()) &&
                Objects.equals(this.getGroupId(), another.getGroupId()) &&
                Objects.equals(this.getRulePairId(), another.getRulePairId());
    }

    /**
     * 是否双向Rule
     */
    public boolean isBothWayRule() {
        return getRulePurposeEnum().isBothWay();
    }

    @Override
    public String toString() {
        return "ConfigurationRuleAggr{" +
                "id=" + id +
                ", ruleNumber='" + ruleNumber + '\'' +
                ", ruleVersion='" + ruleVersion + '\'' +
                ", groupId=" + groupId +
                ", purpose=" + purpose +
                ", ruleType='" + ruleType + '\'' +
                ", changeType='" + changeType + '\'' +
                ", status='" + status + '\'' +
                ", effIn=" + effIn +
                ", effOut=" + effOut +
                ", releaseDate=" + releaseDate +
                ", rulePairId=" + rulePairId +
                ", createUser='" + createUser + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
