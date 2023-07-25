package com.nio.ngfs.plm.bom.configuration.application.event.oxo;

import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.event.OxoPackageEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoPackageEventHandler implements EventHandler<OxoPackageEvent> {

    private final OxoDomainService oxoDomainService;


    @Override
    @Async("commonThreadPool")
    public void onApplicationEvent(OxoPackageEvent event) {


        //oxoDomainService.insert






    }
}
