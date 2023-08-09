package com.nio.ngfs.plm.bom.configuration.application.query.oxo;


import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.application.query.oxo.common.OxoExcelUtil;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoFeatureOptionApplicationService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;


/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OxoInfoExportQuery {


    private final OxoFeatureOptionApplicationService oxoFeatureOptionApplicationService;


    /**
     * Excel标题列表
     */
    private static final List<String> TITLE_LIST = Lists.newArrayList(
            "Feature Code", "Display Name", "Chinese Name", "Group",
            "Comments"
    );

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
        OxoListQry qry = oxoFeatureOptionApplicationService.queryOxoInfoByModelCode(modelCode, version);


        OxoExcelUtil.oxoExport(qry, modelCode, version, response, request);


    }
}
