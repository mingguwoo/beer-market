package com.nio.ngfs.plm.bom.configuration.infrastructure.facade;


import com.nio.ngfs.plm.bom.configuration.common.constants.BaseConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.BrandEnum;
import com.nio.ngfs.plm.bom.configuration.domain.facade.AdministratorDetailFacade;
import com.nio.ps.third.party.lanka.client.LankaApiClient;
import com.nio.ps.third.party.lanka.model.data.AdministratorDetailData;
import com.nio.ps.third.party.lanka.model.request.AdministratorDetailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AdministratorDetailFacadeImpl implements AdministratorDetailFacade {



    private  final  LankaApiClient lankaApiClient;


    /**
     * 根据用户名称获取roleNames
     * @param userName
     * @return
     */
    @Override
    public List<String> queryRoleNamesByUserName(String userName) {
        try {
            AdministratorDetailRequest request = new AdministratorDetailRequest();
            request.setDomainAccount(userName);
            request.setLesseeCode(BaseConstants.brandName.get());


            AdministratorDetailData detailData = lankaApiClient.getAdministratorDetail(request);

            return detailData.getRoles().stream().map(AdministratorDetailData.PeopleRole::getCode).distinct().toList();

        } catch (Exception e) {
            log.error("getAdministratorDetail error",e);
        }
        return Lists.newArrayList();
    }
}
