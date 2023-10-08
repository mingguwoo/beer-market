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
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.enums.OxoOptionPackageTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.enums.OxoSnapshotEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.event.OxoVersionSnapshotPublishEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoVersionSnapshotDomainService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoVersionSnapshotDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoVersionSnapshotEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoSnapshotCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

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

    private final EventPublisher eventPublisher;

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
        OxoListRespDto oxoLists = oxoQueryApplicationService.queryOxoInfoByModelCode(modelCode, ConfigConstants.WORKING,
                StringUtils.equals(type, OxoSnapshotEnum.FORMAL.getCode()), Lists.newArrayList());



        OxoVersionSnapshotAggr oxoVersionSnapshot = OxoVersionSnapshotFactory.buildOxoFeatureOptions(oxoLists, version, editGroupCmd);

        // 检查快照数据
        checkSnapshot(version,oxoLists,type);


        //开启事务
        transactionTemplate.execute(status -> {
            //添加 oxo快照信息
            bomsOxoVersionSnapshotDao.insertBomsOxoVersionSnapshot(BeanConvertUtils.convertTo(
                    oxoVersionSnapshot, BomsOxoVersionSnapshotEntity::new));

            //同步product context
            try {
                productContextApplicationService.addProductContext(oxoLists, editGroupCmd.getUserName());
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
                OxoListRespDto preOxoListQry = JSONObject.parseObject(JSONArray.parse(oxoVersionSnapshotAggr.getPreOxoSnapshot()).toString(), OxoListRespDto.class);

                // 对比
                OxoListRespDto compareOxoListQry = oxoCompareDomainService.compareVersion(oxoLists, preOxoListQry, true);

                oxoVersionSnapshot.setPreOxoSnapshot(oxoVersionSnapshotAggr.getPreOxoSnapshot());
                oxoVersionSnapshot.setPreVersion(oxoVersionSnapshotAggr.getPreVersion());
                //发送邮件
                oxoCompareDomainService.sendCompareEmail(compareOxoListQry, oxoVersionSnapshot);

            }
            // 发布事件
            eventPublisher.publish(new OxoVersionSnapshotPublishEvent(oxoVersionSnapshot));
            return oxoFeatureOptionApplicationService.checkOxoFeatureCode(modelCode);
        });

        return Lists.newArrayList();
    }


    public void checkSnapshot(String version, OxoListRespDto oxoLists, String type) {
        // 如果是首发版本
        if (StringUtils.contains(version, ConfigConstants.VERSION_AA)) {
            //- 针对Model下的首版OXO发布，系统需校验AF00是否存在于OXO中
            if (CollectionUtils.isEmpty(oxoLists.getOxoRowsResps()) ||
                    oxoLists.getOxoRowsResps().stream().noneMatch(x -> StringUtils.equals(x.getFeatureCode(), ConfigConstants.FEATURE_CODE_AF00))) {
                throw new BusinessException(ConfigErrorCode.AF_ERROR);
            }
        }
        //非首发formal 版本 表头不能为空
        if (StringUtils.equals(type, OxoSnapshotEnum.FORMAL.getCode()) && CollectionUtils.isEmpty(oxoLists.getOxoHeadResps())) {
            throw new BusinessException(ConfigErrorCode.MATURITY_ERROR);
        }


        //校验数据  每个版本必须要有打点信息
        List<OxoRowsQry> oxoRowsQries = oxoLists.getOxoRowsResps();
        List<Long> headIds = Lists.newArrayList();

        // 获取头id
        oxoLists.getOxoHeadResps().forEach(oxoHeadQry -> {
            oxoHeadQry.getRegionInfos().forEach(regionInfo -> {
                regionInfo.getDriveHands().forEach(driveHandInfo -> {
                    driveHandInfo.getSalesVersionInfos().forEach(salesVersionInfo -> {
                        headIds.add(salesVersionInfo.getHeadId());
                    });
                });
            });
        });

        //获取没有 打点信息的optionCode
        List<String> options = Lists.newArrayList();
        oxoRowsQries.forEach(oxoRowsQry -> {
            oxoRowsQry.getOptions().forEach(option -> {
                List<Long> packageHeadIds = option.getPackInfos().stream().map(OxoEditCmd::getHeadId).distinct().toList();
                if(!packageHeadIds.containsAll(headIds.stream().distinct().toList())){
                    options.add(option.getFeatureCode());
                }
            });
        });


        if (CollectionUtils.isNotEmpty(options)) {
            String message = MessageFormat.format(ConfigErrorCode.EDIT_OPTION_ERROR.getMessage(), String.join(",",options));
            throw new BusinessException(message);
        }


        //基于Base Vehicle，某一Feature 下的所有Option打点是否存在多个（超过1个）实心圈
        List<String> featureCodes = Lists.newArrayList();
        oxoRowsQries.forEach(oxoRowsQry -> {
            headIds.forEach(headId -> {
                List<String> packageCodes = Lists.newArrayList();
                oxoRowsQry.getOptions().forEach(option -> {
                    packageCodes.add(option.getPackInfos().stream().filter(x -> Objects.equals(x.getHeadId(), headId))
                            .findFirst().orElse(new OxoEditCmd()).getPackageCode());
                });

                if (packageCodes.stream().filter(x -> StringUtils.equals(x, OxoOptionPackageTypeEnum.DEFALUT.getType())).count() > 1) {
                    featureCodes.add(oxoRowsQry.getFeatureCode());
                }
            });
        });

        if (CollectionUtils.isNotEmpty(featureCodes)) {
            throw new BusinessException(MessageFormat.format(ConfigErrorCode.EDIT_FEATURE_ERROR.getMessage(), String.join(",",featureCodes)));
        }
    }

}

