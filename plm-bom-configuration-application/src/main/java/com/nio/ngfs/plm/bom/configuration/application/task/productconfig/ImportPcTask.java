package com.nio.ngfs.plm.bom.configuration.application.task.productconfig;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.google.common.collect.Lists;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.ImportPcRespDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * 导入PC历史数据
 *
 * @author xiaozhou.tu
 * @date 2023/10/11
 */
@Component
@RequiredArgsConstructor
public class ImportPcTask {

    public ImportPcRespDto execute(MultipartFile file) {
        // 读取历史数据
        List<PcHistory> pcHistoryList = readData(file);
        return new ImportPcRespDto();
    }

    /**
     * 读取历史数据
     */
    private List<PcHistory> readData(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             ExcelReader excelReader = EasyExcel.read(new BufferedInputStream(inputStream)).excelType(ExcelTypeEnum.XLSX).build()) {
            PcReadListener readListener = new PcReadListener();
            // 读取Sheet 0
            ReadSheet readSheet = EasyExcel.readSheet(0).head(PcHistory.class)
                    .registerReadListener(readListener)
                    .headRowNumber(1).build();
            // 开始读取Sheet
            excelReader.read(Lists.newArrayList(readSheet));
            return readListener.getPcHistoryList();
        } catch (Exception e) {
            throw new BusinessException(ConfigErrorCode.EXCEL_UPLOAD_ERROR, e.getMessage());
        }
    }

    private static class PcReadListener implements ReadListener<PcHistory> {

        private final List<PcHistory> pcHistoryList = Lists.newArrayList();

        @Override
        public void invoke(PcHistory pcHistory, AnalysisContext analysisContext) {
            pcHistoryList.add(pcHistory);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        }

        public List<PcHistory> getPcHistoryList() {
            return pcHistoryList;
        }

    }

    @Data
    @NoArgsConstructor
    private static class PcHistory {

        @ExcelProperty("PC Id")
        private String pcId;

        @ExcelProperty("Name")
        private String pcName;

    }

}
