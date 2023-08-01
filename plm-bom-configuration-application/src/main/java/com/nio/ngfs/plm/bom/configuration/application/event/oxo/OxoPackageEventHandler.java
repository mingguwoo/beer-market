package com.nio.ngfs.plm.bom.configuration.application.event.oxo;

import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.event.OxoPackageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoPackageEventHandler implements EventHandler<OxoPackageEvent> {



    @Override
    @Async("commonThreadPool")
    public void onApplicationEvent(OxoPackageEvent event) {


        //oxoDomainService.insert






    }
}
