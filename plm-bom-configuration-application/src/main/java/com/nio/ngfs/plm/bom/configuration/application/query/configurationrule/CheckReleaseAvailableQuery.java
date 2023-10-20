package com.nio.ngfs.plm.bom.configuration.application.query.configurationrule;

import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRuleChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRuleStatusEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.CheckRuleReleaseQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.CheckRuleReleaseAvailableRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author bill.wang
 * @date 2023/10/20
 */
@Component
@RequiredArgsConstructor
public class CheckReleaseAvailableQuery extends AbstractQuery<CheckRuleReleaseQry, CheckRuleReleaseAvailableRespDto> {

    private final BomsConfigurationRuleDao bomsConfigurationRuleDao;

    @Override
    protected CheckRuleReleaseAvailableRespDto executeQuery(CheckRuleReleaseQry qry) {
        //默认false
        CheckRuleReleaseAvailableRespDto respDto = new CheckRuleReleaseAvailableRespDto();
        List<BomsConfigurationRuleEntity> entityList = bomsConfigurationRuleDao.queryByRuleNumber(qry.getRuleNumber());
        Map<Long,BomsConfigurationRuleEntity> entityMap = new HashMap<>();
        String latestRev = new String();
        if (CollectionUtils.isEmpty(entityList)){
            return respDto;
        }
        //将id与entity对应起来并获取最新的revision
        for (int i = 0; i < entityList.size(); i++){
            entityMap.put(entityList.get(i).getId(),entityList.get(i));
            if (latestRev.isEmpty() || (latestRev.compareTo(entityList.get(i).getRuleVersion()) < 0 )){
                //获取最新版本
                latestRev = entityList.get(i).getRuleVersion();
            }
        }
        BomsConfigurationRuleEntity entity = entityMap.get(qry.getId());
        Set<Integer> purposeEnum = new HashSet<>(Arrays.asList(1,2,3,4));
        //校验是否可以出现revise按钮
        if ( purposeEnum.contains(entity.getPurpose()) &&
                Objects.equals(latestRev,entity.getRuleVersion()) &&
                Objects.equals(entity.getStatus(), ConfigurationRuleStatusEnum.RELEASED.getStatus()) &&
                !Objects.equals(entity.getChangeType(), ConfigurationRuleChangeTypeEnum.REMOVE.getChangeType()) ){
                respDto.setAvailable(true);
        }
        return respDto;
    }
}
