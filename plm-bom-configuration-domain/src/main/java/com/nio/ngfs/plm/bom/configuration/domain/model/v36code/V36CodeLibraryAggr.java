package com.nio.ngfs.plm.bom.configuration.domain.model.v36code;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.common.util.RegexUtil;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.enums.V36CodeLibraryTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

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
        setParentId(ConfigConstants.V36_CODE_DIGIT_PARENT_CODE_ID);
        checkDigitCode();
    }

    /**
     * 新增Option
     */
    public void addOption() {
        setType(V36CodeLibraryTypeEnum.OPTION.getType());
        checkOptionCode();
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
        // 校验Digit Code格式
        if (!RegexUtil.isMatchV36DigitCode(code)) {
            throw new BusinessException(ConfigErrorCode.V36_CODE_DIGIT_FORMAT_INVALID);
        }
        String[] splits = code.split("-");
        String low = splits[0];
        String high = splits[1];
        // 校验Digit数字不能以非0开头
        if (low.startsWith(CommonConstants.STR_ZERO) || high.startsWith(CommonConstants.STR_ZERO)) {
            throw new BusinessException(ConfigErrorCode.V36_CODE_DIGIT_FORMAT_INVALID);
        }
        int lowNumber = Integer.parseInt(low);
        int highNumber = Integer.parseInt(high);
        // 校验Digit数字最小为1，最大为32
        if (lowNumber > highNumber || lowNumber < CommonConstants.INT_ONE || highNumber > MAX_DIGIT_NUMBER) {
            throw new BusinessException(ConfigErrorCode.V36_CODE_DIGIT_FORMAT_INVALID);
        }
    }

    /**
     * 校验Option Code
     */
    private void checkOptionCode() {
        if (!RegexUtil.isMatchAlphabetAndNumber(code)) {
            throw new BusinessException(ConfigErrorCode.V36_CODE_OPTION_FORMAT_INVALID);
        }
        int optionCodeLength = parent.getDigitHighNumber() - parent.getDigitLowNumber() + 1;
        if (optionCodeLength < 1) {
            throw new BusinessException(ConfigErrorCode.V36_CODE_DIGIT_FORMAT_INVALID);
        }
        if (code.length() != optionCodeLength) {
            throw new BusinessException(ConfigErrorCode.V36_CODE_OPTION_LENGTH_NOT_MATCH);
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
