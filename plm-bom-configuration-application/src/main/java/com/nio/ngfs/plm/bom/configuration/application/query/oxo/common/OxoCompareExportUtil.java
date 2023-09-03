package com.nio.ngfs.plm.bom.configuration.application.query.oxo.common;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.OxoBasicVehicleDto;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.enums.CompareChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.enums.OxoOptionPackageTypeEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoCompareQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author wangchao.wang
 */
@Slf4j
public class OxoCompareExportUtil {


    public static void compareExport(OxoListRespDto OxoListRespDto, OxoCompareQry compareCmd, HttpServletResponse response, HttpServletRequest request) {

        String modelCode = compareCmd.getModelCode();
        String baseVersion = compareCmd.getBaseVersion();
        String compareVersion = compareCmd.getCompareVersion();

        List<OxoBasicVehicleDto> oxoBasicVehicles = BaseVehicleFactory.buildOxoBasicVehicles(OxoListRespDto.getOxoHeadResps(), modelCode, "");

        XSSFWorkbook xssfWorkbook = buildHead(oxoBasicVehicles);

        List<OxoRowsQry> dataList = OxoListRespDto.getOxoRowsResps();
        AtomicInteger rowIndex = new AtomicInteger(4);

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
        cellFeatureColorStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(241, 250, 250)));
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
            buildData(rowIndex, feature, oxoBasicVehicles, xssfWorkbook, cellFeatureColorStyle, cellOptionColorStyle, cellOptionCodeColorStyle, cellColorStyle);
        }
        OutputStream output = null;
        try {
            output = response.getOutputStream();
            response.reset();
            // 导出文件 Model_Version_高版本号 vs 低版本号_OXO_Compare（例：Aries_Version_AB vs AA_OXO_Compare）
            String fileName = modelCode + "_Version_" + baseVersion + " vs " + compareVersion + "_OXO_Compare";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            xssfWorkbook.write(output);
        } catch (IOException e) {
            log.error("exportFeature failed :" + e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(output);
        }
    }


    private static void buildData(AtomicInteger rowIndex, OxoRowsQry feature, List<OxoBasicVehicleDto> oxoBasicVehicles, XSSFWorkbook xssfWorkbook, XSSFCellStyle cellFeatureColorStyle, XSSFCellStyle cellOptionColorStyle, XSSFCellStyle cellOptionCodeColorStyle, XSSFCellStyle cellCodeColorStyle) {
        int featureRowIndex = rowIndex.incrementAndGet();
        XSSFRow row = xssfWorkbook.getSheetAt(0).createRow(featureRowIndex);
        buildFeatureHeadDatas(feature, row, oxoBasicVehicles, cellFeatureColorStyle);
        List<OxoRowsQry> children = feature.getOptions();
        Map<String, Integer> optionIndex = Maps.newLinkedHashMap();
        AtomicInteger optionStartIndex = new AtomicInteger(6);
        oxoBasicVehicles.forEach(x -> {
            optionIndex.put(String.format("%s-%s-%s-%s", x.getModelYear(), x.getRegionCode(), x.getDriverOptionCode(), x.getSalesOptionCode()), optionStartIndex.get());
            optionStartIndex.incrementAndGet();
        });

        if (CollectionUtils.isNotEmpty(children)) {
            children.forEach(x -> {
                int childRowIndex = rowIndex.incrementAndGet();
                XSSFRow childRow = xssfWorkbook.getSheetAt(0).createRow(childRowIndex);
                buildOptionHeadDatas(x, childRow, cellOptionColorStyle, cellOptionCodeColorStyle);
                List<OxoEditCmd> optionOxoConfigration = x.getPackInfos();
                Map<String, List<OxoEditCmd>> oxoConfigrationMap = optionOxoConfigration.stream().collect(Collectors.groupingBy(o -> String.format("%s-%s-%s-%s", o.getModelCode()+" "+o.getModelYear()+" ",
                        o.getRegionCode(), o.getDriveHandCode(), o.getSalesCode())));
                //是否有变更，没有的话需要清除changeType 为 modify
                AtomicBoolean isModify = new AtomicBoolean(false);
                for (int i = 0; i < oxoBasicVehicles.size(); i++) {
                    XSSFCell cell = childRow.createCell(i + 6);
                    cell.setCellStyle(cellCodeColorStyle);
                    //列的changeType如果是delete 整列的option不展示
                    XSSFRow changeTypeRow = xssfWorkbook.getSheetAt(0).getRow(4);
                    XSSFCell changeTypeCell = changeTypeRow.getCell(i + 6);
                    if (!Objects.isNull(changeTypeCell) && CompareChangeTypeEnum.DELETE.getName().equals(changeTypeCell.getStringCellValue())) {
                        cell.setCellValue("");
                        continue;
                    }
                    OxoBasicVehicleDto oxoBasicVehicleDto = oxoBasicVehicles.get(i);
                    String key = String.format("%s-%s-%s-%s", oxoBasicVehicleDto.getModelYear(), oxoBasicVehicleDto.getRegionCode(), oxoBasicVehicleDto.getDriverOptionCode(), oxoBasicVehicleDto.getSalesOptionCode());
                    if (oxoConfigrationMap.containsKey(key)) {
                        List<OxoEditCmd> ipdOXOOutputs = oxoConfigrationMap.get(key);
                        if (CollectionUtils.isNotEmpty(ipdOXOOutputs)) {
                            //一条记录的oxo打点匹配一行
                            OxoEditCmd ipdOXOOutput = ipdOXOOutputs.get(0);
                            String packageCode = ipdOXOOutput.getPackageCode();
                            OxoEditCmd compareIpdOXOOutput = ipdOXOOutput.getCompareOxoEdit();
                            StringBuffer packageCellValue = new StringBuffer();
                            if (!Objects.isNull(compareIpdOXOOutput)) {
                                String compareIpdPackageNo = compareIpdOXOOutput.getPackageCode();
                                if (StringUtils.isNotBlank(compareIpdPackageNo)) {
                                    if (StringUtils.equals(OxoOptionPackageTypeEnum.UNAVAILABLE.getType(),compareIpdPackageNo)) {
                                        packageCellValue.append("->");
                                    } else if (StringUtils.equals(OxoOptionPackageTypeEnum.DEFALUT.getType(), compareIpdPackageNo)) {
                                        packageCellValue.append("●>");
                                    } else if (StringUtils.equals(OxoOptionPackageTypeEnum.AVAILABLE.getType(), compareIpdPackageNo)) {
                                        packageCellValue.append("○>");
                                    } else if (StringUtils.isNotBlank(compareIpdPackageNo)) {
                                        packageCellValue.append(compareIpdPackageNo + ">");
                                    }
                                }
                            }
                            //设置打点属性值
                            if (StringUtils.equals(OxoOptionPackageTypeEnum.UNAVAILABLE.getType(),packageCode)) {
                                cell.setCellValue(packageCellValue.append("-").toString());
                            } else if (StringUtils.equals(OxoOptionPackageTypeEnum.DEFALUT.getType(),packageCode)) {
                                cell.setCellValue(packageCellValue.append("●").toString());
                            } else if (StringUtils.equals(OxoOptionPackageTypeEnum.AVAILABLE.getType(), packageCode)) {
                                cell.setCellValue(packageCellValue.append("○").toString());
                            } else if (StringUtils.isNotBlank(packageCode)) {
                                cell.setCellValue(packageCellValue.append(packageCode).toString());
                            }

                        }
                    }
                    if (StringUtils.isNotBlank(cell.getStringCellValue()) && cell.getStringCellValue().contains(">")) {
                        isModify.set(true);
                    }
                }
                String changeType = childRow.getCell(0).getStringCellValue();
                if (StringUtils.isNotBlank(changeType) && CompareChangeTypeEnum.MODIFY.getName().equals(changeType)) {
                    if (!isModify.get()) {
                        //清除
                        childRow.getCell(0).setCellValue("");
                    }
                }
            });
        }
    }

    private static void buildFeatureHeadDatas(OxoRowsQry feature, XSSFRow row, List<OxoBasicVehicleDto> oxoBasicVehicleDtos, XSSFCellStyle cellFeatureColorStyle) {
        XSSFCell changeType = row.createCell(0);
        XSSFCell featureCode = row.createCell(1);
        XSSFCell displayName = row.createCell(2);
        XSSFCell name = row.createCell(3);
        XSSFCell library = row.createCell(4);
        XSSFCell comments = row.createCell(5);
        changeType.setCellValue(CompareChangeTypeEnum.DEL.getName().equals(feature.getChangeType()) ? "" : feature.getChangeType());
        changeType.setCellStyle(cellFeatureColorStyle);
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
        if (CollectionUtils.isNotEmpty(oxoBasicVehicleDtos)) {
            for (int i = 0; i < oxoBasicVehicleDtos.size(); i++) {
                XSSFCell cell = row.createCell(i + 6);
                cell.setCellStyle(cellFeatureColorStyle);
            }
        }
    }

    private static void buildOptionHeadDatas(OxoRowsQry oxoRowsQry, XSSFRow row, XSSFCellStyle cellOptionColorStyle, XSSFCellStyle cellOptionCodeColorStyle) {
        XSSFCell changeType = row.createCell(0);
        XSSFCell optionCode = row.createCell(1);
        XSSFCell displayName = row.createCell(2);
        XSSFCell name = row.createCell(3);
        XSSFCell library = row.createCell(4);
        XSSFCell comments = row.createCell(5);
        changeType.setCellValue(CompareChangeTypeEnum.DEL.getName().equals(oxoRowsQry.getChangeType()) ? "" : oxoRowsQry.getChangeType());
        changeType.setCellStyle(cellOptionColorStyle);
        optionCode.setCellValue(oxoRowsQry.getFeatureCode());
        optionCode.setCellStyle(cellOptionCodeColorStyle);
        displayName.setCellValue(oxoRowsQry.getDisplayName());
        displayName.setCellStyle(cellOptionColorStyle);
        name.setCellValue(oxoRowsQry.getChineseName());
        name.setCellStyle(cellOptionColorStyle);
        library.setCellValue(oxoRowsQry.getGroup());
        library.setCellStyle(cellOptionColorStyle);
        comments.setCellValue(oxoRowsQry.getComments());
        comments.setCellStyle(cellOptionColorStyle);
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
        cellColorStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(230, 249, 249)));
        cellColorStyle.setAlignment(HorizontalAlignment.CENTER);    //左右居中
        cellColorStyle.setVerticalAlignment(VerticalAlignment.CENTER);    //上下居中
        cellColorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellColorStyle.setFont(font);

        //动态表头样式
        XSSFCellStyle headStyle = xssfWorkbook.createCellStyle();
        headStyle.setAlignment(HorizontalAlignment.CENTER);    //左右居中
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headStyle.setFont(font);

        //固定表头样式
        XSSFCellStyle cellCenterStyle = xssfWorkbook.createCellStyle();
        cellCenterStyle.setAlignment(HorizontalAlignment.CENTER);    //左右居中
        cellCenterStyle.setVerticalAlignment(VerticalAlignment.CENTER);    //上下居中
        cellCenterStyle.setFont(font);

        XSSFRow row1 = sheet.createRow(0);
        XSSFRow row2 = sheet.createRow(1);
        XSSFRow row3 = sheet.createRow(2);
        XSSFRow row4 = sheet.createRow(3);
        XSSFRow row5 = sheet.createRow(4);
        //设置固定表头
        for (int i = 0; i < commonHead.size(); i++) {
            XSSFCell cell = row1.createCell(i);
            if (i == 0) {
                XSSFCellStyle cellStyle = cell.getCellStyle();
                cellStyle.setWrapText(true);
            }
            cell.setCellValue(commonHead.get(i));
            cell.setCellStyle(headStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 3, i, i));
        }
        //动态表头值


        //设置表头值和样式
        for (int i = 0; i < oxoBasicVehicles.size(); i++) {
            XSSFCell cell4 = row4.createCell(commonHead.size() + i);
            XSSFCell cell3 = row3.createCell(commonHead.size() + i);
            XSSFCell cell2 = row2.createCell(commonHead.size() + i);
            XSSFCell cell1 = row1.createCell(commonHead.size() + i);
            OxoBasicVehicleDto oxoSalesExportModel = oxoBasicVehicles.get(i);
            cell4.setCellValue(oxoSalesExportModel.getSalesOption());
            cell3.setCellValue(oxoSalesExportModel.getDriverOption());
            cell2.setCellValue(oxoSalesExportModel.getRegion());
            cell1.setCellValue(oxoSalesExportModel.getModelYear());
            cell1.setCellStyle(headStyle);
            cell2.setCellStyle(cellColorStyle);
            cell3.setCellStyle(headStyle);
            cell4.setCellStyle(headStyle);
            if (StringUtils.isNotBlank(oxoSalesExportModel.getChangeType())) {
                XSSFCell cell5 = row5.createCell(commonHead.size() + i);
                cell5.setCellValue(oxoSalesExportModel.getChangeType());
                cell5.setCellStyle(headStyle);
            }
        }

        //合并单元格
        for (int i = 0; i < oxoBasicVehicles.size() - 1; i++) {
            mergeRow(sheet, row3, commonHead.size() + i, commonHead.size() + oxoBasicVehicles.size());
        }
        //设置列宽
        sheet.setColumnWidth(0, 11 * 256 + 200);
        sheet.setColumnWidth(1, 11 * 256 + 200);
        sheet.setColumnWidth(2, 18 * 256 + 200);
        sheet.setColumnWidth(3, 18 * 256 + 200);
        sheet.setColumnWidth(4, 16 * 256 + 200);
        sheet.setColumnWidth(5, 15 * 256 + 200);
        for (int i = 6; i < oxoBasicVehicles.size(); i++) {
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
        commonHead.add("Change Type");
        commonHead.add("Feature Code");
        commonHead.add("Display Name");
        commonHead.add("Chinese Name");
        commonHead.add("Group");
        commonHead.add("Comments");
        return commonHead;
    }


}
