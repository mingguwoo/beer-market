package com.sh.beer.market.infrastructure.common.warn;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 配置管理同步3DE的告警发送
 *
 * @author
 * @date 2023/8/2
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ConfigurationTo3deWarnSender {

    /*private static final String TITLE_FORMAT = "配置管理同步3DE告警（%s）";
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

    *//**
     * 获取Title
     *//*
    private String getTitle() {
        return String.format(TITLE_FORMAT, env);
    }

    *//**
     * 获取Feature Library告警人列表
     *//*
    private List<String> getFeatureLibraryAtList() {
        return Splitter.on(",").omitEmptyStrings().trimResults().splitToList(featureAts);
    }

    *//**
     * 获取Product Config告警人列表
     *//*
    private List<String> getProductConfigAtList() {
        return Splitter.on(",").omitEmptyStrings().trimResults().splitToList(productConfigAts);
    }

    *//**
     * 获取Product Context告警人列表
     *
     * @return
     *//*
    private List<String> getProductContextAtList() {
        return Splitter.on(",").omitEmptyStrings().trimResults().splitToList(productContextAts);
    }

    *//**
     * 发送同步Feature/Option告警
     *//*
    public void sendSyncFeatureOptionWarn(PlmFeatureOptionSyncDto syncDto, String errorMsg) {
        sendWarnMessage(FEATURE_LIBRARY, syncDto, errorMsg, getFeatureLibraryAtList(), buildSyncFeatureOptionMsg(syncDto));
    }

    *//**
     * 发送同步勾选ProductConfig告警
     *//*
    public void sendSelectPcOptionWarn(PlmConnectPcFeatureAndOptionDto syncDto, String errorMsg) {
        sendWarnMessage(PRODUCT_CONFIGURATION, Lists.newArrayList(syncDto), errorMsg, getProductConfigAtList(),
                String.format("PC %s Sync Select Code %s Fail!", syncDto.getPcId(), syncDto.getOptionCode()));
    }

    *//**
     * 发送同步取消勾选ProductConfig告警
     *//*
    public void sendUnselectPcOptionWarn(PlmDisconnectPcFeatureAndOptionDto syncDto, String errorMsg) {
        sendWarnMessage(PRODUCT_CONFIGURATION, Lists.newArrayList(syncDto), errorMsg, getProductConfigAtList(),
                String.format("PC %s Sync Unselect Code %s Fail!", syncDto.getPcId(), syncDto.getOptionCode()));
    }

    *//**
     * 发送同步新增PC告警
     *//*
    public void sendAddPcWarn(SyncAddPcDto dto, PlmSyncProductConfigurationDto syncDto, String errorMsg) {
        sendWarnMessage(PRODUCT_CONFIGURATION, syncDto, errorMsg, getProductConfigAtList(),
                String.format("Model/Model Year %s Add Base PC %s Fail!", dto.getModel() + " " + dto.getModelYear(), syncDto.getParamMap().getPcId()));
    }

    *//**
     * 发送同步修改PC告警
     *//*
    public void sendUpdatePcWarn(SyncUpdatePcDto dto, PlmModifyPcDto syncDto, String errorMsg) {
        sendWarnMessage(PRODUCT_CONFIGURATION, syncDto, errorMsg, getProductConfigAtList(),
                String.format("Model/Model Year %s Update Base PC %s Fail!", dto.getModel() + " " + dto.getModelYear(), syncDto.getParamMap().getPcId()));
    }

    *//**
     * 发送同步删除PC告警
     *//*
    public void sendDeletePcWarn(SyncDeletePcDto dto, PlmDeletePcDto syncDto, String errorMsg) {
        sendWarnMessage(PRODUCT_CONFIGURATION, syncDto, errorMsg, getProductConfigAtList(),
                String.format("Model/Model Year %s Delete Base PC %s Fail!", dto.getModel() + " " + dto.getModelYear(), syncDto.getParamMap().getPcId()));
    }

    *//**
     * 发送同步Prodcut Context Model Feature告警
     *
     * @param dto
     * @param errorMsg
     *//*
    public void sendSyncProductContextFeatureModelWarn(SyncProductContextModelFeatureDto dto, String errorMsg) {
        sendWarnMessage(PRODUCT_CONTEXT, dto, errorMsg, getProductContextAtList(),
                String.format("Model %s Sync Code %s Fail!", dto.getModelCodeList().get(0), dto.getFeatureCode()));
    }

    *//**
     * 发送同步Prodcut Context Model Feature Option告警
     *
     * @param dto
     * @param errorMsg
     *//*
    public void sendSyncProductContextModelFeatureOptionWarn(SyncProductContextModelFeatureOptionDto dto, String errorMsg) {
        sendWarnMessage(PRODUCT_CONTEXT, dto, errorMsg, getProductContextAtList(),
                String.format("Model %s Sync Code Fail!", dto.getModel()));
    }

    private <Req> void sendWarnMessage(String module, Req request, String errorMsg, List<String> atList, String failMsg) {
        // 错误信息特殊转译符号处理
        if (StringUtils.isNotBlank(errorMsg)) {
            errorMsg = errorMsg.replaceAll("\u003d", "=");
        }
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
    }*/

}
