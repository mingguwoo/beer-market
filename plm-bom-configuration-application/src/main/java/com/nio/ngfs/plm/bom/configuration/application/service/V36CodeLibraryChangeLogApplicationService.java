package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.event.V36CodeLibraryAttributeChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36codelibrarychangelog.V36CodeLibraryChangeLogAggr;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/9/20
 */
public interface V36CodeLibraryChangeLogApplicationService {

    /**
     * 构建V36CodeLibrary变更log
     *
     * @param event 事件
     * @return V36CodeLibraryChangeLogAggr列表
     */
    List<V36CodeLibraryChangeLogAggr> buildV36CodeLibraryChangeLogAggr(V36CodeLibraryAttributeChangeEvent event);

}
