package com.nio.ngfs.plm.bom.configuration.common.util;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xiaozhou.tu
 * @date 2023/7/3
 */
public class ConverterUtil {

    public static <R, T> List<T> convertList(List<R> list, Function<R, T> function) {
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        return list.stream().map(function).collect(Collectors.toList());
    }

}
