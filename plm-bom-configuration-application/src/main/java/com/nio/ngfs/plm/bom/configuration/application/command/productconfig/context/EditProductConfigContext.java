package com.nio.ngfs.plm.bom.configuration.application.command.productconfig.context;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 编辑ProductConfig上下文
 *
 * @author xiaozhou.tu
 * @date 2023/9/11
 */
@Data
public class EditProductConfigContext {

    private static final ThreadLocal<EditProductConfigContext> CONTEXT = ThreadLocal.withInitial(EditProductConfigContext::new);

    /**
     * PC的错误提示信息列表集合
     */
    private final Map<String, List<String>> messageListMap = Maps.newHashMap();

    public static void addMessage(String pcId, String message) {
        CONTEXT.get().getMessageListMap().computeIfAbsent(pcId, i -> Lists.newArrayList()).add(message);
    }

    public static List<String> getMessageList() {
        return CONTEXT.get().getMessageListMap().values().stream().flatMap(Collection::stream).toList();
    }

    /**
     * 清除
     */
    public static void remove() {
        CONTEXT.remove();
    }

}
