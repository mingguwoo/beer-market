package com.nio.ngfs.plm.bom.configuration.application.query.productconfig.assemble;

import com.nio.ngfs.plm.bom.configuration.common.util.ModelYearComparator;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsModelYearConfigEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.GetBasedOnPcListRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.GetModelListRespDto;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
public class ProductConfigAssembler {

    public static GetModelListRespDto assemble(String model, List<BomsModelYearConfigEntity> entityList) {
        GetModelListRespDto respDto = new GetModelListRespDto();
        respDto.setModel(model);
        respDto.setModelYearList(entityList.stream().map(BomsModelYearConfigEntity::getModelYear)
                .sorted(new ModelYearComparator()).toList());
        return respDto;
    }

    public static GetBasedOnPcListRespDto assemble(BomsProductConfigEntity entity) {
        GetBasedOnPcListRespDto respDto = new GetBasedOnPcListRespDto();
        respDto.setPcId(entity.getPcId());
        respDto.setPcName(entity.getName());
        return respDto;
    }

}
