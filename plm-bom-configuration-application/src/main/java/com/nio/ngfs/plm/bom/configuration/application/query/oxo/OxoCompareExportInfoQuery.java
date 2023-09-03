package com.nio.ngfs.plm.bom.configuration.application.query.oxo;


import com.nio.ngfs.plm.bom.configuration.application.query.oxo.common.OxoCompareExportUtil;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoQueryApplicationService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoCompareQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OxoCompareExportInfoQuery {



    private final OxoQueryApplicationService oxoQueryApplicationService;


    /**
     * 对比导出
     * @param compareCmd
     * @param request
     * @param response
     */
    public void execute(OxoCompareQry compareCmd, HttpServletRequest request, HttpServletResponse response) {

        OxoListRespDto OxoListRespDto= oxoQueryApplicationService.compareVersion(compareCmd);

        OxoCompareExportUtil.compareExport(OxoListRespDto,compareCmd, response, request);

    }

}
