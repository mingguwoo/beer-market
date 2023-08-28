package com.nio.ngfs.plm.bom.configuration.application.command.oxo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.common.utils.BeanConvertUtils;
import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.application.service.*;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.enums.OxoSnapshotEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoVersionSnapshotDomainService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoVersionSnapshotDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoVersionSnapshotEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoSnapshotCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OxoSnapshotCommand extends AbstractLockCommand<OxoSnapshotCmd, List<String>> {


    private final OxoVersionSnapshotDomainService oxoVersionSnapshotDomainService;


    private final BomsOxoVersionSnapshotDao bomsOxoVersionSnapshotDao;


    private final OxoQueryApplicationService oxoQueryApplicationService;


    private final ProductConfigModelOptionApplicationService productConfigModelOptionApplicationService;


    private final OxoCompareApplicationService oxoCompareDomainService;


    private final ProductContextApplicationService productContextApplicationService;

    private final TransactionTemplate transactionTemplate;

    private final OxoFeatureOptionApplicationService oxoFeatureOptionApplicationService;


    @Override
    protected String getLockKey(OxoSnapshotCmd editGroupCmd) {
        return RedisKeyConstant.OXO_SAVE_SNAPSHOT_LOCK_KEY_PREFIX + editGroupCmd.getModelCode();
    }

    @Override
    protected List<String> executeWithLock(OxoSnapshotCmd editGroupCmd) {


        String modelCode = editGroupCmd.getModelCode();
        String type = editGroupCmd.getType();

        //版本
        OxoVersionSnapshotAggr oxoVersionSnapshotAggr = oxoVersionSnapshotDomainService.queryVersionByModelCode(modelCode, type);

        String version = oxoVersionSnapshotAggr.getVersion();
        //获取 oxo版本内容
        OxoBaseCmd baseCmd = new OxoBaseCmd();
        baseCmd.setModelCode(modelCode);

        //查询 oxo最新working 版本信息  Informal 仅包含Maturity为P且Status为Active的Base Vehicle
        OxoListQry oxoListQry = oxoQueryApplicationService.queryOxoInfoByModelCode(modelCode, ConfigConstants.WORKING,
                StringUtils.equals(type, OxoSnapshotEnum.FORMAL.getCode()));

        // 如果是首发版本
        if (StringUtils.contains(version, ConfigConstants.VERSION_AA)) {
            //- 针对Model下的首版OXO发布，系统需校验AF00是否存在于OXO中
            if (oxoListQry.getOxoRowsResps().stream().noneMatch(x -> StringUtils.equals(x.getFeatureCode(), ConfigConstants.FEATURE_CODE_AF00))) {
                throw new BusinessException(ConfigErrorCode.AF_ERROR);
            }
        }

        OxoVersionSnapshotAggr oxoVersionSnapshot = OxoVersionSnapshotFactory.buildOxoFeatureOptions(oxoListQry, version, editGroupCmd);


        //开启事务
        transactionTemplate.execute(status -> {
            //添加 oxo快照信息
            bomsOxoVersionSnapshotDao.insertBomsOxoVersionSnapshot(BeanConvertUtils.convertTo(
                    oxoVersionSnapshot, BomsOxoVersionSnapshotEntity::new));


            //同步product context
            try {
                productContextApplicationService.addProductContext(oxoVersionSnapshot.getOxoSnapshot());
            } catch (Exception e) {
                log.error("addProductContext error", e);
                throw new BusinessException("Sync Product Context Fail!");
            }

            //同步product config
            try {
                productConfigModelOptionApplicationService.syncFeatureOptionFromOxoRelease(oxoVersionSnapshot);
            } catch (Exception e) {
                log.error("syncFeatureOptionFromOxoRelease error", e);
                throw new BusinessException("Sync Product Configuration Fail!");
            }


            //发送对比邮件  FORMAL版本邮件，版本号 AA
            if (StringUtils.equals(type, OxoSnapshotEnum.FORMAL.getCode())
                    && !StringUtils.equals(version, ConfigConstants.VERSION_AA)
                    && StringUtils.isNotBlank(oxoVersionSnapshotAggr.getPreOxoSnapshot())) {

                //获取对比信息
                OxoListQry preOxoListQry = JSONObject.parseObject(JSONArray.parse(oxoVersionSnapshotAggr.getPreOxoSnapshot()).toString(), OxoListQry.class);

                // 对比
                OxoListQry compareOxoListQry = oxoCompareDomainService.compareVersion(oxoListQry, preOxoListQry, true);

                oxoVersionSnapshot.setPreOxoSnapshot(oxoVersionSnapshotAggr.getPreOxoSnapshot());
                oxoVersionSnapshot.setPreVersion(oxoVersionSnapshotAggr.getPreVersion());
                //发送邮件
                oxoCompareDomainService.sendCompareEmail(compareOxoListQry, oxoVersionSnapshot);

            }
            return oxoFeatureOptionApplicationService.checkOxoFeatureCode(modelCode);
        });

        return Lists.newArrayList();
    }
}
