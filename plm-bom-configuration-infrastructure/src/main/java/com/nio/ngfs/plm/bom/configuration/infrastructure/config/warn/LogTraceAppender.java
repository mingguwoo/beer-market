package com.nio.ngfs.plm.bom.configuration.infrastructure.config.warn;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.AppenderBase;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nio.bom.share.configuration.WarnLogConfig;
import com.nio.ngfs.common.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * error日志告警监控
 *
 * @author guangjin.gao
 */
@Slf4j
public class LogTraceAppender extends AppenderBase<ILoggingEvent> {

    /**
     * 最近一次告警时间
     */
    private static long recentWarnTime = System.currentTimeMillis();
    /**
     * 滑动窗口时间
     * 自定义机器人的频控策略为 100 次/分钟
     */
    private final static long MAX_WINDOW_TIME = 60 * 1000L;
    /**
     * 栈打印行
     */
    private final static long MAX_STACK_LINE = 5;

    /**
     * 时间窗口范围内告警次数
     */
    private final AtomicInteger windowWarnNum = new AtomicInteger();
    /**
     * 可以忽视的错误日志
     */
    private final static List<String> IGNORE_ALARM_WORD_LIST = Lists.newArrayList();
    /**
     * 环境
     */
    private final static String ENV_DEV = "dev";
    private final static String ENV_LOCAL = "local";
    private final static String ENV_TEST = "test";

    private final static String ENV_STG = "stg";
    /**
     * 提醒时间
     */
    private final static int SKIP_END_TIME = 21;
    private final static int SKIP_BEGIN_TIME = 9;

    //添加特定错误的过滤
    static {
        IGNORE_ALARM_WORD_LIST.add("date format error");
        IGNORE_ALARM_WORD_LIST.add("coordinator");
        IGNORE_ALARM_WORD_LIST.add("timed out");
        IGNORE_ALARM_WORD_LIST.add("UnknownHostException");
        IGNORE_ALARM_WORD_LIST.add("RedisTimeoutException");
        IGNORE_ALARM_WORD_LIST.add("rebalancing");
    }

    /**
     * append
     *
     * @param event event
     */
    @Override
    protected void append(ILoggingEvent event) {
        try {
            if (skip()) {
                return;
            }
            Map<String, String> mdcDataMap = MDC.getCopyOfContextMap() == null ? Maps.newHashMap() : MDC.getCopyOfContextMap();
            //窗口限流推送钉钉消息
            if (Level.ERROR.levelStr.equalsIgnoreCase(event.getLevel().levelStr)) {
                //如果上次告警距离上次告警时间超过5分钟，直接进行通知
                if (System.currentTimeMillis() - recentWarnTime > MAX_WINDOW_TIME) {
                    boolean ret = sendWarningMessage(event, mdcDataMap);
                    if (!ret) {
                        return;
                    }
                    //重置为原始值
                    windowWarnNum.set(1);
                    recentWarnTime = System.currentTimeMillis();
                } else if (windowWarnNum.intValue() <= SpringContextHolder.getApplicationContext().getBean(WarnLogConfig.class).getWindowSize()) {
                    boolean ret = sendWarningMessage(event, mdcDataMap);
                    if (!ret) {
                        return;
                    }
                    windowWarnNum.incrementAndGet();
                    recentWarnTime = System.currentTimeMillis();
                } else {
                    log.info("告警信息推送qps过高，已经拦截");
                }
            }
        } catch (Exception e) {
            log.warn("LogWarningAppender append error", e);
        }
    }

    /**
     * dev环境不发送
     * test环境9～22点之外不发送提醒
     *
     * @return boolean
     */
    private boolean skip() {
        if (SpringContextHolder.getApplicationContext() == null) {
            return true;
        }

        String env = SpringContextHolder.getApplicationContext().getBean(WarnLogConfig.class).getEnv();
        if (StringUtils.isEmpty(env)) {
            return true;
        }

        //local、dev直接跳过
        if (ENV_DEV.equals(env) || ENV_LOCAL.equals(env)) {
            return true;
        }

        //test/stg 非工作时间忽略告警
        Calendar calendar = Calendar.getInstance();
        if (ENV_TEST.equals(env) || ENV_STG.equals(env)) {
            return calendar.get(Calendar.HOUR_OF_DAY) > SKIP_END_TIME || calendar.get(Calendar.HOUR_OF_DAY) < SKIP_BEGIN_TIME;
        }
        return false;
    }

    /**
     * sendWarningMessage
     *
     * @param event      event
     * @param mdcDataMap mdcDataMap
     * @return boolean
     */
    private boolean sendWarningMessage(ILoggingEvent event, Map<String, String> mdcDataMap) {
        String errorStackInfo = buildErrorStackInfo(event);
        if (StringUtils.isNotBlank(errorStackInfo)) {
            for (String word : IGNORE_ALARM_WORD_LIST) {
                if (errorStackInfo.contains(word)) {
                    return false;
                }
            }
        }
        Map<String, String> extMap = Maps.newLinkedHashMap();
        extMap.put("env", SpringContextHolder.getApplicationContext().getBean(WarnLogConfig.class).getEnv());
        Map<String, String> traceInfoMap = buildTraceInfoMap(mdcDataMap);
        extMap.putAll(traceInfoMap);
        WarnLogUtils.send(SpringContextHolder.getApplicationContext().getBean(WarnLogConfig.class).getUrl(), "应用发生错误，日志详情信息:" + errorStackInfo, extMap);
        return true;
    }

    /**
     * buildErrorStackInfo
     *
     * @param event event
     * @return String
     */
    private String buildErrorStackInfo(ILoggingEvent event) {
        StringBuilder errorStackTraceInfo = new StringBuilder();
        errorStackTraceInfo.append("\n").append(event.getFormattedMessage()).append("\n");
        if (event.getThrowableProxy() != null) {
            errorStackTraceInfo.append(event.getThrowableProxy().getClassName());
            if (StringUtils.isNotEmpty(event.getThrowableProxy().getMessage())) {
                errorStackTraceInfo.append(":").append(event.getThrowableProxy().getMessage()).append("\n");
            } else {
                errorStackTraceInfo.append("\n");
            }
            StackTraceElementProxy[] stackTraceElementProxies = event.getThrowableProxy().getStackTraceElementProxyArray();
            if (stackTraceElementProxies != null) {
                //输出堆栈前面5行
                for (int i = 0; i < MAX_STACK_LINE; i++) {
                    for (int j = 0; j < MAX_STACK_LINE; j++) {
                        errorStackTraceInfo.append("\u00A0");
                    }
                    errorStackTraceInfo.append(stackTraceElementProxies[i].getSTEAsString()).append("\n");
                }
            }
            IThrowableProxy cause = event.getThrowableProxy().getCause();
            if (cause != null && StringUtils.isNotEmpty(cause.getMessage())) {
                errorStackTraceInfo.append("Caused by:").append(cause.getMessage()).append("\n");
            }
        }
        return errorStackTraceInfo.toString();
    }

    /**
     * buildTraceInfoMap
     *
     * @param mdcDataMap mdcDataMap
     * @return Map
     */
    private Map<String, String> buildTraceInfoMap(Map<String, String> mdcDataMap) {
        Map<String, String> retMap = Maps.newLinkedHashMap();
        retMap.put("txId", "TID:" + mdcDataMap.get("PtxId"));
        return retMap;
    }

}
