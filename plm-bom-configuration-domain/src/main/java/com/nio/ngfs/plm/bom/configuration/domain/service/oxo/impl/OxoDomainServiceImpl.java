package com.nio.ngfs.plm.bom.configuration.domain.service.oxo.impl;

import com.nio.ngfs.plm.bom.configuration.domain.facade.AdministratorDetailFacade;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.PageData;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoChangeLogRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListsRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author wangchao.wang
 */
@Service
@RequiredArgsConstructor
public class OxoDomainServiceImpl implements OxoDomainService {



    private final AdministratorDetailFacade administratorDetailFacade;



    @Override
    public List<String> queryVersion(OxoBaseCmd cmd) {
        String userName= cmd.getUserName();


        List<String> roleNames =administratorDetailFacade.queryRoleNamesByUserName(userName);




        return null;
    }

    @Override
    public OxoListsRespDto queryList(OxoListCmd cmd) {
        return null;
    }

    @Override
    public PageData<OxoChangeLogRespDto> queryChangeLog(OxoBaseCmd oxoBaseCmd) {
        return null;
    }

    @Override
    public void compareExport(OxoCompareCmd compareCmd, HttpServletResponse response) {

    }

    @Override
    public void export(OxoListCmd cmd, HttpServletResponse response) {

    }

    @Override
    public OxoListsRespDto compare(OxoCompareCmd compareCmd) {
        return null;
    }

    @Override
    public List<OxoAddCmd> queryFeatureList() {
        return null;
    }

    @Override
    public List<String> queryEmailGroup() {
        return null;
    }

    @Override
    public void renewSort(OxoSnapshotCmd cmd) {

    }

    @Override
    public Object querySortFeatureList(OxoSnapshotCmd cmd) {
        return null;
    }
}
