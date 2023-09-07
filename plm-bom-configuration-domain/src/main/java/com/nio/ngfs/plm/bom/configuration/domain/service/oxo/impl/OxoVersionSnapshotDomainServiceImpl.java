package com.nio.ngfs.plm.bom.configuration.domain.service.oxo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nio.bom.share.utils.GZIPUtils;
import com.nio.bom.share.utils.VersionUtils;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.enums.OxoSnapshotEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoVersionSnapshotDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;
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


        if (CollectionUtils.isEmpty(oxoVersionSnapshots)) {

            if (StringUtils.equals(type, OxoSnapshotEnum.FORMAL.getCode())) {
                oxoVersionSnapshotAggr.setVersion(ConfigConstants.VERSION_AA);
            } else {
                oxoVersionSnapshotAggr.setVersion(ConfigConstants.VERSION_AA_1);
            }
        } else {
            List<OxoVersionSnapshotAggr> oxoSortedVersionSnapshot = oxoVersionSnapshots.stream()
                    .sorted(Comparator.comparing(OxoVersionSnapshotAggr::getCreateTime).reversed()).toList();


            //版本
            String version = oxoSortedVersionSnapshot.get(0).getVersion();

            if (version.contains(ConfigConstants.REG_DOT)) {
                if (StringUtils.equals(type, OxoSnapshotEnum.INFORMAL.getCode())) {
                    oxoVersionSnapshotAggr.setVersion(VersionUtils.getNextMajorRev(version));
                } else {

                    String nextVersion = VersionUtils.getMajorRev(version);

                    if(oxoSortedVersionSnapshot.stream().anyMatch(x-> StringUtils.equals(x.getVersion(),nextVersion))) {
                        oxoVersionSnapshotAggr.setVersion(VersionUtils.findNextRev(nextVersion));
                    }else{
                        oxoVersionSnapshotAggr.setVersion(nextVersion);
                    }
                }
            } else {
                if (StringUtils.equals(type, OxoSnapshotEnum.INFORMAL.getCode())) {
                    oxoVersionSnapshotAggr.setVersion(VersionUtils.findNextRev(version) + ".1");
                } else {
                    oxoVersionSnapshotAggr.setVersion(VersionUtils.findNextRev(version));
                }
            }

            List<OxoVersionSnapshotAggr> formalVersionSnapshots = oxoSortedVersionSnapshot.stream().filter(x -> StringUtils.equals(x.getType(),
                    OxoSnapshotEnum.FORMAL.getCode())).sorted(Comparator.comparing(OxoVersionSnapshotAggr::getVersion).reversed()).toList();

            if (CollectionUtils.isNotEmpty(formalVersionSnapshots)) {
                oxoVersionSnapshotAggr.setPreVersion(formalVersionSnapshots.get(0).getVersion());
                oxoVersionSnapshotAggr.setPreOxoSnapshot(formalVersionSnapshots.get(0).getOxoSnapshot());
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
    public OxoListRespDto resolveSnapShot(String oxoSnapShot) {
        return JSONObject.parseObject(JSONArray.parse(GZIPUtils.uncompress(oxoSnapShot)).toString(), OxoListRespDto.class);
    }

}