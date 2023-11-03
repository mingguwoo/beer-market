package com.nio.ngfs.plm.bom.configuration.application.query.productcontext.assemble;

import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.QueryFeatureOptionRespDto;

/**
 * @author bill.wang
 * @date 2023/8/11
 */
public class ProductContextFeatureAssembler {

    public static QueryFeatureOptionRespDto assemble(BomsFeatureLibraryEntity featureLibraryEntity) {
        QueryFeatureOptionRespDto respDto = new QueryFeatureOptionRespDto();
        respDto.setFeatureCode(featureLibraryEntity.getFeatureCode());
        respDto.setChineseName(featureLibraryEntity.getChineseName());
        respDto.setDisplayName(featureLibraryEntity.getDisplayName());
        respDto.setCatalog(featureLibraryEntity.getCatalog());
        return respDto;
    }

    public static QueryFeatureOptionRespDto.OptionItemDto assembleOption(BomsFeatureLibraryEntity featureLibraryEntity) {
        QueryFeatureOptionRespDto.OptionItemDto optionItemDto = new QueryFeatureOptionRespDto.OptionItemDto();
        optionItemDto.setOptionCode(featureLibraryEntity.getFeatureCode());
        optionItemDto.setChineseName(featureLibraryEntity.getChineseName());
        optionItemDto.setDisplayName(featureLibraryEntity.getDisplayName());
        return optionItemDto;
    }

}
