package com.sh.beer.market.infrastructure.config.warn;


import lombok.extern.slf4j.Slf4j;

/**
 * 日志告警UTIl
 *
 * @author
 */
@Slf4j
public class WarnLogUtils {
    /**
     * 发送飞书告警
     *
     * @param url     url
     * @param message message
     * @param extMap  extMap
     *//*
    public static void send(String url, String message, Map<String, String> extMap) {
        if (StringUtils.isEmpty(url)) {
            log.warn("WarnMsgUtil send msg fail,because url is empty");
            return;
        }
        try {
            DingTextMessage dtMsg = generateRequestBean(message, extMap);
            if (Objects.nonNull(dtMsg)) {
                SpringContextHolder.getBean(FeishuIntegrationClient.class).sendMessageToLogGroup(JSON.toJSONString(dtMsg));
            }
        } catch (Exception e) {
            log.warn("send warn msg error,dingUrl:{}", url, e);
        }
    }

    *//**
     * 告警内容
     *
     * @param message message
     * @param extMap  extMap
     * @return DingTextMessage
     *//*
    private static DingTextMessage generateRequestBean(String message, Map<String, String> extMap) {
        WarnLogConfig warnLogConfig = SpringContextHolder.getApplicationContext().getBean(WarnLogConfig.class);
        if (filter(warnLogConfig, message)) {
            return null;
        }
        DingTextMessage dingTextMessage = new DingTextMessage();
        dingTextMessage.setMsg_type("text");
        DingTextMessage.Content content = new DingTextMessage.Content();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("事件:").append("报警").append("\n");
        stringBuilder.append("服务器时间:").append(LocalDateTime.now()).append("\n");
        //设置luban cls信息，方便定位
        if (ObjectUtils.isNotEmpty(warnLogConfig)) {
            stringBuilder.append("cls地址:").append(warnLogConfig.getLubanAddress()).append("\n");
            stringBuilder.append("env:").append(warnLogConfig.getEnv()).append("\n");
            String podIp = Optional.ofNullable(SpringContextHolder.getApplicationContext().getEnvironment().getProperty("POD_IP")).orElse("localhost");
            //添加pod ip区分本地还是pod环境
            stringBuilder.append("pod_ip:").append(podIp).append("\n");
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

    *//**
     * 过滤不需要的报错
     * *//*
    private static boolean filter(WarnLogConfig warnLogConfig, String message) {
        if ("test".equals(warnLogConfig.getEnv())) {
            List<String> filterWords = Arrays.stream(warnLogConfig.getFilterWords().split(",")).toList();
            return filterWords.stream().anyMatch(message::contains);
        }
        return false;
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

    }*/

}
