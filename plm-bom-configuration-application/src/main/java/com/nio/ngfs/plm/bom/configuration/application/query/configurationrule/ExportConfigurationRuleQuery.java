package com.nio.ngfs.plm.bom.configuration.application.query.configurationrule;

import com.google.common.collect.Lists;
import com.nio.bom.share.utils.DateUtils;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractExportQuery;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRulePurposeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRuleTypeEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.ExportConfigurationRuleQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.ConfigurationGroupDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.ConfigurationRuleDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.CriteriaDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.QueryConfigurationRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author bill.wang
 * @date 2023/10/25
 */
@Component
@RequiredArgsConstructor
public class ExportConfigurationRuleQuery extends AbstractExportQuery {

    private static final List<String> TITLE_LIST = Lists.newArrayList(
            "Chinese Name", "Display Name", "Driving Criteria", "Constrained Criteria",
            "Purpose", "Rule Type", "Defined by", "Eff-in", "Eff-out", "Rule ID", "Rule Version",
            "Description", "Change Type", "Release Date", "Type", "Create User", "Create Date",
            "Update User", "Update Date", "Status"
    );

    private final QueryConfigurationRuleQuery queryConfigurationRuleQuery;

    public void execute(ExportConfigurationRuleQry qry, HttpServletResponse response) {

        QueryConfigurationRuleRespDto queryConfigurationRuleRespDto = queryConfigurationRuleQuery.executeQuery(qry);
        String prefix = (Objects.isNull(qry.getModelYear()) ? qry.getModel():qry.getModel()+"_"+qry.getModelYear());
        String fileName = prefix + "_Configuration Rule_"+ DateUtils.dateTimeNow("yyyyMMddHHmm") + ".xlsx";
        export(response, fileName, workbook -> exportConfigurationRule(queryConfigurationRuleRespDto,workbook));
    }

    /**
     * configuration rule导出到excel
     * @param queryConfigurationRuleRespDto
     * @param workbook
     */
    private void exportConfigurationRule(QueryConfigurationRuleRespDto queryConfigurationRuleRespDto, XSSFWorkbook workbook){
        XSSFSheet sheet = workbook.createSheet();
        configSheetStyle(sheet);
        setSheetTitle(workbook, sheet, TITLE_LIST);
        writeConfigurationRow(queryConfigurationRuleRespDto, workbook, sheet);
    }

