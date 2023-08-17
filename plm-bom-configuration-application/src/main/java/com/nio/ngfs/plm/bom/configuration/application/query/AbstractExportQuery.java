package com.nio.ngfs.plm.bom.configuration.application.query;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author xiaozhou.tu
 * @date 2023/8/17
 */
public abstract class AbstractExportQuery {

    protected void export(HttpServletResponse response, String fileName, Consumer<XSSFWorkbook> workbookConsumer) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             OutputStream output = response.getOutputStream()) {
            response.reset();
            response.setHeader(CommonConstants.HEADER_CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            response.setHeader(CommonConstants.HEADER_ACCESS_CONTROL_EXPOSE_HEADERS, CommonConstants.HEADER_CONTENT_DISPOSITION);
            response.setContentType("application/octet-stream;charset=UTF-8");
            workbookConsumer.accept(workbook);
            workbook.write(output);
        } catch (IOException e) {
            throw new BusinessException(ConfigErrorCode.EXCEL_DOWNLOAD_ERROR, e.getMessage());
        }
    }

    /**
     * 设置Sheet样式
     */
    protected void configSheetStyle(XSSFSheet sheet) {
        // 默认列宽
        sheet.setDefaultColumnWidth(15);
    }

    /**
     * 设置标题
     */
    protected void setSheetTitle(XSSFWorkbook workbook, XSSFSheet sheet, List<String> titleList) {
        XSSFCellStyle style = workbook.createCellStyle();
        // 标题蓝底色
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.SKY_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        int columnIndex = 0;
        XSSFRow row = sheet.createRow(0);
        for (String title : titleList) {
            XSSFCell cell = row.createCell(columnIndex++);
            cell.setCellValue(title);
            cell.setCellStyle(style);
        }
    }

    /**
     * 创建单元格
     */
    protected void createCell(XSSFRow row, int columnIndex, String value, XSSFCellStyle cellStyle) {
        XSSFCell cell = row.createCell(columnIndex);
        cell.setCellValue(value);
        cell.setCellType(CellType.STRING);
        if (cellStyle != null) {
            cell.setCellStyle(cellStyle);
        }
    }

}
