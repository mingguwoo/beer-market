package com.nio.ngfs.plm.bom.configuration.application.query.productconfig.assemble;

import com.nio.ngfs.plm.bom.configuration.common.util.ModelYearComparator;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsModelYearConfigEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.ModelListRespDto;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
public class ProductConfigAssembler {

    public static ModelListRespDto assemble(String model, List<BomsModelYearConfigEntity> modelYearConfigEntityList) {
        ModelListRespDto respDto = new ModelListRespDto();
        respDto.setModel(model);
        respDto.setModelYearList(modelYearConfigEntityList.stream().map(BomsModelYearConfigEntity::getModelYear)
                .sorted(new ModelYearComparator()).toList());
        return respDto;
    }

}
