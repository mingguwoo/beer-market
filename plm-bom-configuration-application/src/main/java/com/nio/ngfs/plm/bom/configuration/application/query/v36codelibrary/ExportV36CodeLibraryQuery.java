package com.nio.ngfs.plm.bom.configuration.application.query.v36codelibrary;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractExportQuery;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.ExportV36CodeLibraryQry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author bill.wang
 * @date 2023/9/20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExportV36CodeLibraryQuery extends AbstractExportQuery {

    private static final List<String> TITLE_LIST = Lists.newArrayList(
            "V36 Code ID", "Code", "Display Name", "Chinese Name", "Type", "Sales Feature",
            "Display Name", "Chinese Name", "Remark", "Creator", "Originated", "Update User", "Last Modified"
    );
    public void execute(ExportV36CodeLibraryQry qry, HttpServletResponse response) {

    }
}
