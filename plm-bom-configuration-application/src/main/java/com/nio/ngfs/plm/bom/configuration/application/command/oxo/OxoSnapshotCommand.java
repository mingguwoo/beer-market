package com.nio.ngfs.plm.bom.configuration.application.command.oxo;

import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.common.utils.BeanConvertUtils;
import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.application.query.oxo.OxoInfoQuery;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotFactory;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoVersionSnapshotDomainService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoVersionSnapshotDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoVersionSnapshotEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoSnapshotCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoSnapshotCommand extends AbstractLockCommand<OxoSnapshotCmd, Boolean> {


    private final OxoVersionSnapshotDomainService oxoVersionSnapshotDomainService;


    private final BomsOxoVersionSnapshotDao bomsOxoVersionSnapshotDao;



    private final OxoInfoQuery oxoInfoQuery;


    @Override
    protected String getLockKey(OxoSnapshotCmd editGroupCmd) {
        return RedisKeyConstant.OXO_SAVE_SNAPSHOT_LOCK_KEY_PREFIX + editGroupCmd.getModelCode();
    }

    @Override
    protected Boolean executeWithLock(OxoSnapshotCmd editGroupCmd) {

        String modelCode = editGroupCmd.getModelCode();
        String type = editGroupCmd.getType();

        //版本
        String version = oxoVersionSnapshotDomainService.queryVersionByModelCode(modelCode, type);

        //获取 oxo版本内容
        OxoBaseCmd baseCmd = new OxoBaseCmd();
        baseCmd.setModelCode(modelCode);

        //查询 oxo信息
        OxoListQry oxoListQry = oxoInfoQuery.execute(baseCmd);

        // 如果是首发版本
        if(StringUtils.contains(version, ConfigConstants.VERSION_AA)){
          //- 针对Model下的首版OXO发布，系统需校验AF00是否存在于OXO中
            if(oxoListQry.getOxoRowsResps().stream().noneMatch(x-> StringUtils.equals(x.getFeatureCode(),ConfigConstants.FEATURE_CODE_AF00))){
                throw new BusinessException(ConfigErrorCode.AF_ERROR);
            }
        }

        OxoVersionSnapshotAggr oxoVersionSnapshot = OxoVersionSnapshotFactory.buildOxoFeatureOptions(oxoListQry, version, editGroupCmd);


        //添加 oxo快照信息
        bomsOxoVersionSnapshotDao.insertBomsOxoVersionSnapshot(BeanConvertUtils.convertTo(
                oxoVersionSnapshot, BomsOxoVersionSnapshotEntity::new));



        //发送对比邮件




        return true;
    }
}
