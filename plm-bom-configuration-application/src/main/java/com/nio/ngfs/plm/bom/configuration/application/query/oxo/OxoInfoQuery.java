package com.nio.ngfs.plm.bom.configuration.application.query.oxo;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.application.query.Query;
import com.nio.ngfs.plm.bom.configuration.application.query.oxo.assemble.OxoInfoAssembler;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoFeatureOptionApplicationService;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.basevehicle.BaseVehicleDomainService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoOptionPackageDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoOptionPackageEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoInfoQuery implements Query<OxoBaseCmd, OxoListQry> {



    private final OxoFeatureOptionApplicationService featureOptionApplicationService;


    @Override
    public OxoListQry execute(OxoBaseCmd oxoListQry) {
        return featureOptionApplicationService.queryOxoInfoByModelCode(oxoListQry.getModelCode());
    }
}
