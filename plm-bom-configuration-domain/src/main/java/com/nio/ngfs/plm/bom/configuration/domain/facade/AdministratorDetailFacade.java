package com.nio.ngfs.plm.bom.configuration.domain.facade;

import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.GetColorCodeReqDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.response.GetColorCodeRespDto;

import java.util.List;

/**
 * @author wangchao.wang
 */
public interface AdministratorDetailFacade {


    /**
     *  根据用户名称获取roleNames
     * @param userName
     * @return
     */
    List<String> queryRoleNamesByUserName(String userName);
}
