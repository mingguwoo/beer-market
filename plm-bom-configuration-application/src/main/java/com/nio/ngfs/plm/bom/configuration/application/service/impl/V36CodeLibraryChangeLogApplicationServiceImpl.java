package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.application.service.V36CodeLibraryChangeLogApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.event.V36CodeLibraryAttributeChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36codelibrarychangelog.V36CodeLibraryChangeLogAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36codelibrarychangelog.V36CodeLibraryChangeLogFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author xiaozhou.tu
 * @date 2023/9/20
 */
@Service
@RequiredArgsConstructor
public class V36CodeLibraryChangeLogApplicationServiceImpl implements V36CodeLibraryChangeLogApplicationService {

    @Override
    public List<V36CodeLibraryChangeLogAggr> buildV36CodeLibraryChangeLogAggr(V36CodeLibraryAttributeChangeEvent event) {
        if (event.getBeforeAggr() == null || event.getAfterAggr() == null) {
            return Lists.newArrayList();
        }
        List<V36CodeLibraryChangeLogAggr> changeLogAggrList = Lists.newArrayList();
        addChangeLog(changeLogAggrList, event.getBeforeAggr(), event.getAfterAggr(), V36CodeLibraryAggr::getDisplayName, ConfigConstants.V36_CODE_ATTRIBUTE_DISPLAY_NAME);
        addChangeLog(changeLogAggrList, event.getBeforeAggr(), event.getAfterAggr(), V36CodeLibraryAggr::getChineseName, ConfigConstants.V36_CODE_ATTRIBUTE_CHINESE_NAME);
        addChangeLog(changeLogAggrList, event.getBeforeAggr(), event.getAfterAggr(), V36CodeLibraryAggr::getSalesFeatureList, ConfigConstants.V36_CODE_ATTRIBUTE_SALES_FEATURE);
        addChangeLog(changeLogAggrList, event.getBeforeAggr(), event.getAfterAggr(), V36CodeLibraryAggr::getRemark, ConfigConstants.V36_CODE_ATTRIBUTE_REMARK);
        return changeLogAggrList;
    }

    private void addChangeLog(List<V36CodeLibraryChangeLogAggr> changeLogAggrList, V36CodeLibraryAggr before, V36CodeLibraryAggr after,
                              Function<V36CodeLibraryAggr, String> valueFunction, String attributeName) {
        String oldValue = valueFunction.apply(before);
        String newValue = valueFunction.apply(after);
        if (Objects.equals(oldValue, newValue)) {
            return;
        }
        changeLogAggrList.add(V36CodeLibraryChangeLogFactory.create(before.getId(), attributeName, oldValue, newValue));
    }

}
