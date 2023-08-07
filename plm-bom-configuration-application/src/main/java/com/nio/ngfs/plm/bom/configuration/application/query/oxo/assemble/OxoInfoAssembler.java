package com.nio.ngfs.plm.bom.configuration.application.query.oxo.assemble;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoOptionPackageEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;

import java.util.List;
import java.util.Objects;

/**
 * @author wangchao.wang
 */
public class OxoInfoAssembler {

    public static OxoRowsQry assembleOxoQry(OxoFeatureOptionAggr infoDo, String group) {
        OxoRowsQry qry = new OxoRowsQry();
        qry.setCatalog(infoDo.getCatalog());
        qry.setHeadId(infoDo.getId());
        qry.setDisplayName(infoDo.getDisplayName());
        qry.setChineseName(infoDo.getChineseName());
        qry.setRuleCheck(infoDo.getRuleCheck());
        qry.setFeatureCode(infoDo.getFeatureCode());
        qry.setGroup(group);
        qry.setComments(infoDo.getComment());
        qry.setIsSortDelete(infoDo.getSortDelete());
        return qry;
    }


    public static List<OxoEditCmd> buildOxoEditCmd(List<OxoOptionPackageAggr> entities, OxoFeatureOptionAggr oxoInfoDo, List<OxoHeadQry> oxoLists) {

        List<OxoOptionPackageAggr> rowEntities =
                entities.stream().filter(y -> Objects.equals(y.getFeatureOptionId(), oxoInfoDo.getId())).toList();

        List<OxoEditCmd> oxoEditCmds = Lists.newArrayList();

        oxoLists.forEach(modelYears -> {
            modelYears.getRegionInfos().forEach(regionInfos -> {
                regionInfos.getDriveHands().forEach(driveHandInfos -> {
                    driveHandInfos.getSalesVersionInfos().forEach(salesVersionInfo -> {

                        OxoOptionPackageAggr entity = rowEntities.stream().filter(head ->
                                Objects.equals(head.getBaseVehicleId(), salesVersionInfo.getHeadId())).findFirst().orElse(null);

                        if (Objects.nonNull(entity)) {
                            oxoEditCmds.add(convertPackageInfoEntity(entity, modelYears, regionInfos, driveHandInfos, salesVersionInfo));
                        }

                    });
                });
            });
        });

        return oxoEditCmds;

    }


    public static OxoEditCmd convertPackageInfoEntity(OxoOptionPackageAggr entity,
                                                      OxoHeadQry headQry, OxoHeadQry.RegionInfo regionInfo,
                                                      OxoHeadQry.DriveHandInfo driveHandInfo,
                                                      OxoHeadQry.SalesVersionInfo salesVersion) {
        OxoEditCmd cmd = new OxoEditCmd();
        cmd.setRowId(entity.getFeatureOptionId());
        cmd.setHeadId(entity.getBaseVehicleId());
        cmd.setPackageCode(entity.getPackageCode());
        cmd.setModelCode(headQry.getModelCode());
        cmd.setModelYear(headQry.getModelYear());
        cmd.setDescription(entity.getDescription());
        cmd.setDriveHandCode(driveHandInfo.getDriveHandCode());
        cmd.setRegionCode(regionInfo.getRegionCode());
        cmd.setSalesCode(salesVersion.getSalesCode());
        return cmd;

    }
}
