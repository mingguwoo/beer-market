package com.nio.ngfs.plm.bom.configuration.application.event.productcontext;

import com.nio.ngfs.plm.bom.configuration.application.service.ProductContextApplicationService;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.event.OxoVersionReleaseEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoVersionSnapshotDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author bill.wang
 * @date 2023/8/15
 */
@Component
@RequiredArgsConstructor
public class ProductContextSyncOxoHandler {

    private final OxoVersionSnapshotDomainService oxoVersionSnapshotDomainService;
    private final ProductContextApplicationService productContextApplicationService;

    @EventListener
    @Async("commonThreadPool")
    public void oOxonVersionReleaseEvent(@NotNull OxoVersionReleaseEvent oxoVersionReleaseEvent){
        OxoListQry oxoListQry = oxoVersionSnapshotDomainService.resolveSnapShot(oxoVersionReleaseEvent.getOxoSnapshot());
        productContextApplicationService.addProductContext(oxoListQry);
    }
}
