package com.nio.ngfs.plm.bom.configuration.application.event.v36code;

import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.application.service.V36CodeLibraryChangeLogApplicationService;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.event.V36CodeLibraryAttributeChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36codelibrarychangelog.V36CodeLibraryChangeLogAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36codelibrarychangelog.V36CodeLibraryChangeLogRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/9/20
 */
@Component
@RequiredArgsConstructor
public class V36CodeLibraryChangeLogHandler implements EventHandler<V36CodeLibraryAttributeChangeEvent> {

    private final V36CodeLibraryChangeLogRepository v36CodeLibraryChangeLogRepository;
    private final V36CodeLibraryChangeLogApplicationService v36CodeLibraryChangeLogApplicationService;

    @Async("eventExecutor")
    @Override
    public void onApplicationEvent(@NotNull V36CodeLibraryAttributeChangeEvent event) {
        List<V36CodeLibraryChangeLogAggr> changeLogAggrList = v36CodeLibraryChangeLogApplicationService.buildV36CodeLibraryChangeLogAggr(event);
        v36CodeLibraryChangeLogRepository.batchSave(changeLogAggrList);
    }

}
