package com.nio.ngfs.plm.bom.configuration.application.query.feature.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaozhou.tu
 * @date 2023/7/19
 */
public class FeatureLibraryQueryUtil {

    /**
     * 构建Feature Library树
     *
     * @param entityList entityList
     * @return Feature Library树
     */
    public static List<BomsFeatureLibraryEntity> buildFeatureLibraryTree(List<BomsFeatureLibraryEntity> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return Lists.newArrayList();
        }
        List<BomsFeatureLibraryEntity> groupList = Lists.newArrayList();
        Map<String, List<BomsFeatureLibraryEntity>> featureListByGroup = Maps.newHashMap();
        Map<String, List<BomsFeatureLibraryEntity>> optionListByFeature = Maps.newHashMap();
        entityList.forEach(entity -> {
            if (Objects.equals(entity.getType(), FeatureTypeEnum.GROUP.getType())) {
                groupList.add(entity);
            } else if (Objects.equals(entity.getType(), FeatureTypeEnum.FEATURE.getType())) {
                featureListByGroup.computeIfAbsent(entity.getParentFeatureCode(), k -> Lists.newArrayList()).add(entity);
            } else if (Objects.equals(entity.getType(), FeatureTypeEnum.OPTION.getType())) {
                optionListByFeature.computeIfAbsent(entity.getParentFeatureCode(), k -> Lists.newArrayList()).add(entity);
            }
        });
        groupList.forEach(group -> {
            group.setChildren(featureListByGroup.get(group.getFeatureCode()));
            if (CollectionUtils.isNotEmpty(group.getChildren())) {
                group.getChildren().forEach(feature -> {
                    feature.setGroup(group.getFeatureCode());
                    feature.setChildren(optionListByFeature.get(feature.getFeatureCode()));
                    if (CollectionUtils.isNotEmpty(feature.getChildren())) {
                        feature.getChildren().forEach(option -> option.setGroup(group.getFeatureCode()));
                    }
                });
            }
        });
        return groupList;
    }

    /**
     * Feature Library树排序
     *
     * @return Feature Library树
     */
    public static List<BomsFeatureLibraryEntity> sortFeatureLibraryTree(List<BomsFeatureLibraryEntity> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        entityList.forEach(entity -> entity.setChildren(sortFeatureLibraryTree(entity.getChildren())));
        return entityList.stream().sorted(Comparator.comparing(BomsFeatureLibraryEntity::getFeatureCode)).toList();
    }

}
