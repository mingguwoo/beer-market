package com.nio.ngfs.plm.bom.configuration.application.query.feature;

import com.nio.bom.share.enums.StatusEnum;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.GetGroupCodeListQry;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 查询Group Code列表
 *
 * @author xiaozhou.tu
 * @date 2023/7/19
 */
@Component
@RequiredArgsConstructor
public class GetGroupCodeListQuery extends AbstractQuery<GetGroupCodeListQry, List<String>> {

    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;

    @Override
    protected void validate(GetGroupCodeListQry qry) {
        // 校验status
        if (qry.getStatus() != null && StatusEnum.getByStatus(qry.getStatus()) == null) {
            throw new BusinessException(ConfigErrorCode.FEATURE_STATUS_INVALID);
        }
    }

    @Override
    public List<String> executeQuery(GetGroupCodeListQry qry) {
        List<BomsFeatureLibraryEntity> groupList = bomsFeatureLibraryDao.getGroupList();
        if (CollectionUtils.isEmpty(groupList)) {
            return Collections.emptyList();
        }
        // 过滤和排序
        return groupList.stream().filter(i -> qry.getStatus() == null || Objects.equals(qry.getStatus(), i.getStatus()))
                .map(BomsFeatureLibraryEntity::getFeatureCode)
                .sorted(String::compareTo)
                .toList();
    }

}
