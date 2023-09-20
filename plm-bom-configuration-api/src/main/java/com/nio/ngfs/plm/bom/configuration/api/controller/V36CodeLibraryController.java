package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.annotation.NotLogResult;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.command.v36code.AddV36DigitCommand;
import com.nio.ngfs.plm.bom.configuration.application.command.v36code.AddV36OptionCommand;
import com.nio.ngfs.plm.bom.configuration.application.command.v36code.EditV36DigitCommand;
import com.nio.ngfs.plm.bom.configuration.application.command.v36code.EditV36OptionCommand;
import com.nio.ngfs.plm.bom.configuration.application.query.v36codelibrary.ExportV36CodeLibraryQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.v36codelibrary.QueryV36CodeLibraryQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.v36codelibrary.QueryV36DigitCodeQuery;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmV36CodeLibraryClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.analysis.function.Exp;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@RestController
@RequiredArgsConstructor
public class V36CodeLibraryController implements PlmV36CodeLibraryClient {

    private final AddV36DigitCommand addV36DigitCommand;
    private final EditV36DigitCommand editV36DigitCommand;
    private final AddV36OptionCommand addV36OptionCommand;
    private final EditV36OptionCommand editV36OptionCommand;
    private final QueryV36CodeLibraryQuery queryV36CodeLibraryQuery;
    private final QueryV36DigitCodeQuery queryV36DigitCodeQuery;
    private final ExportV36CodeLibraryQuery exportV36CodeLibraryQuery;

    @Override
    @NotLogResult
    public ResultInfo<AddDigitRespDto> addDigit(@Valid @RequestBody AddDigitCmd cmd) {
        return ResultInfo.success(addV36DigitCommand.execute(cmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<EditDigitRespDto> editDigit(@Valid @RequestBody EditDigitCmd cmd) {
        return ResultInfo.success(editV36DigitCommand.execute(cmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<AddOptionRespDto> addOption(@Valid @RequestBody AddOptionCmd cmd) {
        return ResultInfo.success(addV36OptionCommand.execute(cmd));
    }
    @Override
    @NotLogResult
    public ResultInfo<QueryV36CodeLibraryRespDto> queryV36CodeLibrary(@Valid @RequestBody QueryV36CodeLibraryQry qry) {
        return ResultInfo.success(queryV36CodeLibraryQuery.execute(qry));
    }

    @Override
    @NotLogResult
    public ResultInfo<List<String>> queryV36DigitCode(@Valid @RequestBody QueryV36DigitCodeQry qry) {
        return ResultInfo.success(queryV36DigitCodeQuery.execute(qry));
    }

    @Override
    @NotLogResult
    public ResultInfo<EditOptionRespDto> editOption(@Valid @RequestBody EditOptionCmd cmd) {
        return ResultInfo.success(editV36OptionCommand.execute(cmd));
    }

    @NotLogResult
    @PostMapping("/v36code/exportV36CodeLibrary")
    public void exportV36CodeLibrary(@Valid @RequestBody ExportV36CodeLibraryQry qry, HttpServletResponse response) {
        exportV36CodeLibraryQuery.execute(qry,response);
    }


}
