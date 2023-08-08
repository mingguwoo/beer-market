package com.nio.ngfs.plm.bom.configuration.domain.service.oxo.impl;

import com.nio.bom.share.enums.ErrorCode;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.VersionUtils;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.enums.OxoSnapshotEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoVersionSnapshotDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.DeleteBaseVehicleCmd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OxoVersionSnapshotDomainServiceImpl implements OxoVersionSnapshotDomainService {


    private final OxoVersionSnapshotRepository oxoVersionSnapshotRepository;


    /**
     * 查询 oxo版本
     * @param modelCode
     * @param type
     * @return
     */
    @Override
    public String queryVersionByModelCode(String modelCode, String type) {


        List<OxoVersionSnapshotAggr> oxoVersionSnapshots =
                oxoVersionSnapshotRepository.queryOxoVersionSnapshotByModelCode(modelCode);


        // 根据 车型获取 oxo版本
        if (CollectionUtils.isNotEmpty(oxoVersionSnapshots)) {

            List<String> versions = oxoVersionSnapshots.stream().filter(x -> StringUtils.equals(x.getType(), OxoSnapshotEnum.FORMAL.getCode())).
                    map(OxoVersionSnapshotAggr::getVersion).distinct().sorted(Comparator.reverseOrder()).toList();


            String version = versions.get(0);

            // Formal
            if (StringUtils.equals(type, OxoSnapshotEnum.FORMAL.getCode())) {

                //如果包含小版本
                if (version.contains(ConfigConstants.REG_DOT)) {
                    //则系统基于最新已发布版本号去除后缀，保留前两个大写字母，生成新版本号
                    return VersionUtils.getMajorRev(version);
                } else {
                    //则系统基于最新已发布版本号往后顺延自动生成新版本号
                    return VersionUtils.findNextRev(version);
                }
            }

            if (StringUtils.equals(type, OxoSnapshotEnum.INFORMAL.getCode())) {
                //则系统基于最新已发布版本号，后缀数字+1，生成新版本号
                if (version.contains(ConfigConstants.REG_DOT)) {
                    return VersionUtils.getNextMajorRev(version);
                } else {
                    //则系则系统基于最新已发布版本号往后顺延+“.”+数字，再自动生成新版本号
                    //系统中存在的最新版本号为AB，则系统基于AB往后顺延（字母排序）生成AC+“.”+数字，即生成AC.1
                    return VersionUtils.findNextRev(version) + ConfigConstants.REG_DOT + "1";
                }
            }
        } else {

            if (StringUtils.equals(type, OxoSnapshotEnum.FORMAL.getCode())) {
                return ConfigConstants.VERSION_AA;
            } else {
                return ConfigConstants.VERSION_AA_1;
            }
        }

        return StringUtils.EMPTY;
    }

    @Override
    public void checkBaseVehicleReleased(String modelCode) {
        if (CollectionUtils.isNotEmpty(oxoVersionSnapshotRepository.queryBomsOxoVersionSnapshotsByModel(modelCode))){
            throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_ALREADY_RELEASED);
        }
    }

}
