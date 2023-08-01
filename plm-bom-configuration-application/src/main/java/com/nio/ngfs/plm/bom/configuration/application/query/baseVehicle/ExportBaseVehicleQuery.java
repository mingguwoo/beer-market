package com.nio.ngfs.plm.bom.configuration.application.query.baseVehicle;

import com.google.common.collect.Lists;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.DateUtils;
import com.nio.bom.share.utils.ExcelUtils;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.ExportBaseVehicleQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.QueryBaseVehicleQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.BaseVehicleRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.ExportFeatureLibraryQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.QueryFeatureLibraryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/1
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExportBaseVehicleQuery {
    private static final List<String> TITLE_LIST = Lists.newArrayList(
            "Base Vehicle Id", "Model Code", "Model Year",
            "Region Option Code","Region English Name", "Region Chinese Name",
            "Drive Hand", "Drive Hand English Name", "Drive Hand Chinese Name",
            "Sales Version", "Sales Version English Name", "Sales Version Chinese Name",
            "Status", "Maturity", "Create User","Create Time", "Update User", "Update Time"
    );


    private final QueryBaseVehicleQuery queryBaseVehicleQuery;

    public void execute(ExportBaseVehicleQry qry, HttpServletResponse response) {
        List<BaseVehicleRespDto> baseVehicleRespDtoList = queryBaseVehicleQuery.executeQuery(qry);
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             OutputStream output = response.getOutputStream()) {
            String fileName = "Base Vehicle-" + DateUtils.dateTimeNow("yyyyMMddHHmm") + ".xlsx";
            response.reset();
            response.setHeader(ConfigConstants.HEADER_CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            response.setHeader(ConfigConstants.HEADER_ACCESS_CONTROL_EXPOSE_HEADERS, ConfigConstants.HEADER_CONTENT_DISPOSITION);
            response.setContentType("application/octet-stream;charset=UTF-8");
            exportBaseVehicle(qry, baseVehicleRespDtoList, workbook);
            workbook.write(output);
        } catch (IOException e) {
            throw new BusinessException(ConfigErrorCode.EXCEL_DOWNLOAD_ERROR, e.getMessage());
        }
    }

    /**
     * Base Vehicle导出到Excel
     */
    private void exportBaseVehicle(ExportBaseVehicleQry qry, List<BaseVehicleRespDto> baseVehicleRespDtoList, XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.createSheet();
        configSheetStyle(sheet);
        setSheetTitle(workbook, sheet, qry);
        writeBaseVehicleRow(qry, baseVehicleRespDtoList, workbook, sheet);
    }

    /**
     * 设置Sheet样式
     */
    private void configSheetStyle(XSSFSheet sheet) {
        sheet.setDefaultColumnWidth(15);
    }

    /**
     * 设置标题
     */
    private void setSheetTitle(XSSFWorkbook workbook, XSSFSheet sheet, ExportBaseVehicleQry qry) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.SKY_BLUE.getIndex());
        int columnIndex = 0;
        XSSFRow row = sheet.createRow(0);
        for (String title : TITLE_LIST) {
            XSSFCell cell = row.createCell(columnIndex++);
            cell.setCellValue(title);
            cell.setCellStyle(style);
        }
    }

    /**
     * 写Feature和Option行数据
     */
    private void writeBaseVehicleRow(ExportBaseVehicleQry qry, List<BaseVehicleRespDto> baseVehicleRespDtoList, XSSFWorkbook workbook, XSSFSheet sheet) {
        int rowIndex = 1;
        XSSFCellStyle featureCellStyle = createBaseVehicleCellStyle(workbook);
        for (BaseVehicleRespDto baseVehicle : baseVehicleRespDtoList) {
            createBaseVehicleRow(qry,baseVehicle,sheet, rowIndex++, featureCellStyle);
        }
    }

    /**
     * 创建BaseVehicle样式
     */
    private XSSFCellStyle createBaseVehicleCellStyle(XSSFWorkbook workbook) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        return cellStyle;
    }

    /**
     * 创建BaseVehicle行
     */
    private void createBaseVehicleRow(ExportBaseVehicleQry qry, BaseVehicleRespDto baseVehicle, XSSFSheet sheet, int rowIndex, XSSFCellStyle cellStyle) {
        int columnIndex = -1;
        XSSFRow row = sheet.createRow(rowIndex);
        createCell(row, ++columnIndex, baseVehicle.getBaseVehicleId(), cellStyle);
        createCell(row, ++columnIndex, baseVehicle.getModelCode(), cellStyle);
        createCell(row, ++columnIndex, baseVehicle.getModelYear(), cellStyle);
        createCell(row, ++columnIndex, baseVehicle.getRegionOptionCode(), cellStyle);
        createCell(row, ++columnIndex, baseVehicle.getRegionEn(), cellStyle);
        createCell(row, ++columnIndex, baseVehicle.getRegionCn(), cellStyle);
        createCell(row, ++columnIndex, baseVehicle.getDriveHand(), cellStyle);
        createCell(row, ++columnIndex, baseVehicle.getDriveEn(), cellStyle);
        createCell(row, ++columnIndex, baseVehicle.getDriveCn(), cellStyle);
        createCell(row, ++columnIndex, baseVehicle.getSalesVersion(), cellStyle);
        createCell(row, ++columnIndex, baseVehicle.getSalesVersionEn(), cellStyle);
        createCell(row, ++columnIndex, baseVehicle.getSalesVersionCn(), cellStyle);
        createCell(row, ++columnIndex, baseVehicle.getStatus(), cellStyle);
        createCell(row, ++columnIndex, baseVehicle.getMaturity(), cellStyle);
        createCell(row, ++columnIndex, baseVehicle.getCreateUser(), cellStyle);
        createCell(row, ++columnIndex, baseVehicle.getCreateTime(), cellStyle);
        createCell(row, ++columnIndex, baseVehicle.getUpdateUser(), cellStyle);
        createCell(row, ++columnIndex, baseVehicle.getUpdateTime(), cellStyle);
    }

    /**
     * 创建BaseVehicle单元格
     */
    private void createCell(XSSFRow row, int columnIndex, String value, XSSFCellStyle cellStyle) {
        XSSFCell cell = row.createCell(columnIndex);
        cell.setCellValue(value);
        cell.setCellType(CellType.STRING);
        if (cellStyle != null) {
            cell.setCellStyle(cellStyle);
        }
    }
}
