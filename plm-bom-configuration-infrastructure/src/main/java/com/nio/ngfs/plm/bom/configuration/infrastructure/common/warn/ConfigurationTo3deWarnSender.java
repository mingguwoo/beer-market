package com.nio.ngfs.plm.bom.configuration.infrastructure.common.warn;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.nio.bom.share.utils.GsonUtils;
import com.nio.ngfs.plm.bom.configuration.remote.FeishuIntegrationClient;
import com.nio.ngfs.plm.bom.configuration.remote.dto.common.PlmEnoviaResult;
import com.nio.ngfs.plm.bom.configuration.remote.dto.feature.PlmFeatureOptionSyncDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants.*;

/**
 * 配置管理同步3DE的告警发送
 *
 * @author xiaozhou.tu
 * @date 2023/8/2
 */
@Component
@RequiredArgsConstructor
public class ConfigurationTo3deWarnSender {

    private static final String TITLE_FORMAT = "配置管理同步3DE告警（%s）";
    private static final String CONTENT_TEMPLATE_1 = """
            模块: %s\r
            失败信息: %s\r
            请求参数: %s\r
            失败响应: %s
            """;
    private static final String CONTENT_TEMPLATE_2 = """
            模块: %s\r
            失败信息: %s\r
            请求参数: %s\r
            异常信息: %s
            """;

    @Value("${warn.3de.env:}")
    private final String env;
    @Value("${warn.3de.feature.ats:}")
    private final String featureAts;

    private final FeishuIntegrationClient feishuIntegrationClient;

    /**
     * 获取Title
     */
    private String getTitle() {
        return String.format(TITLE_FORMAT, env);
    }

    /**
     * 获取Feature Library告警人列表
     */
    private List<String> getFeatureLibraryAtList() {
        return Splitter.on(",").omitEmptyStrings().trimResults().splitToList(featureAts);
    }

    /**
     * 发送同步Feature/Option失败告警
     */
    public void sendSyncFeatureOptionWarn(PlmFeatureOptionSyncDto syncDto, PlmEnoviaResult<Object> result) {
        String message = FeishuPostMessageBuilder.buildPostMessage(getTitle(),
                Lists.newArrayList(String.format(CONTENT_TEMPLATE_1, "Feature Library", buildSyncFeatureOptionMsg(syncDto),
                        GsonUtils.toJson(syncDto), GsonUtils.toJson(result))),
                getFeatureLibraryAtList());
        feishuIntegrationClient.sendMessageTo3deGroup(message);
    }

    /**
     * 发送同步Feature/Option异常告警
     */
    public void sendSyncFeatureOptionWarn(PlmFeatureOptionSyncDto syncDto, Exception e) {
        String message = FeishuPostMessageBuilder.buildPostMessage(getTitle(),
                Lists.newArrayList(String.format(CONTENT_TEMPLATE_2, "Feature Library", buildSyncFeatureOptionMsg(syncDto),
                        GsonUtils.toJson(syncDto), e.getMessage())),
                getFeatureLibraryAtList());
        feishuIntegrationClient.sendMessageTo3deGroup(message);
    }

    private String buildSyncFeatureOptionMsg(PlmFeatureOptionSyncDto syncDto) {
        if (syncDto.getFeature().getType() != null) {
            switch (syncDto.getFeature().getType()) {
                case FEATURE_OPTION_SYNC_ADD -> {
                    return "Add Feature " + syncDto.getFeature().getFeatureCode() + " Fail!";
                }
                case FEATURE_OPTION_SYNC_CHANGE_OLD_DATA -> {
                    return "Update Feature " + syncDto.getFeature().getFeatureCode() + " Fail!";
                }
            }
        } else if (CollectionUtils.isNotEmpty(syncDto.getOptionList())) {
            switch (syncDto.getOptionList().get(0).getType()) {
                case FEATURE_OPTION_SYNC_ADD -> {
                    return "Add Option " + syncDto.getOptionList().get(0).getOptionCode() + " Fail!";
                }
                case FEATURE_OPTION_SYNC_UPDATE -> {
                    return "Update Option " + syncDto.getOptionList().get(0).getOptionCode() + " Fail!";
                }
            }
        }
        return "Sync Feature/Option Fail!";
    }

}
