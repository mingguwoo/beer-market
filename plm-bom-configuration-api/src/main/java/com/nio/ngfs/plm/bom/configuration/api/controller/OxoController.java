package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nio.bom.share.annotation.NeedAuthorization;
import com.nio.bom.share.annotation.NotLogResult;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.command.oxo.*;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.PageData;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoChangeLogRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListsRespDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final OxoDomainService oxoDomainService;

    private final OxoOperateCommand oxoOperateCommand;

    private final OxoSnapshootCommand oxoSnapshootCommand;

    private final OxoDeleteCommand oxoDeleteCommand;

    private final OxoAddCommand oxoAddCommand;

    private final OxoEditCommand oxoEditCommand;


    /**
     * 根据车型查询快照版本
     * @param cmd
     * @return
     */
    @NeedAuthorization
    @NotLogResult
    @PostMapping("/queryVersion")
    public ResultInfo<List<String>> queryVersion(@Valid @RequestBody OxoBaseCmd cmd) {
        return ResultInfo.success(oxoDomainService.queryVersion(cmd));
    }


    /**
     * 查询oxo列表
     * @param cmd
     * @return
     */
    @PostMapping("/queryList")
    public ResultInfo<OxoListsRespDto> queryList(@Valid @RequestBody OxoListCmd cmd) {
        return ResultInfo.success(oxoDomainService.queryList(cmd));
    }


    /**
     * 批量删除oxo
     * @param cmd
     * @return
     */
    @PostMapping("/delete")
    public ResultInfo delete(@Valid @RequestBody OxoDeleteCmd cmd) {
        oxoDeleteCommand.delete(cmd);
        return  ResultInfo.success(true);
    }


    /**
     * 添加oxo信息
     * @param cmd
     * @return
     */
    @PostMapping("/add")
    public ResultInfo add(@Valid @RequestBody OxoAddCmd cmd) {
        oxoAddCommand.add(cmd);
        return ResultInfo.success(true);
    }


    /**
     * 添加oxo 下拉code列表
     * @return
     */
    @PostMapping("/queryFeatureList")
    public ResultInfo<List<OxoAddCmd>> queryFeatureList() {
        return ResultInfo.success(oxoDomainService.queryFeatureList());
    }


    /**
     * 编辑打点/Rule Check/填写备注
     * @param cmd
     * @return
     */
    @PostMapping("/edit")
    public ResultInfo edit(@Valid @RequestBody OxoEditInfoCmd cmd) {
        oxoEditCommand.edit(cmd);
        return ResultInfo.success(true);
    }



    /**
     * 查询邮件group
     * @return
     */
    @PostMapping("/queryEmailGroup")
    public ResultInfo<List<String>> queryEmailGroup() {
        return ResultInfo.success(oxoDomainService.queryEmailGroup());
    }


    /**
     * 保存快照
     * @param cmd
     * @return
     */
    @PostMapping("/saveSnapshot")
    public ResultInfo saveSnapshot(@Valid @RequestBody OxoSnapshotCmd cmd) {
        oxoSnapshootCommand.saveSnapshot(cmd);
        return ResultInfo.success(true);
    }




    /**
     * todo 排序
     * @param cmd
     * @return
     */
    @PostMapping("/renewSort")
    public ResultInfo renewSort(@Valid @RequestBody OxoSnapshotCmd cmd) {
        oxoDomainService.renewSort(cmd);
        return ResultInfo.success(true);
    }


    /**
     * todo 排序 查询
     * @param cmd
     * @return
     */
    @PostMapping("/querySortFeatureList")
    public ResultInfo querySortFeatureList(@Valid @RequestBody OxoSnapshotCmd cmd) {
        return ResultInfo.success( oxoDomainService.querySortFeatureList(cmd));
    }

    /**
     * 版本对比
     * @param compareCmd
     * @return
     */
    @PostMapping("/compare")
    public ResultInfo<OxoListsRespDto> compare(@Valid @RequestBody OxoCompareCmd compareCmd) {
        return ResultInfo.success(oxoDomainService.compare(compareCmd));
    }



    /**
     * oxo 导出
     * @param cmd
     * @return
     */
    @PostMapping("/export")
    public void  export(@Valid @RequestBody OxoListCmd cmd,HttpServletResponse response) {
        oxoDomainService.export(cmd,response);
    }


    /**
     * oxo 对比导出
     * @param compareCmd
     * @return
     */
    @PostMapping("/compareExport")
    public void  compareExport(@Valid @RequestBody OxoCompareCmd compareCmd,HttpServletResponse response) {
        oxoDomainService.compareExport(compareCmd,response);
    }



    /**
     * 查询changeLog
     * @param oxoBaseCmd
     * @return
     */
    @PostMapping("/queryChangeLog")
    public  ResultInfo<PageData<OxoChangeLogRespDto>> queryChangeLog(@Valid @RequestBody OxoBaseCmd oxoBaseCmd) {
        return ResultInfo.success(oxoDomainService.queryChangeLog(oxoBaseCmd));
    }
}