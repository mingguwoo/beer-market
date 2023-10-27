package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.Lists;
import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.bom.share.enums.YesOrNoEnum;
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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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

    /**
     * 是否不可见的Rule（针对双向Rule），0-否，1-是
     */
    private Integer invisible;

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
        setInvisible(YesOrNoEnum.NO.getCode());
        // 校验option打点不为空
        if (CollectionUtils.isEmpty(optionList)) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_RULE_OPTION_LIST_IS_EMPTY);
        }
        if (isBothWayRule()) {
            // 校验双向Rule打点一对一
            if (optionList.size() > 1) {
                throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_BOTH_WAY_RULE_SELECT_ONE_CONSTRAINED);
            }
            setRulePairId(IdWorker.getId());
        }
        optionList.forEach(ConfigurationRuleOptionDo::add);
    }

    /**
     * 编辑打点
     */
    public boolean updateOption(List<ConfigurationRuleOptionDo> ruleOptionList) {
        AtomicBoolean changed = new AtomicBoolean(false);
        // 先处理更新打点
        Map<String, ConfigurationRuleOptionDo> newConstrainedOptionMap = LambdaUtil.toKeyMap(ruleOptionList, ConfigurationRuleOptionDo::getConstrainedOptionCode);
        optionList.forEach(option -> {
            ConfigurationRuleOptionDo newOption = newConstrainedOptionMap.get(option.getConstrainedOptionCode());
            if (newOption != null) {
                if (Objects.equals(option.getMatrixValue(), newOption.getMatrixValue())) {
                    // 打点未变更
                    return;
                }
                // 打点变更
                option.update(newOption.getMatrixValue(), newOption.getUpdateUser());
                changed.set(true);
            } else {
                // 删除打点
                option.delete();
                changed.set(true);
            }
        });
        // 新增打点
        Set<String> oldConstrainedOptionCodeSet = optionList.stream().map(ConfigurationRuleOptionDo::getConstrainedOptionCode).collect(Collectors.toSet());
        ruleOptionList.stream().filter(i -> !oldConstrainedOptionCodeSet.contains(i.getConstrainedOptionCode())).forEach(option -> {
            option.setRule(this);
            option.add();
            optionList.add(option);
            changed.set(true);
        });
        return changed.get();
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
     * configuration rule 升版
     */
    public ConfigurationRuleAggr revise(String reviser) {
        ConfigurationRuleAggr newAggr = new ConfigurationRuleAggr();
        BeanUtils.copyProperties(this, newAggr);
        newAggr.setId(null);
        newAggr.setRuleVersion(productReviseVersion(getRuleVersion()));
        newAggr.setChangeType(ConfigurationRuleChangeTypeEnum.MODIFY.getChangeType());
        newAggr.setReleaseDate(null);
        newAggr.setEffIn(null);
        newAggr.setEffOut(null);
        newAggr.setCreateUser(reviser);
        newAggr.setUpdateUser(reviser);
        newAggr.setStatus(ConfigurationRuleStatusEnum.IN_WORK.getStatus());
        newAggr.setOptionList(getOptionList().stream().peek(aggr -> {
            aggr.setRuleId(null);
            aggr.setId(null);
        }).toList());
        return newAggr;
    }

    /**
     * 重置rulePairId
     */
    public void resetRulePairId() {
        setRulePairId(IdWorker.getId());
    }

    public void resetRulePairId(Long pairId) {
        setRulePairId(pairId);
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
        // 不可见的Rule
        copyRuleAggr.setInvisible(YesOrNoEnum.YES.getCode());
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
     * 是否In Work状态
     */
    public boolean isStatusInWork() {
        return isStatus(ConfigurationRuleStatusEnum.IN_WORK);
    }

    /**
     * 是否Released状态
     */
    public boolean isStatusReleased() {
        return isStatus(ConfigurationRuleStatusEnum.RELEASED);
    }

    /**
     * ChangeType是否为Remove
     */
    public boolean isChangeTypeRemove() {
        return Objects.equals(changeType, ConfigurationRuleChangeTypeEnum.REMOVE.getChangeType());
    }

    /**
     * 是否可见的Rule
     */
    public boolean isVisible() {
        return !isInvisible();
    }

    /**
     * 是否不可见的Rule
     */
    public boolean isInvisible() {
        return Objects.equals(invisible, YesOrNoEnum.YES.getCode());
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

    /**
     * 生成升版号
     */
    private String productReviseVersion(String version) {
        char lastChar = 'Z';
        StringBuilder newVersion = new StringBuilder(version);
        for (int i = version.length() - 1; i >= 0; i--) {
            if (version.charAt(i) < lastChar) {
                char newChar = (char) (version.charAt(i) + 1);
                newVersion.replace(i, i + 1, String.valueOf(newChar));
                return newVersion.toString();
            } else {
                newVersion.replace(i, i + 1, "A");
            }
        }
        throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_VERSION_OVERFLOW);
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
                ", invisible=" + invisible +
                ", createUser='" + createUser + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
