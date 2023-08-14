package com.nio.ngfs.plm.bom.configuration.sdk;

import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.AddProductContextCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.GetProductContextQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.AddProductContextRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.GetProductContextRespDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
@FeignClient(name = "plm-bom-configuration")
public interface PlmProductContextClient {

    @PostMapping("/productContext/addProductContext")
    ResultInfo<AddProductContextRespDto> addProductContext(AddProductContextCmd cmd);

    @PostMapping("/productContext/getProductContext")
    ResultInfo<GetProductContextRespDto> getProductContext(GetProductContextQry qry);
}
