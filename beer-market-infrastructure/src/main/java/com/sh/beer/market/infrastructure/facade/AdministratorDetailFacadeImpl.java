package com.sh.beer.market.infrastructure.facade;

import com.sh.beer.market.domain.facade.AdministratorDetailFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AdministratorDetailFacadeImpl implements AdministratorDetailFacade {

    /*private final LankaApiClient lankaApiClient;

    *//**
     * 根据用户名称获取roleNames
     * @param userName
     * @return
     *//*
    @Override
    public List<String> queryRoleNamesByUserName(String userName) {
        try {
            AdministratorDetailRequest request = new AdministratorDetailRequest();
            request.setDomainAccount(userName);
            request.setLesseeCode(ConfigConstants.brandName.get());


            AdministratorDetailData detailData = lankaApiClient.getAdministratorDetail(request);

            return detailData.getRoles().stream().map(AdministratorDetailData.PeopleRole::getCode).distinct().toList();

        } catch (Exception e) {
            log.error("getAdministratorDetail error",e);
        }
        return Lists.newArrayList();
    }*/
}
