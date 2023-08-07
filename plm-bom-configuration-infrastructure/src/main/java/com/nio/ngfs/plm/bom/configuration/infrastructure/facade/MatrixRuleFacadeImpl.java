package com.nio.ngfs.plm.bom.configuration.infrastructure.facade;

import com.alibaba.fastjson.JSONObject;
import com.nio.bom.share.result.Result;
import com.nio.ngfs.plm.bom.configuration.domain.facade.MatrixRuleFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.MatrixRuleQueryDto;
import com.nio.ngfs.plm.bom.configuration.remote.CommonClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;



/**
 * @author wangchao.wang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MatrixRuleFacadeImpl implements MatrixRuleFacade {


    private final CommonClient commonClient;

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
    }
}
