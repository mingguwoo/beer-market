package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.alibaba.fastjson.JSONObject;
import com.nio.bom.share.result.Result;
import com.nio.ngfs.common.utils.BeanConvertUtils;
import com.nio.ngfs.plm.bom.configuration.domain.model.common.CommonRepository;
import com.nio.ngfs.plm.bom.configuration.remote.CommonClientFeign;
import com.nio.ngfs.plm.bom.configuration.remote.IntegrationClient;
import com.nio.ngfs.plm.bom.configuration.remote.dto.common.SendEmailRequest;
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


    private final IntegrationClient integrationClient;

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


    /**
     * 发送邮件
     * @param emailParamDo
     */
    @Override
    public void sendEmail(EmailParamDo emailParamDo) {

        Result result =
                integrationClient.sendEmail(BeanConvertUtils.convertTo(emailParamDo, SendEmailRequest::new));

        log.info("sendEmail request：{}，response：{}", JSONObject.toJSONString(emailParamDo),
                JSONObject.toJSONString(result));
    }
}
