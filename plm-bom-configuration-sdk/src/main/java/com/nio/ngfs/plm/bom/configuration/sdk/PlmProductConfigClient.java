package com.nio.ngfs.plm.bom.configuration.sdk;

import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.ModelListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.ModelListRespDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@FeignClient(name = "plm-bom-configuration")
public interface PlmProductConfigClient {

    /**
     * Product Config车型列表
     *
     * @param qry 查询
     * @return 响应
     */
    @PostMapping("/productConfig/modelList")
    ResultInfo<List<ModelListRespDto>> modelList(ModelListQry qry);

}
