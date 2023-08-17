package com.nio.ngfs.plm.bom.configuration.domain.service.productcontext;

/**
 * @author bill.wang
 * @date 2023/8/17
 */
public interface ProductContextDomainService {

    /**
     * 同步oxo，生成product context
     * @param oxoSnapShot
     */
    void addProductContext(String oxoSnapShot);
}
