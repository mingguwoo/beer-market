package com.nio.ngfs.plm.bom.configuration.application.query.configurationrule;

import cn.hutool.core.lang.Pair;
import com.nio.bom.share.utils.DateUtils;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.service.ConfigurationRuleQueryService;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRuleChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRuleStatusEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleGroupDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleGroupEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleOptionEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.QueryConfigurationRuleQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.ConfigurationGroupDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.ConfigurationRuleDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.CriteriaDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.QueryConfigurationRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author bill.wang
 * @date 2023/10/23
 */
@Component
@RequiredArgsConstructor
public class QueryConfigurationRuleQuery extends AbstractQuery<QueryConfigurationRuleQry, QueryConfigurationRuleRespDto> {

    private final BomsConfigurationRuleGroupDao bomsConfigurationRuleGroupDao;
    private final BomsConfigurationRuleDao bomsConfigurationRuleDao;
    private final BomsConfigurationRuleOptionDao bomsConfigurationRuleOptionDao;
    private final ConfigurationRuleQueryService configurationRuleQueryService;

    @Override
    protected QueryConfigurationRuleRespDto executeQuery(QueryConfigurationRuleQry qry) {
        String definedBy;
        QueryConfigurationRuleRespDto respDto = new QueryConfigurationRuleRespDto();
        Set<Long> groupMatchSet = new HashSet<>();
        Set<Long> ruleMatchSet = new HashSet<>();
        Set<Long> extraGroupSet = new HashSet<>();
        Map<Long,BomsConfigurationRuleGroupEntity> groupEntityMap = new HashMap<>();
        Map<Long,BomsConfigurationRuleEntity> ruleEntityMap = new HashMap<>();
        Map<Long,ConfigurationGroupDto> groupDtoMap = new HashMap<>();
        Map<Long,List<BomsConfigurationRuleOptionEntity>> ruleOptionMap = new HashMap<>();
        Map<String,String> ruleRevisionMap = new HashMap<>();
        Map<String, BomsFeatureLibraryEntity> featureOptionMap = configurationRuleQueryService.queryFeatureOptionMap();
        if (Objects.isNull(qry.getModelYear())){
            definedBy = qry.getModel();
        }
        else {
            definedBy = qry.getModel() + " " + qry.getModelYear();
        }
        //先获取model modelYear的全部group
        List<BomsConfigurationRuleGroupEntity> groupEntityList = bomsConfigurationRuleGroupDao.queryByDefinedBy(definedBy);

        //根据groupId获取这些group下的全部rule
        List<Long> groupId = groupEntityList.stream().map(entity->entity.getId()).collect(Collectors.toList());
        List<BomsConfigurationRuleEntity> ruleEntityList = bomsConfigurationRuleDao.queryByGroupId(groupId);

        //遍历group，将id和Entity对应上，方便后面进行rule的筛选时选中Group。同时，根据displayName和chineseName来进行一遍搜索并记录下符合的group。
        groupEntityList.forEach(entity->{
            groupEntityMap.put(entity.getId(),entity);
            ConfigurationGroupDto dto = buildGroup(entity);
            groupDtoMap.put(dto.getId(),dto);
            //如果group符合条件，就记录下来并存进去，以防后面判断rule时重复
            if (matchSearch(entity.getChineseName(),qry.getSearchContent()) || matchSearch(entity.getDisplayName(),qry.getSearchContent())){
                groupMatchSet.add(entity.getId());
                respDto.getGroup().add(dto);
            }
        });

        //遍历Rule，如果属于已经被选中的group，就直接添加，如果没有的话，就判断一下ruleId并添加对应的group和rule。
        ruleEntityList.forEach(entity->{
            ruleEntityMap.put(entity.getId(),entity);
            //属于被选中的group
            if (groupMatchSet.contains(entity.getGroupId())){
                ConfigurationRuleDto dto = buildRule(entity);
                groupDtoMap.get(entity.getGroupId()).getRule().add(dto);
            }
            //记录下符合条件的rule
            else {
                if (matchSearch(entity.getRuleNumber(),qry.getSearchContent())){
                    ruleMatchSet.add(entity.getId());
                }
            }
            //记录下最新版本，留待后面设置revise按钮
            if (Objects.isNull(ruleRevisionMap.get(entity.getRuleNumber()))
                    || entity.getRuleNumber().compareTo(ruleRevisionMap.get(entity.getRuleNumber())) > 0)
                ruleRevisionMap.put(entity.getRuleNumber(),entity.getRuleVersion());
        });

        //查询所有和rule相关的constrained criteria和driving criteria的code/chineseName/displayName信息
        List<BomsConfigurationRuleOptionEntity> optionEntityList = bomsConfigurationRuleOptionDao.queryByRuleIdList(ruleEntityList.stream().map(entity->entity.getId()).toList());
        optionEntityList.forEach(entity->{
            if (matchSearch(entity.getConstrainedFeatureCode(),qry.getSearchContent()) || matchSearch(entity.getConstrainedOptionCode(),qry.getSearchContent())
            || (matchSearch(entity.getDrivingFeatureCode(),qry.getSearchContent()) || matchSearch(entity.getDrivingOptionCode(),qry.getSearchContent()))
            || matchSearch(featureOptionMap.get(entity.getConstrainedOptionCode()).getDisplayName(),qry.getSearchContent()) || matchSearch(featureOptionMap.get(entity.getConstrainedOptionCode()).getChineseName(),qry.getSearchContent())
            || matchSearch(featureOptionMap.get(entity.getConstrainedFeatureCode()).getDisplayName(),qry.getSearchContent()) || matchSearch(featureOptionMap.get(entity.getConstrainedFeatureCode()).getChineseName(),qry.getSearchContent())
            || matchSearch(featureOptionMap.get(entity.getDrivingOptionCode()).getDisplayName(),qry.getSearchContent()) || matchSearch(featureOptionMap.get(entity.getDrivingOptionCode()).getChineseName(),qry.getSearchContent())
            || matchSearch(featureOptionMap.get(entity.getDrivingFeatureCode()).getDisplayName(),qry.getSearchContent()) || matchSearch(featureOptionMap.get(entity.getDrivingFeatureCode()).getChineseName(),qry.getSearchContent())){
                ruleMatchSet.add(entity.getRuleId());
            }
            //将所有rule对应的criteria记录下来
            if (Objects.isNull(ruleOptionMap.get(entity.getRuleId()))){
                List<BomsConfigurationRuleOptionEntity> optionList = new ArrayList<>();
                optionList.add(entity);
                ruleOptionMap.put(entity.getRuleId(),optionList);
            }
            else{
                ruleOptionMap.get(entity.getRuleId()).add(entity);
            }

        });

        //遍历ruleMatchSet，将要补充的rule和group补充进去
        ruleMatchSet.forEach(ruleId->{
            if (!groupMatchSet.contains(ruleEntityMap.get(ruleId).getGroupId())){
                //如果要新建group，就新建一下
                if (!extraGroupSet.contains(ruleEntityMap.get(ruleId).getGroupId())){
                    ConfigurationGroupDto groupDto = buildGroup(groupEntityMap.get(ruleEntityMap.get(ruleId).getGroupId()));
                    groupDtoMap.put(groupDto.getId(),groupDto);
                    respDto.getGroup().add(groupDto);
                    extraGroupSet.add(groupDto.getId());
                }
                //如果只要新创建rule的
                ConfigurationRuleDto ruleDto = buildRule(ruleEntityMap.get(ruleId));
                groupDtoMap.get(ruleEntityMap.get(ruleId).getGroupId()).getRule().add(ruleDto);
            }
        });

        //遍历RespDto里的所有rule，设置Revise按钮和Constrained Criteria和Driving Criteria
        Set<Integer> reviseSet = new HashSet<>(Arrays.asList(1,2,3,4));
        respDto.getGroup().forEach(group->{
            group.getRule().forEach(rule->{
                if (reviseSet.contains(rule.getPurpose()) && Objects.equals(ruleRevisionMap.get(rule.getRuleNumber()),rule.getRuleRevision())
                        && Objects.equals(rule.getStatus(), ConfigurationRuleStatusEnum.RELEASED.getStatus()) && !Objects.equals(rule.getChangeType(), ConfigurationRuleChangeTypeEnum.REMOVE.getChangeType())){
                    rule.setReviseAvailable(true);
                }
                ruleOptionMap.get(rule.getId()).forEach(optionEntity->{
                    Pair<CriteriaDto,CriteriaDto> criteriaPair = buildCriteria(optionEntity,featureOptionMap);
                    rule.getDrivingCriteria().add(criteriaPair.getKey());
                    rule.getDrivingCriteria().add(criteriaPair.getValue());
                });

            });
        });
        return respDto;
    }

