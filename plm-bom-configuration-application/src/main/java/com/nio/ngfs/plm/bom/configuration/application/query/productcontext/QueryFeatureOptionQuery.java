package com.nio.ngfs.plm.bom.configuration.application.query.productcontext;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.enums.ProductContextFeatureEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductContextFeatureDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextFeatureEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.QueryFeatureOptionQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.QueryFeatureOptionRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author xiaozhou.tu
 * @date 2023/10/19
 */
@Component
@RequiredArgsConstructor
public class QueryFeatureOptionQuery extends AbstractQuery<QueryFeatureOptionQry, List<QueryFeatureOptionRespDto>> {

    private final BomsProductContextFeatureDao bomsProductContextFeatureDao;
    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;

    @Override
    protected List<QueryFeatureOptionRespDto> executeQuery(QueryFeatureOptionQry qry) {
        // 查询Product Context的Feature/Option行
        List<BomsProductContextFeatureEntity> featureEntityList = bomsProductContextFeatureDao.queryByModelCode(qry.getModel());
        if (CollectionUtils.isEmpty(featureEntityList)) {
            return Collections.emptyList();
        }
        List<BomsFeatureLibraryEntity> featureLibraryEntityList = bomsFeatureLibraryDao.queryAll();
        return buildRespDto(featureEntityList, featureLibraryEntityList, qry);
    }

    private List<QueryFeatureOptionRespDto> buildRespDto(List<BomsProductContextFeatureEntity> featureEntityList, List<BomsFeatureLibraryEntity> featureLibraryEntityList,
                                                         QueryFeatureOptionQry qry) {
        Map<String, BomsFeatureLibraryEntity> featureLibraryEntityMap = LambdaUtil.toKeyMap(featureLibraryEntityList, BomsFeatureLibraryEntity::getFeatureCode);
        Map<String, List<String>> optionListMap = Maps.newHashMap();
        featureEntityList.stream().filter(i -> Objects.equals(i.getType(), ProductContextFeatureEnum.OPTION.getType())).forEach(i -> {
            BomsFeatureLibraryEntity featureLibraryEntity = featureLibraryEntityMap.get(i.getFeatureCode());
            if (featureLibraryEntity == null) {
                throw new BusinessException(ConfigErrorCode.FEATURE_OPTION_NOT_EXISTS);
            }
            optionListMap.computeIfAbsent(featureLibraryEntity.getParentFeatureCode(), k -> Lists.newArrayList()).add(i.getFeatureCode());
        });
        List<QueryFeatureOptionRespDto> respDtoList = featureEntityList.stream().filter(i -> Objects.equals(i.getType(), ProductContextFeatureEnum.FEATURE.getType())).map(i -> {
                    BomsFeatureLibraryEntity featureLibraryEntity = featureLibraryEntityMap.get(i.getFeatureCode());
                    if (featureLibraryEntity == null) {
                        throw new BusinessException(ConfigErrorCode.FEATURE_FEATURE_NOT_EXISTS);
                    }
                    if (StringUtils.isNotBlank(qry.getCatalog()) && !Objects.equals(qry.getCatalog(), featureLibraryEntity.getCatalog())) {
                        // Catalog不匹配
                        return null;
                    }
                    QueryFeatureOptionRespDto respDto = new QueryFeatureOptionRespDto();
                    respDto.setFeatureCode(i.getFeatureCode());
                    respDto.setOptionCodeList(optionListMap.get(i.getFeatureCode()));
                    if (CollectionUtils.isEmpty(respDto.getOptionCodeList())) {
                        return null;
                    }
                    return respDto;
                }).filter(Objects::nonNull)
                .sorted(Comparator.comparing(QueryFeatureOptionRespDto::getFeatureCode))
                .toList();
        // optionList排序
        respDtoList.forEach(feature -> feature.setOptionCodeList(feature.getOptionCodeList().stream().sorted(String::compareTo).toList()));
        return respDtoList;
    }

}
