package com.nio.ngfs.plm.bom.configuration.common.util;

import java.util.Comparator;

/**
 * Model Year比较
 *
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
public class ModelYearComparator implements Comparator<String> {

    private static final String G1_F = "G1.F";
    private static final String G1_4 = "G1.4";
    private static final String MY_2018 = "MY2018";
    private static final String G1_0 = "G1.0";

    public static final ModelYearComparator INSTANCE = new ModelYearComparator();

    @Override
    public int compare(String modelYear1, String modelYear2) {
        return wrapModelYear(modelYear1).compareTo(wrapModelYear(modelYear2));
    }

    private String wrapModelYear(String modelYear) {
        if (G1_F.equals(modelYear)) {
            return G1_4;
        }
        if (MY_2018.equals(modelYear)){
            return G1_0;
        }
        return modelYear;
    }

}
