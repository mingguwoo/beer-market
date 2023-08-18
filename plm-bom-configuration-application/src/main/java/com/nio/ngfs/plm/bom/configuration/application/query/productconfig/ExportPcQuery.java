package com.nio.ngfs.plm.bom.configuration.application.query.productconfig;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.nio.bom.share.utils.DateUtils;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractExportQuery;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.ExportPcQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.QueryPcRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 导出PC
 *
 * @author xiaozhou.tu
 * @date 2023/8/17
 */
@Component
@RequiredArgsConstructor
public class ExportPcQuery extends AbstractExportQuery {

    /**
     * Excel标题列表
     */
    private static final List<String> TITLE_LIST = Lists.newArrayList(
            "PC Id", "Model", "Model Year", "Name", "Based On-Base Vehicle",
            "Based On-PC Id", "Create User", "Create Date", "Update User", "Update Date"
    );
    private static final String FILE_NAME_FORMAT_1 = "%s_Base PC_%s.xlsx";
    private static final String FILE_NAME_FORMAT_2 = "%s_%s_Base PC_%s.xlsx";

    private final QueryPcQuery queryPcQuery;

    public void execute(ExportPcQry qry, HttpServletResponse response) {
        // 查询PC列表
        List<QueryPcRespDto> pcList = queryPcQuery.execute(qry);
        export(response, generateFileName(qry), workbook -> exportPc(pcList, workbook));
    }

    /**
     * 生成文件名
     */
    private String generateFileName(ExportPcQry qry) {
        String dateTime = DateUtils.dateTimeNow("yyyyMMddHHmm");
        if (CollectionUtils.isEmpty(qry.getModelYearList())) {
            return String.format(FILE_NAME_FORMAT_1, qry.getModel(), dateTime);
        } else {
            return String.format(FILE_NAME_FORMAT_2, qry.getModel(), Joiner.on("-").skipNulls().join(qry.getModelYearList()), dateTime);
        }
    }

    /**
     * PC导出到Excel
     */
    private void exportPc(List<QueryPcRespDto> pcList, XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.createSheet();
        configSheetStyle(sheet);
        setSheetTitle(workbook, sheet, TITLE_LIST);
        writePcRow(pcList, sheet);
    }

    @Override
    protected void configSheetStyle(XSSFSheet sheet) {
        super.configSheetStyle(sheet);
        // 自定义列宽
        sheet.setColumnWidth(0, 20 * 256);
        sheet.setColumnWidth(3, 30 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(9, 20 * 256);
    }

    /**
     * 写PC行数据
     */
    private void writePcRow(List<QueryPcRespDto> pcList, XSSFSheet sheet) {
        int rowIndex = 1;
        for (QueryPcRespDto pc : pcList) {
            int columnIndex = -1;
            XSSFRow row = sheet.createRow(rowIndex++);
            createCell(row, ++columnIndex, pc.getPcId(), null);
            createCell(row, ++columnIndex, pc.getModel(), null);
            createCell(row, ++columnIndex, pc.getModelYear(), null);
            createCell(row, ++columnIndex, pc.getName(), null);
            createCell(row, ++columnIndex, pc.getBasedOnBaseVehicle() != null ? pc.getBasedOnBaseVehicle().toString() : "", null);
            createCell(row, ++columnIndex, pc.getBasedOnPcId(), null);
            createCell(row, ++columnIndex, pc.getCreateUser(), null);
            createCell(row, ++columnIndex, pc.getCreateTime(), null);
            createCell(row, ++columnIndex, pc.getUpdateUser(), null);
            createCell(row, ++columnIndex, pc.getUpdateTime(), null);
        }
    }

}
