package com.nio.ngfs.plm.bom.configuration.application.query.productconfig;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.utils.DateUtils;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractExportQuery;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.ExportProductConfigQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.QueryProductConfigRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 导出ProductConfig
 *
 * @author xiaozhou.tu
 * @date 2023/9/7
 */
@Component
@RequiredArgsConstructor
public class ExportProductConfigQuery extends AbstractExportQuery {

    /**
     * Excel固定标题列表
     */
    private static final List<String> TITLE_LIST = Lists.newArrayList(
            "Feature Code", "Feature Display Name", "Option Code", "Option Display Name",
            "Option Chinese Name"
    );
    private static final String FILE_NAME_FORMAT_1 = "%s_Product Configuration_%s.xlsx";
    private static final String FILE_NAME_FORMAT_2 = "%s_%s_Product Configuration_%s.xlsx";

    private final QueryProductConfigQuery queryProductConfigQuery;

    public void execute(ExportProductConfigQry qry, HttpServletResponse response) {
        // 查询ProductConfig列表
        qry.setEdit(false);
        QueryProductConfigRespDto respDto = queryProductConfigQuery.executeQuery(qry);
        export(response, generateFileName(qry), workbook -> exportProductConfig(respDto, workbook));
    }

    /**
     * 生成文件名
     */
    private String generateFileName(ExportProductConfigQry qry) {
        String dateTime = DateUtils.dateTimeNow("yyyyMMddHHmm");
        if (CollectionUtils.isEmpty(qry.getModelYearList())) {
            return String.format(FILE_NAME_FORMAT_1, qry.getModel(), dateTime);
        } else {
            return String.format(FILE_NAME_FORMAT_2, qry.getModel(), Joiner.on("-").skipNulls().join(qry.getModelYearList()), dateTime);
        }
    }

    /**
     * ProductConfig导出到Excel
     */
    private void exportProductConfig(QueryProductConfigRespDto respDto, XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.createSheet();
        Map<Integer, Integer> maxColumnWithMap = Maps.newHashMap();
        setSheetTitle(workbook, sheet, respDto, maxColumnWithMap);
        writeProductConfigRow(workbook, sheet, respDto, maxColumnWithMap);
        // 设置自适应宽度
        maxColumnWithMap.forEach((columnIndex, width) -> sheet.setColumnWidth(columnIndex, width * 256));
    }

    /**
     * 设置标题
     */
    private void setSheetTitle(XSSFWorkbook workbook, XSSFSheet sheet, QueryProductConfigRespDto respDto, Map<Integer, Integer> maxColumnWithMap) {
        List<String> titleList = Lists.newArrayList(TITLE_LIST);
        // 动态添加PC列表头
        respDto.getPcList().forEach(pc -> titleList.add(pc.getPcId()));
        super.setSheetTitle(workbook, sheet, titleList, 30, true);
        for (int i = 0; i < titleList.size(); i++) {
            maxColumnWithMap.put(i, Math.max(maxColumnWithMap.getOrDefault(i, 0), getStrLength(titleList.get(i))));
        }
    }

    /**
     * 写ProductConfig行数据
     */
    private void writeProductConfigRow(XSSFWorkbook workbook, XSSFSheet sheet, QueryProductConfigRespDto respDto, Map<Integer, Integer> maxColumnWithMap) {
        int featureRowIndex = 1;
        AtomicInteger optionRowIndex = new AtomicInteger(1);
        for (QueryProductConfigRespDto.FeatureDto featureDto : respDto.getFeatureList()) {
            writeProductConfigFeature(workbook, sheet, featureDto, featureRowIndex++, optionRowIndex, maxColumnWithMap);
        }
    }

    /**
     * 写Feature行
     */
    private void writeProductConfigFeature(XSSFWorkbook workbook, XSSFSheet sheet, QueryProductConfigRespDto.FeatureDto featureDto,
                                           int featureRowIndex, AtomicInteger optionRowIndex, Map<Integer, Integer> maxColumnWithMap) {
        int firstOptionRow = optionRowIndex.get();
        featureDto.getOptionList().forEach(option ->
                writeProductConfigOption(workbook, sheet, featureDto, option, featureRowIndex, optionRowIndex, maxColumnWithMap)
        );
        int lastOptionRow = optionRowIndex.get();
        if (firstOptionRow >= lastOptionRow - 1) {
            return;
        }
        // 合并Feature单元格
        for (int i = 0; i < CommonConstants.INT_TWO; i++) {
            CellRangeAddress mergeCell = new CellRangeAddress(firstOptionRow, lastOptionRow - 1, i, i);
            sheet.addMergedRegion(mergeCell);
        }
    }

    /**
     * 写Option行
     */
    private void writeProductConfigOption(XSSFWorkbook workbook, XSSFSheet sheet, QueryProductConfigRespDto.FeatureDto featureDto,
                                          QueryProductConfigRespDto.OptionDto optionDto, int featureRowIndex, AtomicInteger optionRowIndex,
                                          Map<Integer, Integer> maxColumnWithMap) {
        XSSFRow row = sheet.createRow(optionRowIndex.get());
        optionRowIndex.incrementAndGet();
        // 每隔一个Feature设置灰底背景色
        boolean grey = featureRowIndex % CommonConstants.INT_TWO == 1;
        int columnIndex = -1;
        createCell(row, ++columnIndex, featureDto.getFeatureCode(), createCellStyle(workbook, grey, false, true), maxColumnWithMap);
        createCell(row, ++columnIndex, featureDto.getDisplayName(), createCellStyle(workbook, grey, false, true), maxColumnWithMap);
        createCell(row, ++columnIndex, optionDto.getOptionCode(), createCellStyle(workbook, grey, false, false), maxColumnWithMap);
        createCell(row, ++columnIndex, optionDto.getDisplayName(), createCellStyle(workbook, grey, false, false), maxColumnWithMap);
        createCell(row, ++columnIndex, optionDto.getChineseName(), createCellStyle(workbook, grey, false, false), maxColumnWithMap);
        for (QueryProductConfigRespDto.PcOptionConfigDto optionConfigDto : optionDto.getConfigList()) {
            createCell(row, ++columnIndex, (optionConfigDto.isSelect() ? "√" : ""), createCellStyle(workbook, grey, true, false), maxColumnWithMap);
        }
    }

    /**
     * 创建单元格样式
     */
    private XSSFCellStyle createCellStyle(XSSFWorkbook workbook, boolean grey, boolean middle, boolean verticalMiddle) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        if (grey) {
            cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        if (middle) {
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
        }
        if (verticalMiddle) {
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        }
        return cellStyle;
    }

}
