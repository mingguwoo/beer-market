package com.nio.ngfs.plm.bom.configuration.application.query.feature;

import com.google.common.collect.Lists;
import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.DateUtils;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
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
 * 导出Feature Library到Excel
 *
 * @author xiaozhou.tu
 * @date 2023/7/25
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExportFeatureLibraryQuery {

    /**
     * Excel标题列表
     */
    private static final List<String> TITLE_LIST = Lists.newArrayList(
            "Feature Code", "Display Name", "Chinese Name", "Description", "Group",
            "Type", "Catalogue", "Requestor", "Creator", "Originated", "Update User",
            "Last Modified", "Status", "Model Year"
    );

    private final QueryFeatureLibraryQuery queryFeatureLibraryQuery;

    public void execute(ExportFeatureLibraryQry qry, HttpServletResponse response) {
        // 查询Feature Library列表
        qry.setRelatedModel(true);
        List<QueryFeatureLibraryDto> featureLibraryDtoList = queryFeatureLibraryQuery.execute(qry);
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             OutputStream output = response.getOutputStream()) {
            String fileName = "Feature Library-" + DateUtils.dateTimeNow("yyyyMMddHHmm") + ".xlsx";
            response.reset();
            response.setHeader(CommonConstants.HEADER_CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            response.setHeader(CommonConstants.HEADER_ACCESS_CONTROL_EXPOSE_HEADERS, CommonConstants.HEADER_CONTENT_DISPOSITION);
            response.setContentType("application/octet-stream;charset=UTF-8");
            exportFeatureLibrary(featureLibraryDtoList, workbook);
            workbook.write(output);
        } catch (IOException e) {
            throw new BusinessException(ConfigErrorCode.EXCEL_DOWNLOAD_ERROR, e.getMessage());
        }
    }

    /**
     * Feature Library导出到Excel
     */
    private void exportFeatureLibrary(List<QueryFeatureLibraryDto> featureLibraryDtoList, XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.createSheet();
        configSheetStyle(sheet);
        setSheetTitle(workbook, sheet);
        writeFeatureOptionRow(featureLibraryDtoList, workbook, sheet);
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
    private void setSheetTitle(XSSFWorkbook workbook, XSSFSheet sheet) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.SKY_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        int columnIndex = 0;
        XSSFRow row = sheet.createRow(0);
        for (String title : TITLE_LIST) {
            XSSFCell cell = row.createCell(columnIndex++);
            cell.setCellValue(title);
            cell.setCellStyle(style);
        }
    }

    /**
     * 创建Feature样式
     */
    private XSSFCellStyle createFeatureCellStyle(XSSFWorkbook workbook) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        return cellStyle;
    }

    /**
     * 写Feature和Option行数据
     */
    private void writeFeatureOptionRow(List<QueryFeatureLibraryDto> featureLibraryDtoList, XSSFWorkbook workbook, XSSFSheet sheet) {
        int rowIndex = 1;
        XSSFCellStyle featureCellStyle = createFeatureCellStyle(workbook);
        for (QueryFeatureLibraryDto group : featureLibraryDtoList) {
            for (QueryFeatureLibraryDto feature : group.getChildren()) {
                createFeatureOptionRow(feature, sheet, rowIndex++, featureCellStyle);
                for (QueryFeatureLibraryDto option : feature.getChildren()) {
                    createFeatureOptionRow(option, sheet, rowIndex++, null);
                }
            }
        }
    }

    /**
     * 创建Feature/Option行
     */
    private void createFeatureOptionRow(QueryFeatureLibraryDto featureOption, XSSFSheet sheet, int rowIndex, XSSFCellStyle cellStyle) {
        int columnIndex = -1;
        XSSFRow row = sheet.createRow(rowIndex);
        createCell(row, ++columnIndex, featureOption.getFeatureCode(), cellStyle);
        createCell(row, ++columnIndex, featureOption.getDisplayName(), cellStyle);
        createCell(row, ++columnIndex, featureOption.getChineseName(), cellStyle);
        createCell(row, ++columnIndex, featureOption.getDescription(), cellStyle);
        createCell(row, ++columnIndex, featureOption.getGroup(), cellStyle);
        createCell(row, ++columnIndex, "Configuration " + featureOption.getType(), cellStyle);
        createCell(row, ++columnIndex, featureOption.getCatalog(), cellStyle);
        createCell(row, ++columnIndex, featureOption.getRequestor(), cellStyle);
        createCell(row, ++columnIndex, featureOption.getCreateUser(), cellStyle);
        createCell(row, ++columnIndex, featureOption.getCreateTime(), cellStyle);
        createCell(row, ++columnIndex, featureOption.getUpdateUser(), cellStyle);
        createCell(row, ++columnIndex, featureOption.getUpdateTime(), cellStyle);
        createCell(row, ++columnIndex, featureOption.getStatus(), cellStyle);
        createCell(row, ++columnIndex, featureOption.getModelYear(), cellStyle);
    }

    /**
     * 创建Feature/Option单元格
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