    private Pair<CriteriaDto,CriteriaDto> buildCriteria(BomsConfigurationRuleOptionEntity optionEntity,Map<String, BomsFeatureLibraryEntity> featureOptionMap){
        CriteriaDto drivingCriteria = new CriteriaDto();
        CriteriaDto constrainedCriteria = new CriteriaDto();
        drivingCriteria.setFeatureCode(optionEntity.getDrivingFeatureCode());
        drivingCriteria.setFeatureChineseName(featureOptionMap.get(optionEntity.getDrivingFeatureCode()).getChineseName());
        drivingCriteria.setFeatureDisplayName(featureOptionMap.get(optionEntity.getDrivingFeatureCode()).getDisplayName());
        drivingCriteria.setOptionCode(optionEntity.getDrivingOptionCode());
        drivingCriteria.setOptionDisplayName(featureOptionMap.get(optionEntity.getDrivingOptionCode()).getDisplayName());
        drivingCriteria.setOptionDisplayName(featureOptionMap.get(optionEntity.getDrivingOptionCode()).getDisplayName());
        constrainedCriteria.setFeatureCode(optionEntity.getConstrainedFeatureCode());
        constrainedCriteria.setFeatureChineseName(featureOptionMap.get(optionEntity.getConstrainedFeatureCode()).getChineseName());
        constrainedCriteria.setFeatureDisplayName(featureOptionMap.get(optionEntity.getConstrainedFeatureCode()).getDisplayName());
        constrainedCriteria.setOptionCode(optionEntity.getConstrainedOptionCode());
        constrainedCriteria.setOptionChineseName(featureOptionMap.get(optionEntity.getConstrainedOptionCode()).getChineseName());
        constrainedCriteria.setOptionDisplayName(featureOptionMap.get(optionEntity.getConstrainedOptionCode()).getDisplayName());
        return Pair.of(drivingCriteria,constrainedCriteria);
    }

