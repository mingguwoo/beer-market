package com.nio.ngfs.plm.bom.configuration.application.query.oxo.common;

import com.google.common.collect.Lists;
import com.nio.bom.share.utils.DateUtils;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.OxoBasicVehicleDto;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.enums.OxoOptionPackageTypeEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author wangchao.wang
 */
@Slf4j
public class OxoExcelUtil {


    public static void oxoExport(OxoListRespDto oxoListRespDto, String modelCode, String version, HttpServletResponse response, HttpServletRequest request) {
        //动态表头值
        List<OxoHeadQry> oxoHeads = oxoListRespDto.getOxoHeadResps();

        List<OxoBasicVehicleDto> oxoBasicVehicles = BaseVehicleFactory.buildOxoBasicVehicles(oxoHeads, modelCode, version);

        XSSFWorkbook xssfWorkbook = buildHead(oxoBasicVehicles);

        List<OxoRowsQry> dataList = oxoListRespDto.getOxoRowsResps();
        AtomicInteger rowIndex = new AtomicInteger(3);

        //设置字体大小
        XSSFFont featureFont = xssfWorkbook.createFont();
        featureFont.setFontName("等线");
        featureFont.setBold(true);
        featureFont.setFontHeightInPoints((short) 11);

        XSSFFont optionFont = xssfWorkbook.createFont();
        optionFont.setFontName("等线");
        optionFont.setFontHeightInPoints((short) 11);

        //打点字体
        XSSFFont font = xssfWorkbook.createFont();
        font.setFontName("等线");
        font.setFontHeightInPoints((short) 11);

        //Feature样式
        XSSFCellStyle cellFeatureColorStyle = xssfWorkbook.createCellStyle();
        cellFeatureColorStyle.setFont(featureFont);
        cellFeatureColorStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(192, 255, 255), new DefaultIndexedColorMap()));
        cellFeatureColorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellFeatureColorStyle.setAlignment(HorizontalAlignment.LEFT);

        //option样式
        XSSFCellStyle cellOptionColorStyle = xssfWorkbook.createCellStyle();
        cellOptionColorStyle.setFont(optionFont);
        XSSFCellStyle cellOptionCodeColorStyle = xssfWorkbook.createCellStyle();
        cellOptionCodeColorStyle.setFont(optionFont);
        cellOptionCodeColorStyle.setAlignment(HorizontalAlignment.LEFT);

        //配置打点样式
        XSSFCellStyle cellColorStyle = xssfWorkbook.createCellStyle();
        cellColorStyle.setFont(font);
        cellColorStyle.setAlignment(HorizontalAlignment.CENTER);    //左右居中
        cellColorStyle.setVerticalAlignment(VerticalAlignment.CENTER);    //上下居中

        //填充数据exportFeatureData
        for (int i = 0; i < dataList.size(); i++) {
            OxoRowsQry feature = dataList.get(i);
            buildData(modelCode, version, rowIndex, feature, oxoBasicVehicles, xssfWorkbook, cellFeatureColorStyle, cellOptionColorStyle, cellOptionCodeColorStyle, cellColorStyle);
        }
        OutputStream output = null;
        try {
            output = response.getOutputStream();
            String name = modelCode + "_" + version + "_OXO_Feature_" + DateUtils.dateTimeNow() + ".xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("access-control-expose-headers", "content-disposition");
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            xssfWorkbook.write(output);
        } catch (
                IOException e) {
            log.error("exportFeature failed :" + e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(output);
        }
    }


    private static void buildData(String modelCode, String version, AtomicInteger rowIndex, OxoRowsQry rowsQry, List<OxoBasicVehicleDto> oxoBasicVehicles,
                                  XSSFWorkbook xssfWorkbook, XSSFCellStyle cellFeatureColorStyle,
                                  XSSFCellStyle cellOptionColorStyle, XSSFCellStyle cellOptionCodeColorStyle, XSSFCellStyle cellCodeColorStyle) {
        int featureRowIndex = rowIndex.incrementAndGet();
        XSSFRow row = xssfWorkbook.getSheetAt(0).createRow(featureRowIndex);
        buildFeatureHeadDatas(rowsQry, row, oxoBasicVehicles, cellFeatureColorStyle);
        List<OxoRowsQry> children = rowsQry.getOptions();

        if (CollectionUtils.isNotEmpty(children)) {
            children.forEach(x -> {
                int childRowIndex = rowIndex.incrementAndGet();
                XSSFRow childRow = xssfWorkbook.getSheetAt(0).createRow(childRowIndex);
                buildOptionHeadDatas(x, childRow, cellOptionColorStyle, cellOptionCodeColorStyle);
                List<OxoEditCmd> optionOxoConfigration = x.getPackInfos();
                Map<String, List<OxoEditCmd>> oxoConfigrationMap = optionOxoConfigration.stream().collect(Collectors.groupingBy(o -> String.format(
                        "%s-%s-%s-%s", modelCode + " " + o.getModelYear() + " " + version, o.getRegionCode(), o.getDriveHandCode(), o.getSalesCode())));

                for (int i = 0; i < oxoBasicVehicles.size(); i++) {
                    XSSFCell cell = childRow.createCell(i + 5);
                    cell.setCellStyle(cellCodeColorStyle);
                    OxoBasicVehicleDto oxoSalesExportModel = oxoBasicVehicles.get(i);
                    String key = String.format("%s-%s-%s-%s", oxoSalesExportModel.getModelYear(), oxoSalesExportModel.getRegionCode(), oxoSalesExportModel.getDriverOptionCode(), oxoSalesExportModel.getSalesOptionCode());
                    if (oxoConfigrationMap.containsKey(key)) {
                        List<OxoEditCmd> ipdOxoOutputs = oxoConfigrationMap.get(key);
                        if (CollectionUtils.isNotEmpty(ipdOxoOutputs)) {
                            //一条记录的oxo打点匹配一行
                            OxoEditCmd oxoEditCmd = ipdOxoOutputs.get(0);
                            String packageNo = oxoEditCmd.getPackageCode();
                            StringBuilder packageCellValue = new StringBuilder();
                            //设置打点属性值
                            packageCellValue.append(OxoOptionPackageTypeEnum.getByType(packageNo).getCode());
                            if (StringUtils.isNotBlank(oxoEditCmd.getDescription())) {
                                packageCellValue.append("/").append(oxoEditCmd.getDescription());
                            }
                            cell.setCellValue(packageCellValue.toString());
                        }
                    }
                }
            });
        }
    }


    private static void buildOptionHeadDatas(OxoRowsQry rowsQry, XSSFRow row, XSSFCellStyle cellOptionColorStyle, XSSFCellStyle cellOptionCodeColorStyle) {
        XSSFCell optionCode = row.createCell(0);
        XSSFCell displayName = row.createCell(1);
        XSSFCell name = row.createCell(2);
        XSSFCell library = row.createCell(3);
        XSSFCell comments = row.createCell(4);
        optionCode.setCellValue(rowsQry.getFeatureCode());
        optionCode.setCellStyle(cellOptionCodeColorStyle);
        displayName.setCellValue(rowsQry.getDisplayName());
        displayName.setCellStyle(cellOptionColorStyle);
        name.setCellValue(rowsQry.getChineseName());
        name.setCellStyle(cellOptionColorStyle);
        library.setCellValue(rowsQry.getGroup());
        library.setCellStyle(cellOptionColorStyle);
        comments.setCellValue(rowsQry.getComments());
        comments.setCellStyle(cellOptionColorStyle);
    }


    private static void buildFeatureHeadDatas(OxoRowsQry feature, XSSFRow row, List<OxoBasicVehicleDto> oxoBasicVehicles, XSSFCellStyle cellFeatureColorStyle) {
        XSSFCell featureCode = row.createCell(0);
        XSSFCell displayName = row.createCell(1);
        XSSFCell name = row.createCell(2);
        XSSFCell library = row.createCell(3);
        XSSFCell comments = row.createCell(4);
        featureCode.setCellValue(feature.getFeatureCode());
        featureCode.setCellStyle(cellFeatureColorStyle);
        displayName.setCellValue(feature.getDisplayName());
        displayName.setCellStyle(cellFeatureColorStyle);
        name.setCellValue(feature.getChineseName());
        name.setCellStyle(cellFeatureColorStyle);
        library.setCellValue(feature.getGroup());
        library.setCellStyle(cellFeatureColorStyle);
        comments.setCellValue(feature.getComments());
        comments.setCellStyle(cellFeatureColorStyle);
        if (CollectionUtils.isNotEmpty(oxoBasicVehicles)) {
            for (int i = 0; i < oxoBasicVehicles.size(); i++) {
                XSSFCell cell = row.createCell(i + 5);
                cell.setCellStyle(cellFeatureColorStyle);
            }
        }
    }

    private static XSSFWorkbook buildHead(List<OxoBasicVehicleDto> oxoBasicVehicles) {
        List<String> commonHead = addCommonHead();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet sheet = xssfWorkbook.createSheet();

        //设置字体大小
        XSSFFont font = xssfWorkbook.createFont();
        font.setFontName("等线");
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);

        XSSFCellStyle cellColorStyle = xssfWorkbook.createCellStyle();
        cellColorStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(141, 144, 144)));
        cellColorStyle.setAlignment(HorizontalAlignment.CENTER);    //左右居中
        cellColorStyle.setVerticalAlignment(VerticalAlignment.CENTER);    //上下居中
        cellColorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellColorStyle.setFont(font);

        //动态表头样式
        XSSFCellStyle headStyle = xssfWorkbook.createCellStyle();
        headStyle.setAlignment(HorizontalAlignment.CENTER);    //左右居中
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(141, 144, 144)));
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headStyle.setFont(font);

        //固定表头样式
        XSSFCellStyle cellCenterStyle = xssfWorkbook.createCellStyle();
        cellCenterStyle.setAlignment(HorizontalAlignment.CENTER);    //左右居中
        cellCenterStyle.setVerticalAlignment(VerticalAlignment.CENTER);    //上下居中
        cellCenterStyle.setFont(font);
        cellCenterStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(141, 144, 144)));
        cellCenterStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFRow row1 = sheet.createRow(0);
        XSSFRow row2 = sheet.createRow(1);
        XSSFRow row3 = sheet.createRow(2);
        XSSFRow row4 = sheet.createRow(3);
        //设置固定表头
        for (int i = 0; i < commonHead.size(); i++) {
            XSSFCell cell = row1.createCell(i);
            cell.setCellValue(commonHead.get(i));
            cell.setCellStyle(cellColorStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 3, i, i));
        }

        //设置表头值和样式
        for (int i = 0; i < oxoBasicVehicles.size(); i++) {
            XSSFCell cell4 = row4.createCell(commonHead.size() + i);
            XSSFCell cell3 = row3.createCell(commonHead.size() + i);
            XSSFCell cell2 = row2.createCell(commonHead.size() + i);
            XSSFCell cell1 = row1.createCell(commonHead.size() + i);
            OxoBasicVehicleDto oxoBasicVehicleDto = oxoBasicVehicles.get(i);
            cell4.setCellValue(oxoBasicVehicleDto.getSalesOption());
            cell3.setCellValue(oxoBasicVehicleDto.getDriverOption());
            cell2.setCellValue(oxoBasicVehicleDto.getRegion());
            cell1.setCellValue(oxoBasicVehicleDto.getModelYear());
            cell1.setCellStyle(headStyle);
            cell2.setCellStyle(cellColorStyle);
            cell3.setCellStyle(headStyle);
            cell4.setCellStyle(headStyle);
        }

        //合并单元格
        for (int i = 0; i < oxoBasicVehicles.size() - 1; i++) {
            mergeRow(sheet, row3, commonHead.size() + i, commonHead.size() + oxoBasicVehicles.size());
        }
        //设置列宽
        sheet.setColumnWidth(0, 11 * 256 + 200);
        sheet.setColumnWidth(1, 18 * 256 + 200);
        sheet.setColumnWidth(2, 18 * 256 + 200);
        sheet.setColumnWidth(3, 16 * 256 + 200);
        sheet.setColumnWidth(4, 15 * 256 + 200);
        for (int i = 5; i < oxoBasicVehicles.size() + 5; i++) {
            // sheet.autoSizeColumn(i, true);
            //  int width = sheet.getColumnWidth(i) * 2;
            sheet.setColumnWidth(i, 14 * 256 + 200);
        }
        return xssfWorkbook;
    }


    private static void mergeRow(XSSFSheet sheet, XSSFRow row, Integer cell, Integer maxCellSize) {
        if (row == null || row.getRowNum() < 0) {
            return;
        }
        AtomicBoolean flag = new AtomicBoolean(true);
        AtomicInteger cellEnd = new AtomicInteger(cell + 1);
        while (flag.get() == true && cellEnd.get() < maxCellSize) {
            AtomicBoolean atomicBoolean = cellCompare(sheet, row.getRowNum(), cell, cellEnd, flag);
            if (atomicBoolean.get()) {
                cellEnd.incrementAndGet();
            }
        }
        if (cellEnd.get() - 1 > cell) {
            CellRangeAddress cellRangeAddress = new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell, cellEnd.get() - 1);
            List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
            AtomicBoolean contrain = new AtomicBoolean(true);
            mergedRegions.forEach(x -> {
                if (cellRangeAddress.getFirstRow() == x.getFirstRow() && cellRangeAddress.getLastRow() == x.getLastRow()) {
                    if (cellRangeAddress.getFirstColumn() > x.getFirstColumn() && cellRangeAddress.getFirstColumn() < x.getLastColumn()) {
                        contrain.set(false);
                    }
                }
            });
            if (contrain.get()) {
                sheet.addMergedRegion(cellRangeAddress);
            }
        }
        mergeRow(sheet, sheet.getRow(row.getRowNum() - 1), cell, maxCellSize);
    }


    private static AtomicBoolean cellCompare(XSSFSheet sheet, Integer rowNum, Integer cellStart, AtomicInteger cellEnd, AtomicBoolean flag) {
        while (rowNum >= 0 && flag.get() != false) {
            XSSFRow row = sheet.getRow(rowNum);
            if (!(row.getCell(cellStart).getStringCellValue().equals(row.getCell(cellEnd.get()).getStringCellValue()))) {
                flag.set(false);
            }
            rowNum--;
        }
        return flag;
    }


    public static List<String> addCommonHead() {
        List<String> commonHead = Lists.newArrayList();
        commonHead.add("Feature Code");
        commonHead.add("Display Name");
        commonHead.add("Chinese Name");
        commonHead.add("Group");
        commonHead.add("Comments");
        return commonHead;
    }


}
