package com.nio.ngfs.plm.bom.configuration.sdk;

import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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

    /**
     * 编辑Digit
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/v36code/editDigit")
    ResultInfo<EditDigitRespDto> editDigit(EditDigitCmd cmd);

    /**
     * 新增Option
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/v36code/addOption")
    ResultInfo<AddOptionRespDto> addOption(AddOptionCmd cmd);

    /**
     * 编辑Option
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/v36code/editOption")
    ResultInfo<EditOptionRespDto> editOption(EditOptionCmd cmd);


    @PostMapping("/v36code/queryV36CodeLibrary")
    ResultInfo<QueryV36CodeLibraryRespDto> queryV36CodeLibrary(QueryV36CodeLibraryQry qry);

    @PostMapping("/v36Code/queryV36DigitCode")
    ResultInfo<List<String>> queryV36DigitCode(QueryV36DigitCodeQry qry);

    @PostMapping("/v36Code/queryV36CodeLibraryChangeLog")
    ResultInfo<List<QueryV36CodeLibraryChangeLogRespDto>> queryV36CodeLibraryChangeLog(QueryV36CodeLibraryChangeLogQry qry);
}
