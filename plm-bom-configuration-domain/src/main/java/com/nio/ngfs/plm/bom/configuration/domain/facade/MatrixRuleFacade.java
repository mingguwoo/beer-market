package com.nio.ngfs.plm.bom.configuration.domain.facade;

import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.MatrixRuleQueryDto;

import java.util.Map;

/**
 * @author wangchao.wang
 */
public interface MatrixRuleFacade {




    public Map queryMatrixRuleValuesByAbscissaOrOrdinate(MatrixRuleQueryDto param);
}
