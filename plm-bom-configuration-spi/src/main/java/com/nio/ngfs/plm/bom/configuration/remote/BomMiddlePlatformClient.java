package com.nio.ngfs.plm.bom.configuration.remote;

import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.remote.dto.platform.ModelDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author luke.zhu
 * @date 07/11/2023
 */
@FeignClient(name = "plm-bom-middle-platform")
public interface BomMiddlePlatformClient {

    /**
     * 获取车型信息
     *
     * @param model 车型
     * @return ModelDto
     */
    @GetMapping("/model/search")
    ResultInfo<ModelDto> getModel(@RequestParam("model") String model);

    /**
     *  根据品牌获取车型列表
     * @param brand
     * @return
     */
    @GetMapping("/model/search/modelListByBrand")
    ResultInfo<List<ModelDto>> getModelListByBrand(@RequestParam("brand")  String brand);
}
