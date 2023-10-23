package com.nio.ngfs.plm.bom.configuration.sdk;

import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.GetProductContextQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.QueryFeatureOptionQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.QueryProductContextOptionsQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.GetProductContextRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ProductContextOptionsRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.QueryFeatureOptionRespDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
@FeignClient(name = "plm-bom-configuration")
public interface PlmProductContextClient {

    @PostMapping("/productContext/getProductContext")
    ResultInfo<GetProductContextRespDto> getProductContext(GetProductContextQry qry);

    @PostMapping("/productContext/queryProductContextOptions")
    ResultInfo<ProductContextOptionsRespDto> queryProductContextOptions(QueryProductContextOptionsQry qry);

    /**
     * 查询Feature/Option行
     *
     * @param qry 查询
     * @return 结果
     */
    @PostMapping("/productContext/queryFeatureOption")
    ResultInfo<List<QueryFeatureOptionRespDto>> queryFeatureOption(QueryFeatureOptionQry qry);

}
