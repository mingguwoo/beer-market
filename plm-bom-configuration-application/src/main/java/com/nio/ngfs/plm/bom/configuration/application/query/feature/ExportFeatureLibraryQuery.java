package com.nio.ngfs.plm.bom.configuration.application.query.feature;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.ExportFeatureLibraryQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.QueryFeatureLibraryDto;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/25
 */
@Component
@RequiredArgsConstructor
public class ExportFeatureLibraryQuery {

    private static final List<String> TITLE_LIST = Lists.newArrayList(
            "Feature Code"
    );

    private final QueryFeatureLibraryQuery queryFeatureLibraryQuery;

    public void execute(ExportFeatureLibraryQry qry, HttpServletResponse response) {
        List<QueryFeatureLibraryDto> featureLibraryDtoList = queryFeatureLibraryQuery.execute(qry);
        System.out.println(featureLibraryDtoList.size());
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             OutputStream output = response.getOutputStream()) {
            XSSFSheet sheet = workbook.createSheet();
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + "1111.xlsx" + "\"");
            response.setContentType("application/octet-stream;charset=UTF-8");
            workbook.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置标题
     */
    private void setTitle(XSSFWorkbook workbook, XSSFSheet sheet, ExportFeatureLibraryQry qry) {
        XSSFRow rowTitle = sheet.createRow(0);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.SKY_BLUE.getIndex());
        int index = 0;
        for (String title : TITLE_LIST) {
            XSSFCell cell = rowTitle.createCell(index++);
            cell.setCellValue(title);
            cell.setCellStyle(style);
        }
    }

}
