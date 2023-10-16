package com.nio.ngfs.plm.bom.configuration.application.command.productconfig;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.application.service.ProductConfigModelOptionApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotRepository;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.SyncProductConfigModelOptionCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.SyncProductConfigModelOptionRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 从OXO同步ProductConfig Feature/Option行
 *
 * @author xiaozhou.tu
 * @date 2023/10/16
 */
@Component
@RequiredArgsConstructor
public class SyncProductConfigModelOptionCommand extends AbstractLockCommand<SyncProductConfigModelOptionCmd, SyncProductConfigModelOptionRespDto> {

    private final OxoVersionSnapshotRepository oxoVersionSnapshotRepository;
    private final ProductConfigModelOptionApplicationService productConfigModelOptionApplicationService;

    @Override
    protected String getLockKey(SyncProductConfigModelOptionCmd cmd) {
        return RedisKeyConstant.PRODUCT_CONFIG_EDIT_PRODUCT_CONFIG_LOCK_KEY_PREFIX;
    }

    @Override
    protected Long getLockTime(SyncProductConfigModelOptionCmd cmd) {
        return 60L;
    }

    @Override
    protected SyncProductConfigModelOptionRespDto executeWithLock(SyncProductConfigModelOptionCmd cmd) {
        List<String> modelList;
        if (cmd.isAllLatestReleaseOxo()) {
            // 同步全部车型
            modelList = oxoVersionSnapshotRepository.getAllModelList();
        } else {
            // 同步指定车型列表
            modelList = cmd.getLatestReleaseOxoModelList();
        }
        if (CollectionUtils.isEmpty(modelList)) {
            return new SyncProductConfigModelOptionRespDto();
        }
        modelList.forEach(model -> {
            OxoVersionSnapshotAggr oxoVersionSnapshotAggr = oxoVersionSnapshotRepository.queryLastReleaseSnapshotByModel(model, null);
            productConfigModelOptionApplicationService.syncFeatureOptionFromOxoRelease(oxoVersionSnapshotAggr);
        });
        return new SyncProductConfigModelOptionRespDto();
    }

}
