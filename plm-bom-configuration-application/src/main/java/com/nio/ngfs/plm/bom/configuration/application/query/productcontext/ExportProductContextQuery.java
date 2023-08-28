package com.nio.ngfs.plm.bom.configuration.application.query.productcontext;

import com.google.common.collect.Lists;
import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.DateUtils;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractExportQuery;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request.ExportBaseVehicleQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.ExportProductContextQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.GetProductContextRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ProductContextColumnDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ProductContextFeatureRowDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author bill.wang
 * @date 2023/8/24
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExportProductContextQuery extends AbstractExportQuery {

    private static final List<String> TITLE_LIST = Lists.newArrayList(
        "Feature Code", "Feature Display name", "Option Code", "Option Display Name"
    );

    private final GetProductContextQuery getProductContextQuery;

    public void execute(ExportProductContextQry qry, HttpServletResponse response){

        GetProductContextRespDto productContextRespDto = getProductContextQuery.execute(qry);
        String fileName = qry.getModelCode() + "_ModelYear_Option_" + DateUtils.dateTimeNow("yyyyMMddHHmm") + ".xlsx";
        export(response, fileName, workbook -> exportProductContext(qry,productContextRespDto,workbook));
    }

    /**
     * Product Context导出到Excel
     */
    private void exportProductContext(ExportProductContextQry qry,GetProductContextRespDto productContextRespDto,XSSFWorkbook workbook){
        XSSFSheet sheet = workbook.createSheet();
        configSheetStyle(sheet);
        //动态表头值
        List<ProductContextColumnDto> productContextHeads = productContextRespDto.getProductContextColumnDtoList();
        setSheetTitle(workbook,sheet,qry,productContextHeads);
        writeProductContextRow(productContextRespDto,workbook,sheet);
    }

    /**
     * 设置Sheet样式
     */
    protected void configSheetStyle(XSSFSheet sheet) {
        sheet.setDefaultColumnWidth(15);
    }

    /**
     * 设置标题
     */
    private void setSheetTitle(XSSFWorkbook workbook, XSSFSheet sheet, ExportProductContextQry qry,List<ProductContextColumnDto> productContextHeads) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.SKY_BLUE.getIndex());
        int columnIndex = 0;
        XSSFRow row = sheet.createRow(0);
        for (String title : TITLE_LIST) {
            XSSFCell cell = row.createCell(columnIndex);
            cell.setCellValue(title);
            cell.setCellStyle(style);
            sheet.addMergedRegion(new CellRangeAddress(0,1,columnIndex,columnIndex));
            columnIndex++;
        }
        CellRangeAddress firstMergeRegion = new CellRangeAddress(0,1,0,columnIndex-1);
        sheet.addMergedRegion(firstMergeRegion);
        //动态表头
        XSSFCell cell = row.createCell(columnIndex++);
        cell.setCellValue(productContextHeads.get(CommonConstants.INT_ZERO).getModelCode());
        cell.setCellStyle(style);
//        for (int i = 0; i < productContextHeads.size();i++){
//            XSSFCell dynamicCell = row.createCell(columnIndex++);
//            ProductContextColumnDto columnDto = productContextHeads.get(i);
//            dynamicCell.setCellStyle(style);
//            dynamicCell.setCellValue(columnDto.getModelCode()+" "+columnDto.getModelYear());
//        }
//        //合并单元格
//        CellRangeAddress secondMergeRegion = new CellRangeAddress(0,0,4,columnIndex);
//        sheet.addMergedRegion(secondMergeRegion);
    }

    private void writeProductContextRow(GetProductContextRespDto productContextRespDto,XSSFWorkbook workbook, XSSFSheet sheet){
        int rowIndex = 1;
        XSSFCellStyle productContextCellStyle = createProductContextCellStyle(workbook);
        for (ProductContextFeatureRowDto productContextRow : productContextRespDto.getProductContextFeatureRowDtoList()){
            createProductContextRow(productContextRow,sheet,++rowIndex,productContextCellStyle);
        }
    }

    /**
     * 创建ProductContext样式
     */
    private XSSFCellStyle createProductContextCellStyle(XSSFWorkbook workbook) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        return cellStyle;
    }

    /**
     * 创建Product Context行
     */
    private void createProductContextRow(ProductContextFeatureRowDto productContextRow,XSSFSheet sheet, int rowIndex, XSSFCellStyle cellStyle){
        int columnIndex = -1;
        XSSFRow row = sheet.createRow(rowIndex);
        createCell(row, ++columnIndex,productContextRow.getFeatureCode(),cellStyle);
        createCell(row,++columnIndex,productContextRow.getDisplayName(),cellStyle);
//        for (int i = 0; i  < productContextRow.getOptionRowList().size();i++){
//            createCell(row,++columnIndex,productContextRow.getOptionRowList().get(i).getFeatureCode(),cellStyle);
//            createCell(row,++columnIndex,productContextRow.getOptionRowList().get(i).getDisplayName(),cellStyle);
//        }
        rowIndex = rowIndex + productContextRow.getOptionRowList().size()-1;
        CellRangeAddress firstMergeRegion = new CellRangeAddress(rowIndex,rowIndex,0,0);
        CellRangeAddress secondMergeRegion = new  CellRangeAddress(rowIndex,rowIndex,0,0);
        sheet.addMergedRegion(firstMergeRegion);
        sheet.addMergedRegion(secondMergeRegion);

    }

    /**
     * 创建product Context单元格
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
