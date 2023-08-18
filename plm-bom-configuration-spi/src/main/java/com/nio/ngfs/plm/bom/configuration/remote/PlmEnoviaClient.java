package com.nio.ngfs.plm.bom.configuration.remote;

import com.nio.ngfs.plm.bom.configuration.remote.dto.common.PlmEnoviaResult;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.PlmDeletePcDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.PlmModifyPcDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.PlmSyncProductConfigurationDto;
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
     * @return 结果
     */
    @PostMapping("/resources/NioPlm/configuration/syncFeatureOption")
    PlmEnoviaResult<Object> syncFeatureOption(PlmFeatureOptionSyncDto dto);

    /**
     * 同步新增PC到3DE
     *
     * @param dto 同步数据
     * @return 结果
     */
    @PostMapping("/resources/NioPlm/configuration/syncProductConfiguration")
    PlmEnoviaResult<Object> syncProductConfiguration(PlmSyncProductConfigurationDto dto);

    /**
     * 同步修改PC到3DE
     *
     * @param dto 同步数据
     * @return 结果
     */
    @PostMapping("/resources/NioPlm/configuration/modifyPc")
    PlmEnoviaResult<Object> modifyPc(PlmModifyPcDto dto);

    /**
     * 同步删除PC到3DE
     *
     * @param dto 同步数据
     * @return 结果
     */
    @PostMapping("/resources/NioPlm/configuration/deletePc")
    PlmEnoviaResult<Object> deletePc(PlmDeletePcDto dto);

}
