package com.nio.ngfs.plm.bom.configuration.domain.service.oxo;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.PageData;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoAddCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoCompareCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoSnapshotCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoChangeLogRespDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author wangchao.wang
 */
public interface OxoDomainService {

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
    //void export(OxoListCmd cmd, HttpServletResponse response);


    /**
     * 版本对比
     * @param comp areCmd
     * @return
     */
    //OxoListsRes compare(OxoCompareCmd compareCmd);


    /**
     * 添加oxo 下拉code列表
     * @return
     */
    OxoAddCmd queryFeatureList(OxoBaseCmd cmd);


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
