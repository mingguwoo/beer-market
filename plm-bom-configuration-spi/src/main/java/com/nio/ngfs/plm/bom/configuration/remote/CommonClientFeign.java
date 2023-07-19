package com.nio.ngfs.plm.bom.configuration.remote;


import com.nio.bom.share.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wangchao.wang
 */
@FeignClient(name = "plm-portal-middle-common")
public interface CommonClientFeign {

    /**
     * 获取matrix rule
     * @param bizData
     * @return
     */
    @PostMapping(value = "/matrix/getMatrixRuleValuesByAbscissaOrOrdinate")
    Result getMatrixRuleValuesByAbscissaOrOrdinate(@RequestParam("bizData") String bizData);
}
