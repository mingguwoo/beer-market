package com.nio.ngfs.plm.bom.configuration.domain.model.common;


import java.util.Map;

/**
 * @author wangchao.wang
 */

public interface CommonRepository {


    Map queryMatrixRuleValuesByAbscissaOrOrdinate(MatrixRuleQueryDo param);


}
