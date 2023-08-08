package com.nio.ngfs.plm.bom.configuration.application.query.oxo;


import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoFeatureOptionApplicationService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OxoInfoExportQuery  {
//
//
//
//    private final OxoFeatureOptionApplicationService oxoFeatureOptionApplicationService;
//
//
//    /**
//     * Excel标题列表
//     */
//    private static final List<String> TITLE_LIST = Lists.newArrayList(
//            "Feature Code", "Display Name", "Chinese Name", "Group",
//            "Comments"
//    );
//
//    /**
//     * 页面导出
//     * @param oxoBaseCmd
//     * @return
//     */
//    public void execute(OxoBaseCmd oxoBaseCmd) {
//
//        String modelCode = oxoBaseCmd.getModelCode();
//
//        //查询feature数据
//        OxoListQry qry = oxoFeatureOptionApplicationService.queryOxoInfoByModelCode(modelCode);
//
//        List<String> columns = new ArrayList<>();
//        List<String> properties = new ArrayList<>();
//        //处理数据
//        List<Map<String, Object>> result = new ArrayList<>();
//        String version = (String) featureMap.getOrDefault("version", null);
//        handleFeatureExportData(featureList, result, properties);
//        String modelCode = (String) featureMap.get("modelCode");
//        String fileName = generateExportFileName(modelCode, version, "OXO_Feature");
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        exportFeatureData(workbook, columns, properties, result, featureMap);
//        OutputStream output = null;
//        try {
//            output = response.getOutputStream();
//            response.reset();
//            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
//            response.setContentType("application/octet-stream;charset=UTF-8");
//            workbook.write(output);
//        } catch (IOException e) {
//            log.error("exportFeature failed :" + e.getMessage(), e);
//        } finally {
//            IOUtils.closeQuietly(output);
//        }
//    }
//
//
//    /**
//     * 写入数据
//     *
//     * @param columns
//     * @param properties
//     * @param result
//     * @return
//     */
//    private XSSFWorkbook exportFeatureData(XSSFWorkbook workbook, List<String> columns, List<String> properties, List<Map<String, Object>> result, Map<String, Object> featureMap) {
//
//        XSSFSheet sheet = workbook.createSheet("sheet1");
//
//        //设置字体
//        XSSFFont font = workbook.createFont();
//        font.setFontName("等线");
//        font.setBold(true);
//
//        //创建绘图对象
//        XSSFDrawing drawingPatriarch = sheet.createDrawingPatriarch();
//
//        XSSFCellStyle cellColorStyle = workbook.createCellStyle();
//        //设置填充方案
//        cellColorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        //设置自定义填充颜色
//        cellColorStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(241, 250, 250)));
//        //左右居中
//        cellColorStyle.setAlignment(HorizontalAlignment.CENTER);
//        //上下居中
//        cellColorStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//        cellColorStyle.setFont(font);
//
//        XSSFCellStyle cellColorAndFontStyle = workbook.createCellStyle();
//        //设置填充方案
//        cellColorAndFontStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        //设置自定义填充颜色
//        cellColorAndFontStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(230, 249, 249)));
//        cellColorAndFontStyle.setFont(font);
//
//        XSSFCellStyle cellCenterStyle = workbook.createCellStyle();
//        cellCenterStyle.setAlignment(HorizontalAlignment.CENTER);    //左右居中
//        cellCenterStyle.setVerticalAlignment(VerticalAlignment.CENTER);    //上下居中
//        cellCenterStyle.setFont(font);
//
//        XSSFCellStyle cellLeftBoardCenterStyle = workbook.createCellStyle();
//        cellLeftBoardCenterStyle.setAlignment(HorizontalAlignment.CENTER);    //左右居中
//        cellLeftBoardCenterStyle.setVerticalAlignment(VerticalAlignment.CENTER);    //上下居中
//        cellLeftBoardCenterStyle.setBorderLeft(BorderStyle.MEDIUM);//左边框
//        cellLeftBoardCenterStyle.setFont(font);
//
//        XSSFCellStyle cellLeftBoardColorStyle = workbook.createCellStyle();
//        cellLeftBoardColorStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(230, 249, 249)));
//        cellLeftBoardColorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        cellLeftBoardColorStyle.setBorderLeft(BorderStyle.MEDIUM);//左边框
//        cellLeftBoardColorStyle.setFont(font);
//
//        XSSFCellStyle cellLeftBoardCenterColorStyle = workbook.createCellStyle();
//        cellLeftBoardCenterColorStyle.setAlignment(HorizontalAlignment.CENTER);    //左右居中
//        cellLeftBoardCenterColorStyle.setVerticalAlignment(VerticalAlignment.CENTER);    //上下居中
//        cellLeftBoardCenterColorStyle.setBorderLeft(BorderStyle.MEDIUM);//左边框
//        cellLeftBoardCenterColorStyle.setFont(font);
//        cellLeftBoardCenterColorStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(241, 250, 250)));
//        cellLeftBoardCenterColorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//        XSSFCellStyle cellOptionStyle = workbook.createCellStyle();
//        cellOptionStyle.setAlignment(HorizontalAlignment.CENTER);    //左右居中
//        cellOptionStyle.setVerticalAlignment(VerticalAlignment.CENTER);    //上下居中
//        //设置字体
//        XSSFFont optionFont = workbook.createFont();
//        optionFont.setColor(new XSSFColor(new java.awt.Color(147, 151, 153)));
//        optionFont.setFontName("等线");
//        cellOptionStyle.setFont(optionFont);
//        cellOptionStyle.setWrapText(true);
//
//        XSSFRow row = sheet.createRow(0);
//        int index = 0;
//        //设置前四格 标题格
//        for (String title : columns) {
//            XSSFCell cell = row.createCell(index);
//            cell.setCellValue(title);
//            cell.setCellStyle(cellCenterStyle);
//            sheet.setColumnWidth(index, 22 * 256);
//            sheet.addMergedRegion(new CellRangeAddress(0, 3, index, index));
//            index++;
//        }
//        Map<String, String> projectColumn = (Map<String, String>) featureMap.get("projectColumn");
//        //记录project开始列数
//        List<Integer> projectColumnList = new ArrayList<>();
//        if (MapUtils.isNotEmpty(projectColumn)) {
//            List<String> row1ValueList = new ArrayList<>();
//            List<String> rowValueList = new ArrayList<>();
//            XSSFRow row1 = sheet.createRow(1);
//            XSSFRow row2 = sheet.createRow(2);
//            XSSFRow row3 = sheet.createRow(3);
//
//            for (Map.Entry<String, String> entry : projectColumn.entrySet()) {
//                if (StringUtils.equals(entry.getKey(), "modelYear")) {
//                    XSSFCell cell1 = row1.createCell(index);
//                    cell1.setCellValue(entry.getValue());
//                    cell1.setCellStyle(cellColorStyle);
//                    continue;
//                }
//                sheet.setColumnWidth(index, 18 * 256);
//                String[] split = entry.getValue().split("\\|");
//                row1ValueList.add(split[0] + split[1]);
//                rowValueList.add(split[0]);
//                // row2ValueList.add(split[0] +split[2]);
//                XSSFCell cell3 = row3.createCell(index);
//                cell3.setCellValue(split[3]);
//                cell3.setCellStyle(cellCenterStyle);
//
//                XSSFCell cell2 = row2.createCell(index);
//                cell2.setCellValue(split[2]);
//                cell2.setCellStyle(cellCenterStyle);
//
//                XSSFCell cell1 = row1.createCell(index);
//                cell1.setCellValue(split[1]);
//                cell1.setCellStyle(cellColorStyle);
//
//                XSSFCell cell = row.createCell(index);
//                cell.setCellValue(split[0]);
//                cell.setCellStyle(cellCenterStyle);
//
//                index++;
//            }
//            //合并单元格
//            Map<String, Integer> countMap1 = frequencyOfList(row1ValueList);
//            // Map<String, Integer> countMap2 = frequencyOfList(row2ValueList);
//            Map<String, Integer> countMap = frequencyOfList(rowValueList);
//            int mergeIndex = columns.size();
//            if (Objects.nonNull(countMap1)) {
//                for (Integer value : countMap1.values()) {
//                    if (value > 1) {
//                        sheet.addMergedRegion(new CellRangeAddress(1, 1, mergeIndex, mergeIndex + value - 1));
//                    }
//                    mergeIndex = mergeIndex + value;
//                }
//            }
//            mergeIndex = columns.size();
//            if (Objects.nonNull(countMap)) {
//                for (Integer value : countMap.values()) {
//                    projectColumnList.add(mergeIndex);
//                    if (value > 1) {
//                        sheet.addMergedRegion(new CellRangeAddress(0, 0, mergeIndex, mergeIndex + value - 1));
//                    }
//                    mergeIndex = mergeIndex + value;
//                }
//                sheet.getRow(0).getCell(0).setCellStyle(cellCenterStyle);
//            }
//        }
//        if (CollectionUtils.isNotEmpty(result)) {
//            List<Integer> featureRow = new ArrayList<>();
//            List<Integer> optionRow = new ArrayList<>();
//            for (int i = 0; i < result.size(); i++) {
//                XSSFRow column = sheet.createRow(i + 4);
//                if ("1".equals(result.get(i).get("level"))) {
//                    featureRow.add(i + 4);
//                    optionRow.add(i + 4);
//                }
//                if ("2".equals(result.get(i).get("level"))) {
//                    optionRow.add(i + 4);
//                }
//                for (int j = 0; j < properties.size(); j++) {
//                    XSSFCell cell = column.createCell(j);
//                    if ("1".equals(result.get(i).get("level"))) {
//                        String value = (String) result.get(i).get(properties.get(j));
//                        cell.setCellValue(value);
//                        cell.setCellStyle(cellColorAndFontStyle);
//                        continue;
//                    }
//                    if (j == 0) {
//                        if ("2".equals(result.get(i).get("level"))) {
//                            String ipdStatus = (String) result.get(i).get("ipdStatus");
//                            cell.setCellValue(result.get(i).get("code") + ipdStatus);
//                            continue;
//                        }
//                        if ("3".equals(result.get(i).get("level"))) {
//                            String status = (String) result.get(i).get("status");
//                            cell.setCellValue(result.get(i).get("code") + (status == null ? "" :
//                                    (status.contains(":") ? " ( " + status.split(":")[0] + ")" : " ( " + status + ")")));
//                            continue;
//                        }
//                    }
//                    String value = (String) result.get(i).get(properties.get(j));
//                    cell.setCellValue(value);
//                    if (j >= columns.size() && StringUtils.isNotBlank(value)) {
//                        String[] packageSplit = value.split("\n");
//                        String packageNo = packageSplit[0];
//                        if ("Unavailable".equals(packageNo)) {
//                            cell.setCellValue("-");
//                        } else if ("Default".equals(packageNo)) {
//                            cell.setCellValue("●");
//                        } else if ("Available".equals(packageNo)) {
//                            cell.setCellValue("○");
//                        } else if (StringUtils.isNotBlank(packageNo)) {
//                            cell.setCellValue(packageNo);
//                            if (packageSplit.length > 1) {
//                                String packChName = packageSplit[1];
//                                StringJoiner joiner = new StringJoiner(System.lineSeparator());
//                                String[] packageNoSplit = packageNo.split(",");
//                                String[] packChNameSplit = packChName.split(",");
//                                for (int k = 0; k < packageNoSplit.length; k++) {
//                                    if (packChNameSplit.length <= k) {
//                                        break;
//                                    }
//                                    joiner.add(packageNoSplit[k] + " " + packChNameSplit[k]);
//                                }
//                                //获取批注对象
//                                //(int dx1, int dy1, int dx2, int dy2, short col1, int row1, short col2, int row2)
//                                //前四个参数是坐标点,后四个参数是编辑和显示批注时的大小.
//                                XSSFComment comment = drawingPatriarch.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 10));
//                                //输入批注信息
//                                comment.setString(new XSSFRichTextString(joiner.toString()));
//                                //将批注添加到单元格对象中
//                                cell.setCellComment(comment);
//                            }
//                        }
//                        cell.setCellStyle(cellOptionStyle);
//                    }
//                }
//            }
//            //行列分组
//            if (CollectionUtils.isNotEmpty(featureRow)) {
//                featureRow.add(sheet.getLastRowNum() + 1);
//                for (int i = 0; i < featureRow.size() - 1; i++) {
//                    if (featureRow.get(i) + 1 <= featureRow.get(i + 1) - 1) {
//                        sheet.groupRow(featureRow.get(i) + 1, featureRow.get(i + 1) - 1);
//                    }
//                }
//            }
//            if (CollectionUtils.isNotEmpty(optionRow)) {
//                optionRow.remove(0);
//                optionRow.add(sheet.getLastRowNum() + 1);
//                for (int i = 0; i < optionRow.size() - 1; i++) {
//                    if (optionRow.get(i) + 1 <= optionRow.get(i + 1) - 1) {
//                        sheet.groupRow(optionRow.get(i) + 1, optionRow.get(i + 1) - 1);
//                    }
//                }
//            }
//
//        }
//        XSSFCellStyle cellLeftBoardStyle = workbook.createCellStyle();
//        cellLeftBoardStyle.setBorderLeft(BorderStyle.MEDIUM);//左边框
//        cellLeftBoardStyle.setAlignment(HorizontalAlignment.CENTER);    //左右居中
//        cellLeftBoardStyle.setVerticalAlignment(VerticalAlignment.CENTER);    //上下居中
//        //设置字体
//        cellLeftBoardStyle.setFont(optionFont);
//        //设置左边框
//        if (CollectionUtils.isNotEmpty(projectColumnList)) {
//            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
//                for (Integer col : projectColumnList) {
//                    if (i == 0 || i == 2) {
//                        sheet.getRow(i).getCell(col).setCellStyle(cellLeftBoardCenterStyle);
//                    } else if (i == 1) {
//                        sheet.getRow(i).getCell(col).setCellStyle(cellLeftBoardCenterColorStyle);
//                    } else if (sheet.getRow(i).getCell(col).getCellStyle().getFont().getBold()) {
//                        sheet.getRow(i).getCell(col).setCellStyle(cellLeftBoardColorStyle);
//                    } else {
//                        sheet.getRow(i).getCell(col).setCellStyle(cellLeftBoardStyle);
//                    }
//                }
//            }
//        }
//        sheet.createFreezePane(4, 0, 4, 0);
//        return workbook;
//    }
}
