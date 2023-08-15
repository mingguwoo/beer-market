package com.nio.ngfs.plm.bom.configuration.domain.service.oxo.impl;

import com.alibaba.fastjson.JSON;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.GZIPUtils;
import com.nio.bom.share.utils.VersionUtils;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.enums.OxoSnapshotEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoVersionSnapshotDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
     *
     * @param modelCode
     * @param type
     * @return
     */
    @Override
    public OxoVersionSnapshotAggr queryVersionByModelCode(String modelCode, String type) {


        List<OxoVersionSnapshotAggr> oxoVersionSnapshots =
                oxoVersionSnapshotRepository.queryOxoVersionSnapshotByModelCode(modelCode, null, null);


        OxoVersionSnapshotAggr oxoVersionSnapshotAggr = new OxoVersionSnapshotAggr();


        // 根据 车型获取 oxo版本
        if (CollectionUtils.isNotEmpty(oxoVersionSnapshots)) {

            List<OxoVersionSnapshotAggr> formalVersions = oxoVersionSnapshots.stream().filter(x -> StringUtils.equals(x.getType(), OxoSnapshotEnum.FORMAL.getCode()))
                    .sorted(Comparator.comparing(OxoVersionSnapshotAggr::getVersion).reversed()).toList();


            // 如果FORMAL版本 为空
            if (CollectionUtils.isEmpty(formalVersions) && StringUtils.equals(type, OxoSnapshotEnum.FORMAL.getCode())) {

                oxoVersionSnapshotAggr.setVersion(ConfigConstants.VERSION_AA);

            } else if (CollectionUtils.isEmpty(formalVersions) && StringUtils.equals(type, OxoSnapshotEnum.INFORMAL.getCode())) {

                List<OxoVersionSnapshotAggr> informalVersions =
                        oxoVersionSnapshots.stream().filter(x -> StringUtils.equals(x.getType(), OxoSnapshotEnum.INFORMAL.getCode()))
                                .sorted(Comparator.comparing(OxoVersionSnapshotAggr::getVersion).reversed()).toList();

                if (CollectionUtils.isNotEmpty(informalVersions)) {
                    oxoVersionSnapshotAggr.setVersion(VersionUtils.getNextMajorRev(informalVersions.get(0).getVersion()));
                }
            } else if (CollectionUtils.isNotEmpty(formalVersions)) {

                OxoVersionSnapshotAggr oxoVersionSnapshot = formalVersions.get(0);
                String version = oxoVersionSnapshot.getVersion();

                if (StringUtils.equals(type, OxoSnapshotEnum.FORMAL.getCode())) {
                    //如果包含小版本
                    if (version.contains(ConfigConstants.REG_DOT)) {
                        //则系统基于最新已发布版本号去除后缀，保留前两个大写字母，生成新版本号
                        oxoVersionSnapshotAggr.setVersion(VersionUtils.getMajorRev(version));
                    } else {
                        //则系统基于最新已发布版本号往后顺延自动生成新版本号
                        oxoVersionSnapshotAggr.setVersion(VersionUtils.findNextRev(version));
                    }
                    oxoVersionSnapshotAggr.setPreVersion(VersionUtils.findPrevRev(oxoVersionSnapshotAggr.getVersion()));
                    oxoVersionSnapshotAggr.setPreOxoSnapshot(
                            formalVersions.stream().filter(x -> StringUtils.equals(x.getVersion(), oxoVersionSnapshotAggr.getPreVersion())).findFirst()
                                    .orElse(new OxoVersionSnapshotAggr()).getOxoSnapshot());
                } else {
                    if (version.contains(ConfigConstants.REG_DOT)) {
                        oxoVersionSnapshotAggr.setVersion(VersionUtils.getNextMajorRev(version));
                    }else{
                        oxoVersionSnapshotAggr.setVersion(version+".1");
                    }
                }

            }
        } else {
            if (StringUtils.equals(type, OxoSnapshotEnum.FORMAL.getCode())) {
                oxoVersionSnapshotAggr.setVersion(ConfigConstants.VERSION_AA);
            } else {
                oxoVersionSnapshotAggr.setVersion(ConfigConstants.VERSION_AA_1);
            }
        }

        return oxoVersionSnapshotAggr;
    }

    @Override
    public OxoVersionSnapshotAggr queryOxoInfoByModelAndVersion(String modelCode, String version) {
        List<OxoVersionSnapshotAggr> oxoVersionSnapshots =
                oxoVersionSnapshotRepository.queryOxoVersionSnapshotByModelCode(modelCode, version, null);

        if (CollectionUtils.isNotEmpty(oxoVersionSnapshots)) {
            return oxoVersionSnapshots.get(0);
        }
        return null;
    }


    @Override
    public void checkBaseVehicleReleased(String modelCode) {
        if (CollectionUtils.isNotEmpty(oxoVersionSnapshotRepository.queryBomsOxoVersionSnapshotsByModel(modelCode))) {
            throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_ALREADY_RELEASED);
        }
    }

    @Override
    public OxoListQry resolveSnapShot(String oxoSnapShot) {
        return JSON.parseObject(GZIPUtils.uncompress(oxoSnapShot), OxoListQry.class);
    }

}
