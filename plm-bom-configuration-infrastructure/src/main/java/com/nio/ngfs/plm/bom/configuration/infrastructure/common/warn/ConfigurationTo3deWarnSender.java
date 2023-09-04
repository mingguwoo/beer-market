package com.nio.ngfs.plm.bom.configuration.infrastructure.common.warn;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.nio.bom.share.utils.GsonUtils;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncAddPcDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncDeletePcDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncUpdatePcDto;
import com.nio.ngfs.plm.bom.configuration.remote.FeishuIntegrationClient;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.*;
import com.nio.ngfs.plm.bom.configuration.remote.dto.feature.PlmFeatureOptionSyncDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
public class ConfigurationTo3deWarnSender {

    private static final String TITLE_FORMAT = "配置管理同步3DE告警（%s）";
    private static final String FEATURE_LIBRARY = "Feature Library";
    private static final String PRODUCT_CONFIGURATION = "Product Configuration";
    private static final String PRODUCT_CONTEXT = "Product Context";
    private static final String CONTENT_TEMPLATE = """
            模块: %s\r
            失败信息: %s\r
            请求参数: %s\r
            错误信息: %s
            """;

    @Value("${warn.3de.env:}")
    private String env;
    @Value("${warn.3de.feature.ats:}")
    private String featureAts;
    @Value("${warn.3de.productConfig.ats:}")
    private String productConfigAts;
    @Value("${warn.3de.productContext.ats:}")
    private String productContextAts;

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
     * 获取Product Config告警人列表
     */
    private List<String> getProductConfigAtList() {
        return Splitter.on(",").omitEmptyStrings().trimResults().splitToList(productConfigAts);
    }

    /**
     * 获取Product Context告警人列表
     * @return
     */
    private List<String> getProductContextAtList() {
        return Splitter.on(",").omitEmptyStrings().trimResults().splitToList(productContextAts);
    }
    /**
     * 发送同步Feature/Option告警
     */
    public void sendSyncFeatureOptionWarn(PlmFeatureOptionSyncDto syncDto, String errorMsg) {
        sendWarnMessage(FEATURE_LIBRARY, syncDto, errorMsg, getFeatureLibraryAtList(), buildSyncFeatureOptionMsg(syncDto));
    }

    /**
     * 发送同步新增PC告警
     */
    public void sendAddPcWarn(SyncAddPcDto dto, PlmSyncProductConfigurationDto syncDto, String errorMsg) {
        sendWarnMessage(PRODUCT_CONFIGURATION, syncDto, errorMsg, getProductConfigAtList(),
                String.format("Model/Model Year %s Add Base PC %s Fail!", dto.getModel() + " " + dto.getModelYear(), syncDto.getPcId()));
    }

    /**
     * 发送同步修改PC告警
     */
    public void sendUpdatePcWarn(SyncUpdatePcDto dto, PlmModifyPcDto syncDto, String errorMsg) {
        sendWarnMessage(PRODUCT_CONFIGURATION, syncDto, errorMsg, getProductConfigAtList(),
                String.format("Model/Model Year %s Update Base PC %s Fail!", dto.getModel() + " " + dto.getModelYear(), syncDto.getPcId()));
    }

    /**
     * 发送同步删除PC告警
     */
    public void sendDeletePcWarn(SyncDeletePcDto dto, PlmDeletePcDto syncDto, String errorMsg) {
        sendWarnMessage(PRODUCT_CONFIGURATION, syncDto, errorMsg, getProductConfigAtList(),
                String.format("Model/Model Year %s Delete Base PC %s Fail!", dto.getModel() + " " + dto.getModelYear(), syncDto.getPcId()));
    }

    /**
     * 发送同步Prodcut Context Model Feature告警
     * @param dto
     * @param errorMsg
     */
    public void sendSyncProductContextFeatureModelWarn(PlmSyncProductContextModelFeatureDto dto, String errorMsg) {
        sendWarnMessage(PRODUCT_CONTEXT, dto, errorMsg, getProductContextAtList(),
                String.format("Model %s Sync Code %s Fail!", dto.getModelCodeList().get(0), dto.getFeatureCode()));
    }

    /**
     * 发送同步Prodcut Context Model Feature Option告警
     * @param dto
     * @param errorMsg
     */
    public void sendSyncProductContextModelFeatureOptionWarn(PlmSyncProductContextModelFeatureOptionDto dto, String errorMsg) {
        sendWarnMessage(PRODUCT_CONTEXT,dto,errorMsg,getProductContextAtList(),
                String.format("Model %s Sync Code %s Fail!", dto.getModel(),dto.getFeature().get(0).getOption().get(0).getOptionCode()));
    }
    private <Req> void sendWarnMessage(String module, Req request, String errorMsg, List<String> atList, String failMsg) {
        String message = FeishuPostMessageBuilder.buildPostMessage(
                getTitle(),
                Lists.newArrayList(String.format(CONTENT_TEMPLATE, module, failMsg, GsonUtils.toJson(request), errorMsg)),
                atList
        );
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
