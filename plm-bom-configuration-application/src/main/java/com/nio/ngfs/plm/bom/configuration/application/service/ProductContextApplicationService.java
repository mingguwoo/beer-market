package com.nio.ngfs.plm.bom.configuration.application.service;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
public interface ProductContextApplicationService {

    /**
     * 同步oxo，生成product context
     * @param oxoSnapShot
     */
    void addProductContext(String oxoSnapShot);


}
