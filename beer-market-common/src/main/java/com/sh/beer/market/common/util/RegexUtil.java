package com.sh.beer.market.common.util;

/**
 * @author
 * @date 2023/7/11
 */
public class RegexUtil {

    /*private static final Pattern PATTERN_FEATURE_GROUP_CODE = Pattern.compile("^[\\dA-Za-z\\-]+[\\dA-Za-z\\-\\s]*[\\dA-Za-z\\-]+$");
    private static final Pattern PATTERN_ALPHABET_NUMBER = Pattern.compile("^[\\dA-Za-z]+$");
    private static final Pattern PATTERN_PC_ID_SUFFIX = Pattern.compile("^\\d{4}$");
    private static final Pattern PATTERN_PC_NAME = Pattern.compile("^[\\dA-Za-z_\\-.]+$");
    private static final Pattern PATTERN_V36_DIGIT_CODE = Pattern.compile("^\\d{1,2}-\\d{1,2}$");

    *//**
     * GroupCode是否匹配字母、数字、中间空格、-
     *
     * @param str 字符串
     * @return true|false
     *//*
    public static boolean isMatchFeatureGroupCode(String str) {
        return PATTERN_FEATURE_GROUP_CODE.matcher(str).matches();
    }

    *//**
     * 是否匹配字母、数字
     *
     * @param str 字符串
     * @return true|false
     *//*
    public static boolean isMatchAlphabetAndNumber(String str) {
        return PATTERN_ALPHABET_NUMBER.matcher(str).matches();
    }

    *//**
     * 是否匹配PC Id后缀
     *
     * @param str 字符串
     * @return true|false
     *//*
    public static boolean isMatchPcIdSuffix(String str) {
        return PATTERN_PC_ID_SUFFIX.matcher(str).matches();
    }

    *//**
     * 是否匹配PC Name
     *
     * @param str 字符串
     * @return true|false
     *//*
    public static boolean isMatchPcName(String str) {
        return PATTERN_PC_NAME.matcher(str).matches();
    }

    *//**
     * 是否匹配V36 Digit Code
     *//*
    public static boolean isMatchV36DigitCode(String str) {
        return PATTERN_V36_DIGIT_CODE.matcher(str).matches();
    }
*/
}