    /**
     * 写Configuration Rule行数据
     * @param dto
     * @param sheet
     */
    private void writeConfigurationRow(QueryConfigurationRuleRespDto dto, XSSFWorkbook workbook, XSSFSheet sheet) {
        XSSFCellStyle groupCellStyle = createConfigurationRuleCellStyle(workbook, true);
        XSSFCellStyle ruleCellStyle = createConfigurationRuleCellStyle(workbook, false);
        int rowIndex = 1;
        for(ConfigurationGroupDto group: dto.getGroup()){
            //先弄group
            int columnIndex = -1;
            SimpleDateFormat cstFormat = new SimpleDateFormat(DateUtils.EEE_MMM_dd_HH_mm_ss_zz_yyyy, Locale.US);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            try{
                Date groupCreateTime = cstFormat.parse(group.getCreateTime());
                Date groupUpdateTime = cstFormat.parse(group.getUpdateTime());
                XSSFRow row = sheet.createRow(rowIndex++);

                createCell(row, ++columnIndex, group.getChineseName(),groupCellStyle);
                createCell(row, ++columnIndex, group.getDisplayName(),groupCellStyle);
                createCell(row, ++columnIndex, null, groupCellStyle);
                createCell(row, ++columnIndex, null, groupCellStyle);
                createCell(row, ++columnIndex, ConfigurationRulePurposeEnum.getByCode(group.getPurpose()).toString(),groupCellStyle);
                createCell(row, ++columnIndex, null, groupCellStyle);
                createCell(row, ++columnIndex, group.getDefinedBy(),groupCellStyle);
                createCell(row, ++columnIndex, null, groupCellStyle);
                createCell(row, ++columnIndex, null, groupCellStyle);
                createCell(row, ++columnIndex, null, groupCellStyle);
                createCell(row, ++columnIndex, null, groupCellStyle);
                createCell(row, ++columnIndex, group.getDescription(),groupCellStyle);
                createCell(row, ++columnIndex, null, groupCellStyle);
                createCell(row, ++columnIndex, null, groupCellStyle);
                createCell(row, ++columnIndex, "Group",groupCellStyle);
                createCell(row, ++columnIndex, group.getCreateUser(),groupCellStyle);
                createCell(row, ++columnIndex, dateFormat.format(groupCreateTime),groupCellStyle);
                createCell(row, ++columnIndex, group.getUpdateUser(),groupCellStyle);
                createCell(row, ++columnIndex, dateFormat.format(groupUpdateTime),groupCellStyle);
                createCell(row, ++columnIndex, null, groupCellStyle);
                //再弄rule
                for(ConfigurationRuleDto rule:group.getRule()){
                    Date ruleEffIn = cstFormat.parse(rule.getEffIn());
                    Date ruleEffOut = cstFormat.parse(rule.getEffOut());
                    Date ruleCreateTime = cstFormat.parse(rule.getCreateTime());
                    Date ruleUpdateTime = cstFormat.parse(rule.getUpdateTime());
                    Date ruleReleaseDate = cstFormat.parse(rule.getReleaseDate());
                    String drivingCriteria = produceCriteriaList(rule.getDrivingCriteria());
                    String constrainedCriteria = produceCriteriaList(rule.getConstrainedCriteria());
                    int ruleColumnIndex = -1;
                    XSSFRow ruleRow = sheet.createRow(rowIndex++);
                    createCell(ruleRow, ++ruleColumnIndex, group.getChineseName(),ruleCellStyle);
                    createCell(ruleRow, ++ruleColumnIndex, group.getDisplayName(),ruleCellStyle);
                    createCell(ruleRow, ++ruleColumnIndex, drivingCriteria,ruleCellStyle);
                    createCell(ruleRow, ++ruleColumnIndex, constrainedCriteria,ruleCellStyle);
                    createCell(ruleRow, ++ruleColumnIndex, ConfigurationRulePurposeEnum.getByCode(group.getPurpose()).toString(),null);
                    createCell(ruleRow, ++ruleColumnIndex, rule.getRuleType(),ruleCellStyle);
                    createCell(ruleRow, ++ruleColumnIndex, group.getDefinedBy(),ruleCellStyle);
                    createCell(ruleRow, ++ruleColumnIndex, dateFormat.format(ruleEffIn),ruleCellStyle);
                    createCell(ruleRow, ++ruleColumnIndex, dateFormat.format(ruleEffOut),ruleCellStyle);
                    createCell(ruleRow, ++ruleColumnIndex, rule.getRuleNumber(),ruleCellStyle);
                    createCell(ruleRow, ++ruleColumnIndex, rule.getRuleRevision(),ruleCellStyle);
                    createCell(ruleRow, ++ruleColumnIndex, null, ruleCellStyle);
                    createCell(ruleRow, ++ruleColumnIndex, rule.getChangeType(),ruleCellStyle);
                    createCell(ruleRow, ++ruleColumnIndex, dateFormat.format(ruleReleaseDate), ruleCellStyle);
                    createCell(ruleRow, ++ruleColumnIndex, "Rule",ruleCellStyle);
                    createCell(ruleRow, ++ruleColumnIndex, rule.getCreateUser(),ruleCellStyle);
                    createCell(ruleRow, ++ruleColumnIndex, dateFormat.format(ruleCreateTime),ruleCellStyle);
                    createCell(ruleRow, ++ruleColumnIndex, rule.getUpdateUser(),ruleCellStyle);
                    createCell(ruleRow, ++ruleColumnIndex, dateFormat.format(ruleUpdateTime),ruleCellStyle);
                    createCell(ruleRow, ++ruleColumnIndex, rule.getStatus(),ruleCellStyle);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 生成criteria
     * @param dtoList
     * @return
     */
    private String produceCriteriaList(List<CriteriaDto> dtoList){
        List<String> criteriaList = dtoList.stream().map(dto->dto.getOptionCode()).distinct().toList();
        StringBuilder criteriaString = new StringBuilder();
        criteriaList.forEach(criteria->{
            if (criteriaString.isEmpty()){
                criteriaString.append(criteria);
            }
            else{
                criteriaString.append("&" + criteria);
            }
        });
        return criteriaString.toString();
    }

    private XSSFCellStyle createConfigurationRuleCellStyle(XSSFWorkbook workbook, boolean isGroup) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        if (isGroup) {
            cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        }
        else {
            cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        }
        return cellStyle;
    }

}