    private ConfigurationRuleDto buildRule(BomsConfigurationRuleEntity entity){
        ConfigurationRuleDto dto = new ConfigurationRuleDto();
        dto.setId(entity.getId());
        dto.setGroupId(entity.getGroupId());
        dto.setPurpose(entity.getPurpose());
        dto.setChangeType(entity.getChangeType());
        dto.setRuleNumber(entity.getRuleNumber());
        dto.setRuleType(entity.getRuleType());
        dto.setEffIn(String.valueOf(entity.getEffIn()));
        dto.setEffOut(String.valueOf(entity.getEffOut()));
        dto.setReleaseDate(String.valueOf(entity.getReleaseDate()));
        dto.setRuleRevision(entity.getRuleVersion());
        dto.setCreateUser(entity.getCreateUser());
        dto.setCreateTime(String.valueOf(entity.getCreateTime()));
        dto.setUpdateUser(entity.getUpdateUser());
        dto.setUpdateTime(String.valueOf(entity.getUpdateTime()));
        dto.setStatus(entity.getStatus());
        return dto;
    }

    private ConfigurationGroupDto buildGroup(BomsConfigurationRuleGroupEntity entity){
        ConfigurationGroupDto dto = new ConfigurationGroupDto();
        dto.setId(entity.getId());
        dto.setChineseName(entity.getChineseName());
        dto.setDisplayName(entity.getDisplayName());
        dto.setPurpose(entity.getPurpose());
        dto.setDefinedBy(entity.getDefinedBy());
        dto.setDescription(entity.getDescription());
        dto.setCreateUser(entity.getCreateUser());
        dto.setCreateTime(String.valueOf(entity.getCreateTime()));
        dto.setUpdateUser(entity.getUpdateUser());
        dto.setUpdateTime(String.valueOf(entity.getUpdateTime()));
        return dto;
    }

    private boolean matchSearch(String content, String search){
        if (Objects.nonNull(search)){
            return content != null && content.toUpperCase().contains(search.toUpperCase());
        }
        return true;
    }

    private boolean matchTime(String effIn, String effOut, String begin, String end) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.YYYY_MM_DD_HH_MM_SS);
        if (Objects.isNull(begin) && Objects.isNull(end)){
            Date effInDate = dateFormat.parse(effIn);
            Date effOutDate = dateFormat.parse(effOut);
            Date beginDate = dateFormat.parse(begin);
            Date endDate = dateFormat.parse(end);
            return ((beginDate.after(effInDate) ||  beginDate.equals(effInDate)) && (endDate.before(effOutDate) || endDate.equals(effOutDate)));
        }
        return true;
    }
}
