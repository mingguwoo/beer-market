package com.nio.ngfs.plm.bom.configuration.application.query.oxo;


import com.nio.ngfs.plm.bom.configuration.application.query.oxo.common.OxoExcelUtil;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoQueryApplicationService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
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
public class OxoInfoExportQuery {


    private final OxoQueryApplicationService oxoQueryApplicationService;

    /**
     * 页面导出
     *
     * @param oxoBaseCmd
     * @return
     */
    public void execute(OxoBaseCmd oxoBaseCmd, HttpServletRequest request, HttpServletResponse response) {

        // 车型
        String modelCode = oxoBaseCmd.getModelCode();

        // 版本
        String version = oxoBaseCmd.getVersion();

        //查询oxo  info数据
        OxoListQry qry = oxoQueryApplicationService.queryOxoInfoByModelCode(modelCode, version,false);


        //导出数据
        OxoExcelUtil.oxoExport(qry, modelCode, version, response, request);


    }
}
