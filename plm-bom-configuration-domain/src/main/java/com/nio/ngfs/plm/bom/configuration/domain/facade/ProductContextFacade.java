package com.nio.ngfs.plm.bom.configuration.domain.facade;

import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.event.SyncProductContextEvent;

/**
 * @author bill.wang
 * @date 2023/8/30
 */
public interface ProductContextFacade {

    void syncAddProductContextToEnovia(SyncProductContextEvent event);
}
