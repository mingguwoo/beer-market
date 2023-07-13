package com.nio.ngfs.plm.bom.configuration.domain.service.oxo;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.PageData;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoChangeLogRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListsRespDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author wangchao.wang
 */
public interface OxoDomainService {

    /**
     * 根据车型查询所有的版本
     * @param cmd
     * @return
     */
    List<String> queryVersion(OxoBaseCmd cmd);

    /**
     * 查询oxo列表
     * @param cmd
     * @return
     */
    OxoListsRespDto queryList(OxoListCmd cmd);

    /**
     * 分页查询 发布历史
     * @param oxoBaseCmd
     * @return
     */
    PageData<OxoChangeLogRespDto> queryChangeLog(OxoBaseCmd oxoBaseCmd);

    /**
     * 对比导出
     * @param compareCmd
     * @param response
     */
    void compareExport(OxoCompareCmd compareCmd, HttpServletResponse response);

    /**
     * oxo页面导出
     * @param cmd
     * @param response
     */
    void export(OxoListCmd cmd, HttpServletResponse response);


    /**
     * 版本对比
     * @param compareCmd
     * @return
     */
    OxoListsRespDto compare(OxoCompareCmd compareCmd);


    /**
     * 添加oxo 下拉code列表
     * @return
     */
    List<OxoAddCmd> queryFeatureList();


    /**
     *  查询邮件group
     * @return
     */
    List<String> queryEmailGroup();


    /**
     * 排序
     * @param cmd
     */
    void renewSort(OxoSnapshotCmd cmd);


    Object querySortFeatureList(OxoSnapshotCmd cmd);
}
