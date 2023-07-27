package com.nio.ngfs.plm.bom.configuration.application.query.oxo;

import com.nio.ngfs.plm.bom.configuration.application.query.Query;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.facade.AdministratorDetailFacade;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.enums.OxoSnapshotEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.repository.OxoRepository;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoVersionQuery implements Query<OxoBaseCmd, List<String>> {

    private final AdministratorDetailFacade administratorDetailFacade;

    private OxoRepository oxoRepository;

    @Override
    public List<String> execute(OxoBaseCmd oxoBaseCmd) {
        String userName = oxoBaseCmd.getUserName();
        String modelCode = oxoBaseCmd.getModelCode();

        // 获取人员角色
        List<String> roleNames = administratorDetailFacade.queryRoleNamesByUserName(userName);

        // 查询oxo版本
        List<OxoVersionSnapshotAggr> oxoVersionSnapshotAggrs =
                oxoRepository.queryOxoVersionSnapshotLists(modelCode);

        if (CollectionUtils.isEmpty(oxoVersionSnapshotAggrs)) {
            return Lists.newArrayList();
        }

        List<String> revisions = Lists.newArrayList();

        List<String> roleRevisions = oxoVersionSnapshotAggrs.stream()
                .filter(x -> StringUtils.equals(x.getType(), OxoSnapshotEnum.FORMAL.getCode()))
                .map(OxoVersionSnapshotAggr::getVersion).toList().stream()
                .sorted(Comparator.reverseOrder()).toList();

        /**下拉框展示的值默认为Working（即展示OXO Working版本数据），下拉框可选值：Working版本、Formal版本、最新Informal版本*/
        if (roleNames.contains(ConfigConstants.CONFIG_ADMIN)) {

            revisions.add(ConfigConstants.WORKING);

            // Formal版本
            revisions.addAll(roleRevisions);

            //最新Informal版本
            revisions.add(oxoVersionSnapshotAggrs.stream()
                    .filter(x -> StringUtils.equals(x.getType(), OxoSnapshotEnum.INFORMAL.getCode()))
                    .map(OxoVersionSnapshotAggr::getVersion).toList().stream().min(Comparator.reverseOrder()).orElse(StringUtils.EMPTY));
        }

        /**下拉框展示的值默认为最新Formal发布版本（即展示OXO最新正式发布版本数据），
         * 下拉框可选值为所有Formal Version(不包含Working版本和Informal版本)*/
        else if (roleNames.contains(ConfigConstants.CONFIG_USER) ||
                roleNames.contains(ConfigConstants.FEATURE_LIBRARY)) {
            revisions.addAll(roleRevisions);
        }

        return revisions.stream().filter(StringUtils::isNotBlank).distinct().toList();
    }
}
