package com.nio.ngfs.plm.bom.configuration.infrastructure.config.warn;

import com.alibaba.fastjson.JSON;
import com.nio.bom.share.configuration.WarnLogConfig;
import com.nio.ngfs.common.utils.SpringContextHolder;
import com.nio.ngfs.plm.bom.configuration.remote.FeishuIntegrationClient;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 日志告警UTIl
 *
 * @author guangjin.gao
 */
@Slf4j
public class WarnLogUtils {
    /**
     * 发送飞书告警
     *
     * @param url     url
     * @param message message
     * @param extMap  extMap
     */
    public static void send(String url, String message, Map<String, String> extMap) {
        if (StringUtils.isEmpty(url)) {
            log.warn("WarnMsgUtil send msg fail,because url is empty");
            return;
        }
        try {
            SpringContextHolder.getBean(FeishuIntegrationClient.class).sendMessageToLogGroup(JSON.toJSONString(generateRequestBean(message, extMap)));
        } catch (Exception e) {
            log.warn("send warn msg error,dingUrl:{}", url, e);
        }
    }

    /**
     * 告警内容
     *
     * @param message message
     * @param extMap  extMap
     * @return DingTextMessage
     */
    private static DingTextMessage generateRequestBean(String message, Map<String, String> extMap) {
        DingTextMessage dingTextMessage = new DingTextMessage();
        WarnLogConfig warnLogConfig = SpringContextHolder.getApplicationContext().getBean(WarnLogConfig.class);
        dingTextMessage.setMsg_type("text");
        DingTextMessage.Content content = new DingTextMessage.Content();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("事件:").append("报警").append("\n");
        stringBuilder.append("服务器时间:").append(LocalDateTime.now()).append("\n");
        //设置kibana信息，方便定位
        if (ObjectUtils.isNotEmpty(warnLogConfig)) {
            stringBuilder.append("kibana地址:").append(warnLogConfig.getKibanaAddress()).append("\n");
            stringBuilder.append("kibana索引名称:").append(warnLogConfig.getIndexName()).append("\n");
        }
        if (MapUtils.isEmpty(extMap)) {
            for (Map.Entry<String, String> entry : extMap.entrySet()) {
                stringBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
            }
        }
        stringBuilder.append("消息:").append(message);
        content.setText(stringBuilder.toString());
        dingTextMessage.setContent(content);
        return dingTextMessage;
    }


    @NoArgsConstructor
    @Data
    public static class DingTextMessage {

        private String msg_type;
        private Content content;

        @NoArgsConstructor
        @Data
        public static class Content {

            private String text;

        }

    }

}
