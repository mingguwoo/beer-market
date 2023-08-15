package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
public interface ProductContextApplicationService {

    void addProductContext(OxoListQry oxoListQry);

}
