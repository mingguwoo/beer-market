package com.nio.ngfs.plm.bom.configuration.application.query.oxo;


import com.nio.bom.share.enums.BrandEnum;
import com.nio.ngfs.plm.bom.configuration.application.query.Query;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.facade.MatrixRuleFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.MatrixRuleQueryDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoQueryGroupQuery implements Query<OxoBaseCmd,List<String>> {


    private final MatrixRuleFacade matrixRuleFacade;

    @Override
    public List<String> execute(OxoBaseCmd cmd) {
        String brandName = ConfigConstants.brandName.get();

        String name = ConfigConstants.OXO_EMAIL_GROUP;
        if (StringUtils.equalsAnyIgnoreCase(brandName, BrandEnum.ALPS.name())) {
            name = ConfigConstants.OXO_EMAIL_GROUP_ALPS;
        }else if(StringUtils.equalsAnyIgnoreCase(brandName, BrandEnum.FY.name())){
            name = ConfigConstants.OXO_EMAIL_GROUP_FY;
        }

        Map<String, String> map = matrixRuleFacade.queryMatrixRuleValuesByAbscissaOrOrdinate(new MatrixRuleQueryDto
                (name, "matrix", null, "oxo.email", "PLM.EBOM.PartNumRequest"));

        List<String> list = new LinkedList<>();
        map.forEach((x, y) -> {
            list.add(x);
        });
        return list.stream().distinct().toList();
    }
}
