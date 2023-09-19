package com.nio.ngfs.plm.bom.configuration.domain.model.v36code;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.common.util.RegexUtil;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.enums.V36CodeLibraryTypeEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.EditDigitCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.EditOptionCmd;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.Optional;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class V36CodeLibraryAggr extends AbstractDo implements AggrRoot<Long> {

    private static final int MAX_DIGIT_NUMBER = 32;

    /**
     * Code
     */
    private String code;

    /**
     * Parent Code
     */
    private String parentCode;

    /**
     * Parent Code Id
     */
    private Long parentId;

    /**
     * 类型，Digit/Option
     */
    private String type;

    /**
     * 英文描述
     */
    private String displayName;

    /**
     * 中文描述
     */
    private String chineseName;

    /**
     * Sales Feature列表，逗号分隔
     */
    private String salesFeatureList;

    /**
     * 备注说明
     */
    private String remark;

    // -------------------- 以下为业务字段 --------------------
    /**
     * 父节点
     */
    private V36CodeLibraryAggr parent;

    @Override
    public Long getUniqId() {
        return id;
    }

    /**
     * 新增Digit
     */
    public void addDigit() {
        setType(V36CodeLibraryTypeEnum.DIGIT.getType());
        setParentCode(ConfigConstants.V36_CODE_DIGIT_PARENT_CODE);
        setParentId(ConfigConstants.V36_CODE_DIGIT_PARENT_CODE_ID);
        checkDigitCode();
    }

    /**
     * 编辑Digit
     */
    public void editDigit(EditDigitCmd cmd) {
        // 校验是否Digit类型
        checkType(V36CodeLibraryTypeEnum.DIGIT);
        // 修改字段
        setDisplayName(cmd.getDisplayName().trim());
        setChineseName(cmd.getChineseName().trim());
        setSalesFeatureList(Joiner.on(",").join(Optional.ofNullable(cmd.getSalesFeatureCodeList())
                .orElse(Lists.newArrayList()).stream().distinct().toList()));
        setRemark(cmd.getRemark());
        setUpdateUser(cmd.getUpdateUser());
    }

    /**
     * 新增Option
     */
    public void addOption() {
        if (!Objects.equals(parent.getType(), V36CodeLibraryTypeEnum.DIGIT.getType())) {
            throw new BusinessException(ConfigErrorCode.V36_CODE_PARENT_NOT_DIGIT);
        }
        setType(V36CodeLibraryTypeEnum.OPTION.getType());
        checkOptionCode();
    }

    /**
     * 编辑Option
     */
    public void editOption(EditOptionCmd cmd) {
        // 校验是否Option类型
        checkType(V36CodeLibraryTypeEnum.OPTION);
        // 修改字段
        setDisplayName(cmd.getDisplayName().trim());
        setChineseName(cmd.getChineseName().trim());
        setRemark(cmd.getRemark());
        setUpdateUser(cmd.getUpdateUser());
    }

    /**
     * 校验类型
     */
    private void checkType(V36CodeLibraryTypeEnum typeEnum) {
        if (!Objects.equals(getType(), typeEnum.getType())) {
            throw new BusinessException(ConfigErrorCode.V36_CODE_TYPE_NOT_MATCH);
        }
    }

    /**
     * Digit Code是否重叠
     */
    public void checkDigitCodeOverlap(V36CodeLibraryAggr otherAggr) {
        // Digit Code相同，跳过
        if (Objects.equals(code, otherAggr.getCode())) {
            return;
        }
        int lowNumber = getDigitLowNumber();
        int highNumber = getDigitHighNumber();
        if (otherAggr.getDigitLowNumber() >= lowNumber && otherAggr.getDigitLowNumber() <= highNumber) {
            throw new BusinessException(ConfigErrorCode.V36_CODE_DIGIT_OVERLAP);
        }
        if (otherAggr.getDigitHighNumber() >= lowNumber && otherAggr.getDigitHighNumber() <= highNumber) {
            throw new BusinessException(ConfigErrorCode.V36_CODE_DIGIT_OVERLAP);
        }
    }

    /**
     * 校验Digit Code
     */
    private void checkDigitCode() {
        checkCode();
        // 校验Digit Code格式
        if (!RegexUtil.isMatchV36DigitCode(code)) {
            throw new BusinessException(ConfigErrorCode.V36_CODE_DIGIT_CODE_FORMAT_INVALID);
        }
        String[] splits = code.split("-");
        String low = splits[0];
        String high = splits[1];
        // 校验Digit数字不能以非0开头
        if (low.startsWith(CommonConstants.STR_ZERO) || high.startsWith(CommonConstants.STR_ZERO)) {
            throw new BusinessException(ConfigErrorCode.V36_CODE_DIGIT_CODE_FORMAT_INVALID);
        }
        int lowNumber = Integer.parseInt(low);
        int highNumber = Integer.parseInt(high);
        // 校验Digit数字最小为1，最大为32，低位数字不大于高位数字
        if (lowNumber > highNumber || lowNumber < CommonConstants.INT_ONE || highNumber > MAX_DIGIT_NUMBER) {
            throw new BusinessException(ConfigErrorCode.V36_CODE_DIGIT_CODE_FORMAT_INVALID);
        }
    }

    /**
     * 校验Option Code
     */
    private void checkOptionCode() {
        checkCode();
        if (!RegexUtil.isMatchAlphabetAndNumber(code)) {
            throw new BusinessException(ConfigErrorCode.V36_CODE_OPTION_CODE_FORMAT_INVALID);
        }
        int optionCodeLength = parent.getDigitHighNumber() - parent.getDigitLowNumber() + 1;
        if (optionCodeLength < 1) {
            throw new BusinessException(ConfigErrorCode.V36_CODE_DIGIT_CODE_FORMAT_INVALID);
        }
        if (code.length() != optionCodeLength) {
            throw new BusinessException(ConfigErrorCode.V36_CODE_OPTION_LENGTH_NOT_MATCH);
        }
    }

    /**
     * 校验Code
     */
    private void checkCode() {
        if (Objects.equals(code, ConfigConstants.V36_CODE_DIGIT_PARENT_CODE)) {
            throw new BusinessException(isDigit() ? ConfigErrorCode.V36_CODE_DIGIT_CODE_FORMAT_INVALID :
                    ConfigErrorCode.V36_CODE_OPTION_CODE_FORMAT_INVALID);
        }
    }

    /**
     * 是否Digit
     */
    public boolean isDigit() {
        return Objects.equals(type, V36CodeLibraryTypeEnum.DIGIT.getType());
    }

    /**
     * 是否Option
     */
    public boolean isOption() {
        return Objects.equals(type, V36CodeLibraryTypeEnum.OPTION.getType());
    }

    /**
     * 获取Digit低位数字
     */
    public int getDigitLowNumber() {
        return Integer.parseInt(code.substring(0, code.indexOf("-")));
    }

    /**
     * 获取Digit高位数字
     */
    public int getDigitHighNumber() {
        return Integer.parseInt(code.substring(code.indexOf("-") + 1));
    }

}
