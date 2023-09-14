package com.nio.ngfs.plm.bom.configuration.application.command.productconfig.context;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
    private Map<String, List<String>> messageListMap = Maps.newHashMap();

    private Map<Long, Map<String, List<ProductConfigOptionAggr>>> pcFeatureOptionMap;

    public EditProductConfigContext(List<ProductConfigAggr> productConfigAggrList, List<ProductConfigOptionAggr> productConfigOptionAggrList, List<ProductContextAggr> productContextAggrList) {
        this.productConfigAggrList = productConfigAggrList;
        this.productConfigOptionAggrList = productConfigOptionAggrList;
        this.productContextAggrList = productContextAggrList;
        // Product Config勾选按PC和Feature分组
        this.pcFeatureOptionMap = productConfigOptionAggrList.stream().collect(Collectors.groupingBy(ProductConfigOptionAggr::getPcId,
                Collectors.groupingBy(ProductConfigOptionAggr::getFeatureCode, Collectors.toList())));
    }

    public void addMessage(String pcId, String message) {
        messageListMap.computeIfAbsent(pcId, i -> Lists.newArrayList()).add(message);
    }

    public List<String> getMessageList() {
        return messageListMap.values().stream().flatMap(Collection::stream).toList();
    }

}