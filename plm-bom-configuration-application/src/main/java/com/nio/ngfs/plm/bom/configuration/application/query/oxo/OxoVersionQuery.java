package com.nio.ngfs.plm.bom.configuration.application.query.oxo;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.application.query.Query;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.facade.AdministratorDetailFacade;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.enums.OxoSnapshotEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoVersionSnapshotDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoVersionSnapshotEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoVersionQuery implements Query<OxoBaseCmd, List<String>> {

    private final BomsOxoVersionSnapshotDao bomsOxoVersionSnapshotDao;

    @Override
    public List<String> execute(OxoBaseCmd oxoBaseCmd) {
        String modelCode = oxoBaseCmd.getModelCode();

        // 获取人员角色
        List<String> roleNames = oxoBaseCmd.getPermissionPoints();
        // 查询oxo版本
        List<BomsOxoVersionSnapshotEntity> oxoVersionSnapshotAggrs =
                bomsOxoVersionSnapshotDao.queryBomsOxoVersionSnapshotsByModelOrVersionOrType(modelCode, null, null);

        if (CollectionUtils.isEmpty(oxoVersionSnapshotAggrs) && roleNames.contains(ConfigConstants.CONFIG_ADMIN)) {
            return Lists.newArrayList(ConfigConstants.WORKING);
        } else if (CollectionUtils.isEmpty(oxoVersionSnapshotAggrs)) {
            return Lists.newArrayList();
        }

        List<BomsOxoVersionSnapshotEntity> revisions = Lists.newArrayList();

        List<BomsOxoVersionSnapshotEntity> roleRevisions = oxoVersionSnapshotAggrs.stream()
                .filter(x -> StringUtils.equals(x.getType(), OxoSnapshotEnum.FORMAL.getCode()))
                .toList();

        /**下拉框展示的值默认为Working（即展示OXO Working版本数据），下拉框可选值：Working版本、Formal版本、最新Informal版本*/
        if (roleNames.contains(ConfigConstants.CONFIG_ADMIN)) {
            //最新Informal版本
            BomsOxoVersionSnapshotEntity snapshotEntity = oxoVersionSnapshotAggrs.stream()
                    .filter(x -> StringUtils.equals(x.getType(), OxoSnapshotEnum.INFORMAL.getCode()))
                    .max(Comparator.comparing(BomsOxoVersionSnapshotEntity::getCreateTime)).orElse(null);
            if (Objects.nonNull(snapshotEntity)) {
                revisions.add(snapshotEntity);
            }
        }

        revisions.addAll(roleRevisions);

        List<String> revs = Lists.newArrayList();

        if (roleNames.contains(ConfigConstants.CONFIG_ADMIN)) {
            revs.add(ConfigConstants.WORKING);
        }

        revs.addAll(revisions.stream().sorted(Comparator.comparing(BomsOxoVersionSnapshotEntity::getCreateTime).reversed())
                .map(BomsOxoVersionSnapshotEntity::getVersion).distinct().toList());
        return revs;
    }
}
