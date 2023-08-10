package com.nio.ngfs.plm.bom.configuration.sdk;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
@FeignClient(name = "plm-bom-configuration")
public interface PlmProductContextConfigClient {


}
