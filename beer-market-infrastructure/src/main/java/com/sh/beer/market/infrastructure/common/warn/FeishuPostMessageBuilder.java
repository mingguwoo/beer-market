package com.sh.beer.market.infrastructure.common.warn;

import java.util.Collections;
import java.util.List;

/**
 * @author
 * @date 2023/8/2
 */
public class FeishuPostMessageBuilder {

    /**
     * 构建飞书富文本消息
     *
     * @param title       标题
     * @param contentList 内容列表
     * @param atList      @人列表
     * @return 消息内容
     *//*
    public static String buildPostMessage(String title, List<String> contentList, List<String> atList) {
        List<Object> contentMessageList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(contentList)) {
            contentList.forEach(content -> contentMessageList.add(ImmutableMap.of(
                    "tag", "text",
                    "text", content
            )));
        }
        if (CollectionUtils.isNotEmpty(atList)) {
            atList.forEach(at -> contentMessageList.add(ImmutableMap.of(
                    "tag", "at",
                    "user_id", at
            )));
        }
        return GsonUtils.toJson(ImmutableMap.of(
                "msg_type", "post",
                "content", ImmutableMap.of(
                        "post", ImmutableMap.of(
                                "zh_cn", ImmutableMap.of(
                                        "title", title,
                                        "content", Collections.singletonList(contentMessageList)
                                )
                        )
                )
        ));
    }*/

}
