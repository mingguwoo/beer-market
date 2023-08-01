package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.alibaba.fastjson.JSONObject;
import com.nio.bom.share.result.Result;
import com.nio.ngfs.plm.bom.configuration.domain.model.common.CommonRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.common.MatrixRuleQueryDo;
import com.nio.ngfs.plm.bom.configuration.remote.CommonClientFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;


/**
 * @author wangchao.wang
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class CommonRepositoryImpl implements CommonRepository {

    private final CommonClientFeign commonClientFeign;

    @Override
    public Map queryMatrixRuleValuesByAbscissaOrOrdinate(MatrixRuleQueryDo param) {
        String bizData = JSONObject.toJSONString(param);
        Result data = commonClientFeign.getMatrixRuleValuesByAbscissaOrOrdinate(bizData);
        log.info("查询MatrixRuleValuesByAbscissaOrOrdinate 请求参数：{}，返回结果：{}", bizData, data);

        if (data.isError()) {
           return new HashMap();
        }

        Map map = (Map) data.getResultData();
        return (Map<String, String>) map.get("dataValue");
    }
}
