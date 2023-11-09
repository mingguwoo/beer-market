package com.nio.ngfs.plm.bom.configuration.application.query.configurationrule;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.utils.DateUtils;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractExportQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.service.ConfigurationRuleQueryService;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.RuleOptionMatrixValueEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.QueryViewQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.RuleViewConstrainedRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.RuleViewHeadInfoRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.RuleViewInfoRespDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author wangchao.wang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExportConfigurationRuleViewQuery extends AbstractExportQuery {


    private final ConfigurationRuleQueryService configurationRuleQueryService;

    public void execute(QueryViewQry qry, HttpServletResponse response, HttpServletRequest request) {
        try {
            OutputStream output = null;
            RuleViewInfoRespDto ruleViewInfoRespDto = configurationRuleQueryService.queryView(qry.getGroupId());
            XSSFWorkbook workbook = new XSSFWorkbook();
            writeConfigureRuleData(ruleViewInfoRespDto, workbook);
            //导出文件名
            String fileName = "Configuration Rule二维表导出.xlsx";
            response.reset();
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader(CommonConstants.HEADER_CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            response.setHeader(CommonConstants.HEADER_ACCESS_CONTROL_EXPOSE_HEADERS, CommonConstants.HEADER_CONTENT_DISPOSITION);
            output = response.getOutputStream();
            workbook.write(output);
        } catch (Exception e) {
            log.error("execute failed e", e);
        }
    }

    /**
     * @description: configuration rule 规则导出数据处理
     * @author: zheng.li.o
     * @create: 2019-04-09
     **/
    private void writeConfigureRuleData(RuleViewInfoRespDto ruleViewInfoRespDto, XSSFWorkbook workbook) {
        //总的行号
        int rowNo = -1;
        //第一行标题列的列号
        int titleCellNoFirst = -1;
        //创建工作簿
        XSSFSheet sheet = workbook.createSheet();
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

        int headSize = ruleViewHeadInfo.getOptionHeadList().size();

        setXSSFRow(sheet, 0, driveFeatureCode, titleCellStyle, headSize);
        setXSSFRow(sheet, 1, driveFeatureName, titleCellStyle, headSize);

        XSSFRow rowTitle = sheet.createRow(2);
        for (int i = 0; i < headSize; i++) {
            XSSFCell cell = rowTitle.createCell(i + 4);
            String effIn = ruleViewHeadInfo.getOptionHeadList().get(i).getEffIn();
            String effOut = ruleViewHeadInfo.getOptionHeadList().get(i).getEffOut();
            cell.setCellValue(effIn + "-" + effOut);
            cell.setCellStyle(titleCellStyle);
        }

        XSSFRow rowChangeTypeTitle = sheet.createRow(3);
        for (int i = 0; i < headSize; i++) {
            XSSFCell cell = rowChangeTypeTitle.createCell(i + 4);
            cell.setCellValue(ruleViewHeadInfo.getOptionHeadList().get(i).getChangeType());
            cell.setCellStyle(titleCellStyle);
        }

        XSSFRow rowRevisionTitle = sheet.createRow(4);
        for (int i = 0; i < headSize; i++) {
            XSSFCell cell = rowRevisionTitle.createCell(i + 4);
            cell.setCellValue(ruleViewHeadInfo.getOptionHeadList().get(i).getRevision());
            cell.setCellStyle(titleCellStyle);
        }

        XSSFRow rowOptionCodeTitle = sheet.createRow(5);
        for (int i = 0; i < headSize; i++) {
            XSSFCell cell = rowOptionCodeTitle.createCell(i + 4);
            cell.setCellValue(ruleViewHeadInfo.getOptionHeadList().get(i).getDriveOptionCode());
            cell.setCellStyle(titleCellStyle);
        }

        XSSFRow rowOptionNameTitle = sheet.createRow(6);
        for (int i = 0; i < headSize; i++) {
            XSSFCell cell = rowOptionNameTitle.createCell(i + 4);
            cell.setCellValue(ruleViewHeadInfo.getOptionHeadList().get(i).getDriveOptionName());
            cell.setCellStyle(titleCellStyle);
        }

        for (int i = 0; i < respDtos.size(); i++) {
            List<RuleViewConstrainedRespDto.RuleViewConstrainedOption> viewConstrainedOptions =
                    respDtos.get(i).getConstrainedOptionList();

            int size = respDtos.get(i).getConstrainedOptionList().size();
            XSSFRow rowConstrainNameTitle = sheet.createRow(7 + i*size);
            sheet.addMergedRegion(new CellRangeAddress(7 + i*size, 6 + i*size + size, 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(7 + i*size, 6 + i*size + size, 1, 1));
            XSSFCell cell = rowConstrainNameTitle.createCell(0);
            cell.setCellValue(respDtos.get(i).getConstrainedFeatureCode());
            XSSFCell cell1 = rowConstrainNameTitle.createCell(1);
            cell1.setCellValue(respDtos.get(i).getConstrainedFeatureName());


            for (int j = 0; j < size; j++) {
                XSSFRow xssfRow = rowConstrainNameTitle;
                if (j != 0) {
                    xssfRow = sheet.createRow(7 + i*size + j);
                }
                XSSFCell cell2 = xssfRow.createCell(2);
                cell2.setCellValue(viewConstrainedOptions.get(j).getConstrainedOptionCode());
                XSSFCell cell3 = xssfRow.createCell(3);
                cell3.setCellValue(viewConstrainedOptions.get(j).getConstrainedOptionName());

                List<RuleViewConstrainedRespDto.RulePackageInfo> rulePackageInfos =
                        viewConstrainedOptions.get(j).getPackageCodes();

                for (int z = 0; z < rulePackageInfos.size(); z++) {
                    XSSFCell cell4 = xssfRow.createCell(4 + z);
                    if(StringUtils.equals("-",rulePackageInfos.get(z).getPackageCode())){
                        cell4.setCellValue("");
                    }else {
                        cell4.setCellValue(RuleOptionMatrixValueEnum.getDisplayValue(rulePackageInfos.get(z).getPackageCode()));
                    }
                    cell4.setCellStyle(titleCellStyle);
                }
            }
        }

    }


    public void setXSSFRow(XSSFSheet sheet, int index, String value, CellStyle titleCellStyle, int size) {
        sheet.addMergedRegion(new CellRangeAddress(index, index, 4, 3 + size));
        XSSFRow rowTitle = sheet.createRow(index);
        XSSFCell cell1 = rowTitle.createCell(4);
        cell1.setCellValue(value);
        cell1.setCellStyle(titleCellStyle);

    }

}

