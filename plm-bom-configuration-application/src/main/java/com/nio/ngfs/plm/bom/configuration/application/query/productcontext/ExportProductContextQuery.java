package com.nio.ngfs.plm.bom.configuration.application.query.productcontext;

import com.google.common.collect.Lists;
import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.utils.DateUtils;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractExportQuery;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.ExportProductContextQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.GetProductContextRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ProductContextColumnDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ProductContextFeatureRowDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ProductContextOptionRowDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

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

    private static Map<String,List<String>> optionCodeModelYearMap = new HashMap<>();
    private static Map<String,Integer> modelYearColumnMap = new HashMap<>();

    private final GetProductContextQuery getProductContextQuery;

    public void execute(ExportProductContextQry qry, HttpServletResponse response){

        GetProductContextRespDto productContextRespDto = getProductContextQuery.execute(qry);
        String fileName = qry.getModelCode() + "_ModelYear_Option_" + DateUtils.dateTimeNow("yyyyMMddHHmm") + ".xlsx";
        export(response, fileName, workBook -> exportProductContext(qry,productContextRespDto,workBook));
    }

    /**
     * Product Context导出到Excel
     */
    private void exportProductContext(ExportProductContextQry qry,GetProductContextRespDto productContextRespDto,XSSFWorkbook workBook){
        XSSFSheet sheet = workBook.createSheet();
        configSheetStyle(sheet);
        initialOptionCodeModelYearMap(productContextRespDto);
        //获取动态表头值
        List<ProductContextColumnDto> productContextHeads = productContextRespDto.getProductContextColumnDtoList();
        //设置表头
        setSheetTitle(workBook,sheet,qry,productContextHeads);
        //写入行
        writeProductContextRow(productContextRespDto,workBook,sheet);
    }

    private void initialOptionCodeModelYearMap(GetProductContextRespDto productContextRespDto){
        Map<Long, ProductContextOptionRowDto> rowMap = new HashMap<>();
        Map<Long, ProductContextColumnDto> columnMap = new HashMap<>();
        productContextRespDto.getProductContextFeatureRowDtoList().forEach(featureRow->{
                featureRow.getOptionRowList().forEach(optionRow->{
                    rowMap.put(optionRow.getRowId(),optionRow);
                });
        });
        productContextRespDto.getProductContextColumnDtoList().forEach(column->{
            columnMap.put(column.getColumnId(),column);
        });
        productContextRespDto.getProductContextPointDtoList().forEach(point->{
            if (optionCodeModelYearMap.containsKey(rowMap.get(point.getRowId()).getFeatureCode())){
                optionCodeModelYearMap.get(rowMap.get(point.getRowId()).getFeatureCode()).add(columnMap.get(point.getColumnId()).getModelYear());
            }
            else{
                List<String> selectedModelYearList = new ArrayList<>();
                selectedModelYearList.add(columnMap.get(point.getColumnId()).getModelYear());
                optionCodeModelYearMap.put(rowMap.get(point.getRowId()).getFeatureCode(),selectedModelYearList);
            }
        });
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
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.SKY_BLUE.getIndex());
        style.setVerticalAlignment(VerticalAlignment.CENTER);    //上下居中
        int columnIndex = 0;
        XSSFRow rowOne = sheet.createRow(0);
        XSSFRow rowTwo = sheet.createRow(1);
        for (String title : TITLE_LIST) {
            XSSFCell cell = rowOne.createCell(columnIndex);
            cell.setCellValue(title);
            cell.setCellStyle(style);
            CellRangeAddress range = new CellRangeAddress(0,1,columnIndex,columnIndex);
            sheet.addMergedRegion(range);
            addRegionBorder(range,sheet);
            columnIndex++;
        }
        //动态表头
        XSSFCell modelCell = rowOne.createCell(columnIndex);
        modelCell.setCellValue(productContextHeads.get(CommonConstants.INT_ZERO).getModelCode());
        XSSFCellStyle modelStyle = workbook.createCellStyle();
        modelStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        modelStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.SKY_BLUE.getIndex());
        modelStyle.setAlignment(HorizontalAlignment.CENTER);    //左右居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);    //上下居中
        modelCell.setCellStyle(modelStyle);
        for (int i = 0; i < productContextHeads.size();i++){
            XSSFCell dynamicCell = rowTwo.createCell(columnIndex++);
            ProductContextColumnDto columnDto = productContextHeads.get(i);
            dynamicCell.setCellStyle(style);
            dynamicCell.setCellValue(columnDto.getModelCode()+" "+columnDto.getModelYear());
            modelYearColumnMap.put(productContextHeads.get(i).getModelYear(),columnIndex-1);
        }
        CellRangeAddress dynamicRange = new CellRangeAddress(0,0,4,columnIndex-1);
        sheet.addMergedRegion(dynamicRange);
        addRegionBorder(dynamicRange,sheet);
    }

    private void writeProductContextRow(GetProductContextRespDto productContextRespDto,XSSFWorkbook workbook, XSSFSheet sheet){
        int rowIndex = 2;
        boolean colorFlag = true;
        for (ProductContextFeatureRowDto productContextRow : productContextRespDto.getProductContextFeatureRowDtoList()){
            XSSFCellStyle productContextCellStyle = createProductContextCellStyle(workbook,colorFlag);
            productContextCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            createProductContextRow(productContextRow,sheet,rowIndex,productContextCellStyle);
            colorFlag = !colorFlag;
            rowIndex = rowIndex+productContextRow.getOptionRowList().size();
        }
    }

    /**
     * 创建ProductContext样式
     */
    private XSSFCellStyle createProductContextCellStyle(XSSFWorkbook workbook,boolean colorFlag) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        if (colorFlag){
            cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        }
        else {
            cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        }
        return cellStyle;
    }

    /**
     * 创建Product Context 行
     */
    private void createProductContextRow(ProductContextFeatureRowDto productContextRow,XSSFSheet sheet, int rowIndex, XSSFCellStyle cellStyle){
        XSSFRow row = sheet.createRow(rowIndex);
        //先弄Feature
        createFeatureCell(row,0,1,productContextRow.getFeatureCode(),productContextRow.getDisplayName(),cellStyle);
        //再弄Option
        for (int i = 0; i  < productContextRow.getOptionRowList().size();i++){
            Set<Integer> selectedCell = new HashSet<>();
            ProductContextOptionRowDto optionRowDto = productContextRow.getOptionRowList().get(i);
            //如果是和feature同一行，就先用原先的row
            if (i == 0){
                tickOption(row,optionRowDto,cellStyle,selectedCell);
            }
            else{
                XSSFRow  optionRow = sheet.createRow(rowIndex+i);
                tickOption(optionRow,optionRowDto,cellStyle,selectedCell);
            }
        }
        //如果有超过一个option的， 需要合并feature部分
        if (productContextRow.getOptionRowList().size()>1){
            CellRangeAddress firstMergeRegion = new CellRangeAddress(rowIndex,rowIndex+productContextRow.getOptionRowList().size()-1,0,0);
            CellRangeAddress secondMergeRegion = new  CellRangeAddress(rowIndex,rowIndex+productContextRow.getOptionRowList().size()-1,1,1);
            sheet.addMergedRegion(firstMergeRegion);
            sheet.addMergedRegion(secondMergeRegion);
            addRegionBorder(firstMergeRegion,sheet);
            addRegionBorder(secondMergeRegion,sheet);
        }

    }

    /**
     * 为区域增加边框
     * @param range
     * @param sheet
     */
    private void addRegionBorder(CellRangeAddress range,XSSFSheet sheet){
        RegionUtil.setBorderBottom(BorderStyle.THIN,range,sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN,range,sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN,range,sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN,range,sheet);
    }

    private void createFeatureCell(XSSFRow row,int firstColumn,int secondColumn,String featureCode,String displayName,XSSFCellStyle cellStyle){
        createCell(row,firstColumn,featureCode,cellStyle);
        createCell(row,secondColumn,displayName,cellStyle);
    }
    /**
     * 打勾
     * @param row
     * @param optionRowDto
     * @param cellStyle
     * @param selectedCell
     */
    private void tickOption(XSSFRow row,ProductContextOptionRowDto optionRowDto,XSSFCellStyle cellStyle,Set<Integer> selectedCell){
            createFeatureCell(row,2,3,optionRowDto.getFeatureCode(), optionRowDto.getDisplayName(), cellStyle);
            //打勾
            if(Objects.nonNull(optionCodeModelYearMap.get(optionRowDto.getFeatureCode()))){
                optionCodeModelYearMap.get(optionRowDto.getFeatureCode()).forEach(modelYear->{
                    createCell(row,modelYearColumnMap.get(modelYear),"\u2714",cellStyle);
                    selectedCell.add(modelYearColumnMap.get(modelYear));
                });
                modelYearColumnMap.forEach((key,value)->{
                    if (!selectedCell.contains(value)){
                        createCell(row,value,null,cellStyle);
                    }
                });
            }

            //如果一个option 完全没有被选中，也需要同步样式（除了AF00外理论上不存在这种情况，实际可能因数据错误出现）
            else{
                modelYearColumnMap.forEach((key,value)->{
                    createCell(row,value,null,cellStyle);
                });
            }
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
