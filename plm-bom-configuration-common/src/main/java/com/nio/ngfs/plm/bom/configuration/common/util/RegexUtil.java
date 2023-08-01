package com.nio.ngfs.plm.bom.configuration.common.util;

import java.util.regex.Pattern;

/**
 * @author xiaozhou.tu
 * @date 2023/7/11
 */
public class RegexUtil {

    private static final Pattern PATTERN_FEATURE_GROUP_CODE = Pattern.compile("^[\\dA-Za-z\\-]+[\\dA-Za-z\\-\\s]*[\\dA-Za-z\\-]+$");
    private static final Pattern PATTERN_ALPHABET_NUMBER = Pattern.compile("^[\\dA-Za-z]+$");

    /**
     * GroupCode是否匹配字母、数字、中间空格、-
     *
     * @param str 字符串
     * @return true|false
     */
    public static boolean isMatchFeatureGroupCode(String str) {
        return PATTERN_FEATURE_GROUP_CODE.matcher(str).matches();
    }

    /**
     * 是否匹配字母、数字
     *
     * @param str 字符串
     * @return true|false
     */
    public static boolean isMatchAlphabetAndNumber(String str) {
        return PATTERN_ALPHABET_NUMBER.matcher(str).matches();
    }

}
