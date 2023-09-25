package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.annotation.NeedAuthorization;
import com.nio.bom.share.annotation.NotLogResult;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.command.oxo.*;
import com.nio.ngfs.plm.bom.configuration.application.query.oxo.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.PageData;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.DeleteFeatureOptionRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoChangeLogRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.RenewSortFeatureOptionRespDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author wangchao.wang
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/oxo")
public class OxoController {

    private final OxoSnapshotCommand oxoSnapshotCommand;
    private final DeleteFeatureOptionCommand deleteFeatureOptionCommand;
    private final OxoAddCommand oxoAddCommand;
    private final OxoEditCommand oxoEditCommand;
    private final RenewSortFeatureOptionCommand renewSortFeatureOptionCommand;
    private final OxoVersionQuery oxoVersionQuery;

    private final OxoInfoExportQuery oxoInfoExportQuery;
    private final OxoCompareExportInfoQuery compareExportQuery;

    private final OxoInfoQuery oxoInfoQuery;
    private final OxoFeatureOptionQuery oxoFeatureOptionQuery;
    private final OxoQueryGroupQuery oxoQueryGroupQuery;
    private final OxoCompareQuery oxoCompareQuery;
    private final OxoChangeLogQuery oxoChangeLogQuery;

    /**
     * 根据车型查询快照版本
     *
     * @param cmd
     * @return
     */
    @NeedAuthorization
    @NotLogResult
    @PostMapping("/queryVersion")
    public ResultInfo<List<String>> queryVersion(@Valid @RequestBody OxoBaseCmd cmd) {
        return ResultInfo.success(oxoVersionQuery.execute(cmd));
    }

    /**
     * 查询oxo列表
     *
     * @param cmd
     * @return
     */
    //@NeedAuthorization
    @NotLogResult
    @PostMapping("/queryList")
    public ResultInfo<OxoListRespDto> queryList(@Valid @RequestBody OxoBaseCmd cmd) {
        return ResultInfo.success(oxoInfoQuery.execute(cmd));
    }

    /**
     * 删除Feature/Option行
     *
     * @param cmd 命令
     * @return 结果
     */
    @NeedAuthorization
    @NotLogResult
    @PostMapping("/deleteFeatureOption")
    public ResultInfo<DeleteFeatureOptionRespDto> deleteFeatureOption(@Valid @RequestBody DeleteFeatureOptionCmd cmd) {
        return ResultInfo.success(deleteFeatureOptionCommand.execute(cmd));
    }

    /**
     * 添加oxo信息
     *
     * @param cmd
     * @return
     */
    @NeedAuthorization
    @NotLogResult
    @PostMapping("/add")
    public ResultInfo add(@Valid @RequestBody OxoAddCmd cmd) {
        return ResultInfo.success(oxoAddCommand.execute(cmd));
    }

    /**
     * 添加oxo 下拉code列表
     *
     * @return
     */
    @NeedAuthorization
    @NotLogResult
    @PostMapping("/queryFeatureList")
    public ResultInfo<OxoAddCmd> queryFeatureList(@Valid @RequestBody OxoBaseCmd cmd) {
        return ResultInfo.success(oxoFeatureOptionQuery.execute(cmd));
    }

    /**
     * 编辑打点/Rule Check/填写备注
     *
     * @param cmd
     * @return
     */
    @NeedAuthorization
    @NotLogResult
    @PostMapping("/edit")
    public ResultInfo edit(@Valid @RequestBody OxoEditInfoCmd cmd) {
        return ResultInfo.success(oxoEditCommand.execute(cmd));
    }

    /**
     * 查询邮件group
     *
     * @return
     */
    //@NeedAuthorization
    @NotLogResult
    @PostMapping("/queryEmailGroup")
    public ResultInfo<List<String>> queryEmailGroup() {
        return ResultInfo.success(oxoQueryGroupQuery.execute(new OxoBaseCmd()));
    }

    /**
     * 保存快照
     *
     * @param cmd
     * @return
     */
   // @NeedAuthorization
    @NotLogResult
    @PostMapping("/saveSnapshot")
    public ResultInfo saveSnapshot(@Valid @RequestBody OxoSnapshotCmd cmd) {
        return ResultInfo.success(oxoSnapshotCommand.execute(cmd));
    }

    /**
     * Renew Sort
     *
     * @param cmd 命令
     * @return 结果
     */
    @NeedAuthorization
    @NotLogResult
    @PostMapping("/renewSortFeatureOption")
    public ResultInfo<RenewSortFeatureOptionRespDto> renewSortFeatureOption(@Valid @RequestBody RenewSortFeatureOptionCmd cmd) {
        return ResultInfo.success(renewSortFeatureOptionCommand.execute(cmd));
    }


    /**
     * 版本对比
     *
     * @param oxoCompareQry
     * @return
     */
    @PostMapping("/compare")
    public ResultInfo<OxoListRespDto> compare(@Valid @RequestBody OxoCompareQry oxoCompareQry) {
        return ResultInfo.success(oxoCompareQuery.execute(oxoCompareQry));
    }

    /**
     * oxo 导出
     *
     * @param cmd
     * @return
     */
    @PostMapping("/export")
    public void export(@Valid @RequestBody OxoBaseCmd cmd, HttpServletResponse response, HttpServletRequest request) {
        oxoInfoExportQuery.execute(cmd, request, response);
    }

    /**
     * oxo 对比导出
     *
     * @param compareCmd
     * @return
     */
    //@NeedAuthorization
    @NotLogResult
    @PostMapping("/compareExport")
    public void compareExport(@Valid @RequestBody OxoCompareQry compareCmd,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        compareExportQuery.execute(compareCmd, request, response);


    }

    /**
     * 查询changeLog
     *
     * @param oxoBaseCmd
     * @return
     */
    //@NeedAuthorization
    @NotLogResult
    @PostMapping("/queryChangeLog")
    public ResultInfo<PageData<OxoChangeLogRespDto>> queryChangeLog(@Valid @RequestBody OxoBaseCmd oxoBaseCmd) {
        return ResultInfo.success(oxoChangeLogQuery.execute(oxoBaseCmd));
    }
}
