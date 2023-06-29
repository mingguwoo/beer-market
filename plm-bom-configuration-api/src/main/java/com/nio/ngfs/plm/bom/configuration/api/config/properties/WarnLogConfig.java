package com.nio.ngfs.plm.bom.configuration.api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author luke.zhu
 */
@Data
@Component
@ConfigurationProperties(prefix = "warn.log")
public class WarnLogConfig {

    /**
     * 接口地址
     */
    private String url;

    /**
     * card接口地址
     */
    private String cardUrl;
    /**
     * 窗口消息提醒数量
     */
    private Integer windowSize;
    /**
     * 环境
     */
    private String env;

    /**
     * kibana地址
     */
    private String kibanaAddress = "";

    /**
     * index名称
     */
    private String indexName = "";

}
