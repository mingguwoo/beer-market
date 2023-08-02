package com.nio.ngfs.plm.bom.configuration.common.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author angles lose thire hair
 * 查找sv上一个版本
 */
public class VersionUtils {

    private static final String VERSION_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int MAX_LENGTH = 2;

    /**
     * 查找上一个版本
     */
    public static String findPrevRev(String currRev) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(currRev)) {
            List<String> versionList = Arrays.asList(VERSION_LIST.split(""));
            List<String> curRevList = Arrays.asList(currRev.split(""));
            Integer firstLo = versionList.indexOf(curRevList.get(0));
            int secondLo = versionList.indexOf(curRevList.get(1));
            if (firstLo.equals(secondLo)) {
                secondLo--;
                if (secondLo < 0) {
                    return sb.toString();
                }
            } else {
                secondLo--;
                if (secondLo < 0) {
                    secondLo = secondLo + 26;
                    firstLo = firstLo - 1;
                }
            }
            sb.append(versionList.get(firstLo));
            sb.append(versionList.get(secondLo));
        }
        return sb.toString();
    }

    /**
     * 查询下一个版本
     *
     * @param currRev currRev
     * @return String
     */
    public static String findNextRev(String currRev) {
        if (StringUtils.isEmpty(currRev) || currRev.length() < MAX_LENGTH) {
            throw new IllegalStateException("currRev is null");
        }
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(currRev)) {
            List<String> versionList = Lists.newArrayList(VERSION_LIST.split(""));
            List<String> curRevList = Lists.newArrayList(currRev.split(""));
            Integer firstLo = versionList.indexOf(curRevList.get(0));
            int secondLo = versionList.indexOf(curRevList.get(1));
            if (firstLo.equals(secondLo)) {
                secondLo++;
                if (secondLo >= VERSION_LIST.length()) {
                    return sb.toString();
                }
            } else {
                secondLo++;
                if (secondLo >= VERSION_LIST.length()) {
                    secondLo = secondLo - VERSION_LIST.length();
                    firstLo = firstLo + 1;
                }
            }
            sb.append(versionList.get(firstLo));
            sb.append(versionList.get(secondLo));
        }
        return sb.toString();
    }


}
