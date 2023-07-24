package com.nio.ngfs.plm.bom.configuration.application.query.oxo.assemble;

import com.google.common.collect.Lists;
import com.nio.bom.share.utils.DateUtils;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.domainobject.OxoInfoDo;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureChangeLogEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoOptionPackageInfoEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.GetChangeLogListDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangchao.wang
 */
public class OxoInfoAssembler {


    public static OxoRowsQry assembleOxoQry(OxoInfoDo infoDo, String group) {
        OxoRowsQry qry = new OxoRowsQry();
        qry.setCatalog(infoDo.getCatalog());
        qry.setHeadId(infoDo.getId());
        qry.setDisplayName(infoDo.getDisplayName());
        qry.setChineseName(infoDo.getChineseName());
        qry.setRuleCheck(infoDo.getRuleCheck());
        qry.setFeatureCode(infoDo.getFeatureCode());
        qry.setGroup(group);
        qry.setComments(infoDo.getComments());
        qry.setIsSortDelete(infoDo.getSortDelete());
        return qry;
    }


    public static List<OxoEditCmd> buildOxoEditCmd(List<BomsOxoOptionPackageInfoEntity> entities, OxoInfoDo oxoInfoDo, List<OxoHeadQry> oxoLists) {


        List<BomsOxoOptionPackageInfoEntity> rowEntities =
                entities.stream().filter(y -> Objects.equals(y.getRowId(), oxoInfoDo.getId())).toList();

        List<OxoEditCmd> oxoEditCmds = Lists.newArrayList();

        oxoLists.forEach(modelYears -> {
            modelYears.getRegionInfos().forEach(regionInfos -> {
                regionInfos.getDriveHands().forEach(driveHandInfos -> {
                    driveHandInfos.getSalesVersionInfos().forEach(driveHand -> {

                        BomsOxoOptionPackageInfoEntity entity = rowEntities.stream().filter(head ->
                                Objects.equals(head.getBaseVehicleId(), driveHand.getHeadId())).findFirst().orElse(null);

                        if (Objects.nonNull(entity)) {
                            oxoEditCmds.add(convertPackageInfoEntity(entity));
                        }

                    });
                });

            });
        });

        return oxoEditCmds;

    }


    public static OxoEditCmd convertPackageInfoEntity(BomsOxoOptionPackageInfoEntity entity) {
        OxoEditCmd cmd = new OxoEditCmd();
        cmd.setRowId(entity.getRowId());
        cmd.setHeadId(entity.getBaseVehicleId());
        cmd.setPackageCode(entity.getPackageCode());
        return cmd;

    }


}
