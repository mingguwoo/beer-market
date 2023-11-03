package com.nio.ngfs.plm.bom.configuration.application.query.configurationrule;

import com.nio.ngfs.plm.bom.configuration.application.query.AbstractExportQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.service.ConfigurationRuleQueryService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.QueryViewQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.RuleViewConstrainedRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.RuleViewHeadInfoRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.RuleViewInfoRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author wangchao.wang
 */

@Component
@RequiredArgsConstructor
public class ExportConfigurationRuleViewQuery extends AbstractExportQuery {


    private final ConfigurationRuleQueryService configurationRuleQueryService;

    public void execute(QueryViewQry qry, HttpServletResponse response, HttpServletRequest request) {

        RuleViewInfoRespDto ruleViewInfoRespDto = configurationRuleQueryService.queryView(qry.getGroupId());


    }

    /**
     * @description: configuration rule 规则导出数据处理
     * @author: zheng.li.o
     * @create: 2019-04-09
     **/
    private void writeConfigureRuleData(RuleViewInfoRespDto ruleViewInfoRespDto, HSSFWorkbook workbook) {
        //总的行号
        int rowNo = -1;
        //第一行标题列的列号
        int titleCellNoFirst = -1;
        //创建工作簿
        HSSFSheet sheet = workbook.createSheet();
        sheet.setDefaultRowHeightInPoints(40);
        sheet.setDefaultColumnWidth(4);
        sheet.setDefaultColumnWidth(20);
        //设置标题的样式
        CellStyle titleCellStyle = workbook.createCellStyle();
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleCellStyle.setWrapText(true);
        //设置背景色
//        titleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        titleCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        //设置导出数据的样式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);

        List<RuleViewConstrainedRespDto> respDtos = ruleViewInfoRespDto.getRuleViewConstrainedLists();
        RuleViewHeadInfoRespDto ruleViewHeadInfo = ruleViewInfoRespDto.getHeadInfoRespDto();
        String driveFeatureCode = ruleViewHeadInfo.getDriveFeatureCode();
        String driveFeatureName = ruleViewHeadInfo.getDriveFeatureName();

        HSSFRow rowTitle = sheet.createRow(1);
        HSSFCell cell1 = rowTitle.createCell(++titleCellNoFirst);
        cell1.setCellValue("");


        //创建标题行(共两行)
//        HSSFRow rowTitle = sheet.createRow(++rowNo);
//        //写标题
//        HSSFCell cell1 = rowTitle.createCell(++titleCellNoFirst);
//        cell1.setCellValue("Feature");
//        cell1.setCellStyle(titleCellStyle);
//        HSSFCell cell2 = rowTitle.createCell(++titleCellNoFirst);
//        cell2.setCellValue("Option");
//        cell2.setCellStyle(titleCellStyle);
//        HSSFCell cell3 = rowTitle.createCell(++titleCellNoFirst);
//        cell3.setCellValue(out.getFeatureName() + "\r\n" + out.getFeatureCode());
//        cell3.setCellStyle(titleCellStyle);
//        HSSFCell cell4 = rowTitle.createCell(++titleCellNoFirst);
//        cell4.setCellValue("");
//        cell4.setCellStyle(titleCellStyle);
//        //第二行标题列的列号
//        int secondtitleCellNoFirst = -1;
//        HSSFRow secondRow = sheet.createRow(++rowNo);
//        HSSFCell cell5 = secondRow.createCell(++secondtitleCellNoFirst);
//        cell5.setCellValue("");
//        cell5.setCellStyle(titleCellStyle);
//        HSSFCell cell6 = secondRow.createCell(++secondtitleCellNoFirst);
//        cell6.setCellValue("");
//        cell6.setCellStyle(titleCellStyle);
//        for (int i = 0; i < optionNameList.size(); i++) {
//            HSSFCell cell7 = secondRow.createCell(++secondtitleCellNoFirst);
//            cell7.setCellValue(optionNameList.get(i) + "\r\n" + out.getOptionCodeList().get(i));
//            cell7.setCellStyle(titleCellStyle);
//        }
//
//        // 合并单元格
//        CellRangeAddress cra = new CellRangeAddress(0, 0, 2, optionNameList.size() > 1 ? 1 + optionNameList.size() : 2 + optionNameList.size()); // 起始行, 终止行, 起始列, 终止列
//        sheet.addMergedRegionUnsafe(cra);
//        CellRangeAddress cra2 = new CellRangeAddress(0, 1, 0, 0); // 起始行, 终止行, 起始列, 终止列
//        sheet.addMergedRegionUnsafe(cra2);
//        CellRangeAddress cra3 = new CellRangeAddress(0, 1, 1, 1); // 起始行, 终止行, 起始列, 终止列
//        sheet.addMergedRegionUnsafe(cra3);
//
//        for (ConfigurationFeature configurationFeature : featureLists) {
//            List<ConfigurationOption> children = configurationFeature.getChildren();
//
//            for (int i = 0; i < children.size(); i++) {
//                HSSFRow row = sheet.createRow(++rowNo);
//                int cellNumber = -1;
//                int colspanNum = rowNo + children.size() - 1;
//                if (i == 0) {
//                    HSSFCell cell8 = row.createCell(++cellNumber);
//                    cell8.setCellValue(configurationFeature.getFeatureName() + "\n" + configurationFeature.getFeatureCode());
//                    cell8.setCellStyle(cellStyle);
//                    //合并单元格区域只有一个单元格时，不合并
//                    if (rowNo == colspanNum) {
//                        sheet.addMergedRegionUnsafe(new CellRangeAddress(rowNo, colspanNum + 1, 0, 0));
//                    } else {
//                        CellRangeAddress cra4 = new CellRangeAddress(rowNo, colspanNum, 0, 0); // 起始行, 终止行, 起始列, 终止列
//                        sheet.addMergedRegionUnsafe(cra4);
//                    }
//                } else {
//                    HSSFCell cell8 = row.createCell(++cellNumber);
//                    cell8.setCellValue("");
//                    cell8.setCellStyle(cellStyle);
//                }
//
//                HSSFCell cell9 = row.createCell(++cellNumber);
//                cell9.setCellValue(children.get(i).getOptionName() + "\n" + children.get(i).getOptionCode());
//                cell9.setCellStyle(cellStyle);
//
//                List<String> defaultValueList = children.get(i).getDefaultValue();
//                List<String> availableList = children.get(i).getAvailable();
//                List<String> notAvailableList = children.get(i).getNotAvailable();
//
//                // 符号样式特殊设置
//                CellStyle cellStyleSpecial = workbook.createCellStyle();
//                cellStyleSpecial.setAlignment(HorizontalAlignment.CENTER);
//                cellStyleSpecial.setVerticalAlignment(VerticalAlignment.CENTER);
//                HSSFFont font = workbook.createFont();
//                font.setFontHeightInPoints((short) 18);// 设置字号
//                for (String optionName : optionNameList) {
//                    HSSFCell cell10 = row.createCell(++cellNumber);
//                    if (availableList.contains(optionName)) {
//                        cell10.setCellValue("○");
//                    } else if (notAvailableList.contains(optionName)) {
//                        cell10.setCellValue("×");
//                    } else if (defaultValueList.contains(optionName)) {
//                        cell10.setCellValue("●");
//                    }
//                    ((HSSFCellStyle) cellStyleSpecial).setFont(font);
//                    cell10.setCellStyle(cellStyleSpecial);
//
//                }
//            }
//        }
//    }


//    public void set(HSSFRow rowTitle){
//        HSSFCell cell1 = rowTitle.createCell(0);
//        cell1.setCellValue("Feature");
//    }
//}

    }
}
