package com.nio.ngfs.plm.bom.configuration.remote;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author luke.zhu
 * @date 07/11/2023
 */
@FeignClient(url = "${url.middle}", name = "plm-bom-middle-platform")
public interface BomMiddlePlatformClient {


}
