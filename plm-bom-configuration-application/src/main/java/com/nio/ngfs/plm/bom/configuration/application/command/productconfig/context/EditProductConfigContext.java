package com.nio.ngfs.plm.bom.configuration.application.command.productconfig.context;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 编辑ProductConfig上下文
 *
 * @author xiaozhou.tu
 * @date 2023/9/11
 */
@Data
@NoArgsConstructor
public class EditProductConfigContext {

    /**
     * 车型
     */
    private String model;

    /**
     * PC列表
     */
    private List<ProductConfigAggr> productConfigAggrList;

    /**
     * Product Config勾选列表
     */
    private List<ProductConfigOptionAggr> productConfigOptionAggrList;

    /**
     * Product Context勾选列表
     */
    private List<ProductContextAggr> productContextAggrList;

    /**
     * PC的错误提示信息列表集合
     */
    private Map<String, Set<String>> messageListMap = Maps.newHashMap();

    private Map<Long, Map<String, List<ProductConfigOptionAggr>>> pcFeatureOptionMap;

    public EditProductConfigContext(String model, List<ProductConfigAggr> productConfigAggrList, List<ProductConfigOptionAggr> productConfigOptionAggrList,
                                    List<ProductContextAggr> productContextAggrList) {
        this.model = model;
        this.productConfigAggrList = productConfigAggrList;
        this.productConfigOptionAggrList = productConfigOptionAggrList;
        this.productContextAggrList = productContextAggrList;
        // Product Config勾选按PC和Feature分组
        this.pcFeatureOptionMap = productConfigOptionAggrList.stream().collect(Collectors.groupingBy(ProductConfigOptionAggr::getPcId,
                Collectors.groupingBy(ProductConfigOptionAggr::getFeatureCode, Collectors.toList())));
    }

    public void addMessage(String message, String featureCode) {
        messageListMap.computeIfAbsent(message, i -> Sets.newHashSet()).add(featureCode);
    }

    public List<String> getMessageList() {
        return messageListMap.entrySet().stream().map(entry ->
                        String.format(entry.getKey(), Joiner.on(",").join(entry.getValue())))
                .sorted(String::compareTo)
                .toList();
    }

}
