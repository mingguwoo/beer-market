package com.nio.ngfs.plm.bom.configuration.common.util;

import com.google.common.base.Preconditions;
import com.nio.ngfs.plm.bom.configuration.common.enums.ErrorCode;
import com.nio.ngfs.plm.bom.configuration.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public class PreconditionUtil {

    public static void checkMaxLength(String str, Integer maxLength, String errorMessage) {
        checkArgument(str == null || str.length() <= maxLength, errorMessage);
    }

    public static void checkNotBlank(String str, String errorMessage) {
        checkArgument(StringUtils.isNotBlank(str), errorMessage);
    }

    public static void checkArgument(boolean expression, String errorMessage) {
        try {
            Preconditions.checkArgument(expression, errorMessage);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR.getCode(), errorMessage);
        }
    }

    public static <T> void checkNotNull(T reference, String errorMessage) {
        try {
            Preconditions.checkNotNull(reference, errorMessage);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR.getCode(), errorMessage);
        }
    }

}
