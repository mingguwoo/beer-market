package com.nio.ngfs.plm.bom.configuration.remote;

import com.nio.ngfs.plm.bom.configuration.remote.dto.common.PlmEnoviaResult;
import com.nio.ngfs.plm.bom.configuration.remote.dto.feature.PlmFeatureOptionSyncDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author xiaozhou.tu
 * @date 2023/7/25
 */
@FeignClient(url = "${url.plm.enovia}", name = "plm-enovia-22x")
public interface PlmEnoviaClient {

    /**
     * 同步Feature/Option到3DE
     *
     * @param dto 同步数据
     * @return 结构
     */
    @PostMapping("/resources/NioPlm/configuration/syncFeatureOption")
    PlmEnoviaResult<Object> syncFeatureOption(PlmFeatureOptionSyncDto dto);

}
