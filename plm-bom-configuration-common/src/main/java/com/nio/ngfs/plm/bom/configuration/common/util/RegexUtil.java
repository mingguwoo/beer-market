package com.nio.ngfs.plm.bom.configuration.common.util;

import java.util.regex.Pattern;

/**
 * @author xiaozhou.tu
 * @date 2023/7/11
 */
public class RegexUtil {

    private static final Pattern PATTERN_ALPHABET_NUMBER_BLANK = Pattern.compile("^[\\dA-Za-z]+[\\dA-Za-z\\s]*[\\dA-Za-z]+$");

    /**
     * 是否匹配字母、数字和空格（首位不包含空格）
     *
     * @return true|false
     */
    public static boolean isMatchAlphabetNumberAndBlank(String str) {
        return PATTERN_ALPHABET_NUMBER_BLANK.matcher(str).matches();
    }

}
