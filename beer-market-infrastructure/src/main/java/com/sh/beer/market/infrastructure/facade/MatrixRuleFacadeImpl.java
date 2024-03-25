package com.sh.beer.market.infrastructure.facade;

import com.sh.beer.market.domain.facade.FeatureFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;



/**
 * @author
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MatrixRuleFacadeImpl implements FeatureFacade {


    /*private final CommonClient commonClient;

    @Override
    public Map queryMatrixRuleValuesByAbscissaOrOrdinate(MatrixRuleQueryDto param) {
        String bizData = JSONObject.toJSONString(param);
        Result data = commonClient.getMatrixRuleValuesByAbscissaOrOrdinate(bizData);
        log.info("查询MatrixRuleValuesByAbscissaOrOrdinate 请求参数：{}，返回结果：{}", bizData, data);

        if (data.isError()) {
            return new HashMap();
        }

        Map map = (Map) data.getResultData();
        return (Map<String, String>) map.get("dataValue");
    }*/
}
