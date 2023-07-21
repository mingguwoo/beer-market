package com.nio.ngfs.plm.bom.configuration.application.query.oxo;

import com.nio.ngfs.plm.bom.configuration.application.query.Query;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.domainobject.OxoInfoDo;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsBasicVehicleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureChangeLogDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoRowInfoDao;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 *
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoInfoQuery implements Query<OxoBaseCmd, OxoListQry> {


    private final BomsOxoRowInfoDao bomsOxoRowInfoDao;


    private final BomsBasicVehicleDao basicVehicleDao;





    @Override
    public OxoListQry execute(OxoBaseCmd oxoListQry) {

        String modelCode= oxoListQry.getModelCode();


        List<OxoInfoDo> oxoInfoDos =bomsOxoRowInfoDao.queryFeatureListsByModel(modelCode);



        return null;
    }
}
