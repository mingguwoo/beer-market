package com.nio.ngfs.plm.bom.configuration.sdk;

import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.AddDigitCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.AddDigitRespDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@FeignClient(name = "plm-bom-configuration")
public interface PlmV36CodeLibraryClient {

    /**
     * 新增Digit
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/v36code/addDigit")
    ResultInfo<AddDigitRespDto> addDigit(AddDigitCmd cmd);

}
