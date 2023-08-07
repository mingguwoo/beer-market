package com.nio.ngfs.plm.bom.configuration.infrastructure.facade;

import com.alibaba.fastjson.JSONObject;
import com.nio.bom.share.result.Result;
import com.nio.ngfs.common.utils.BeanConvertUtils;
import com.nio.ngfs.plm.bom.configuration.domain.facade.EmailFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.EmailParamDto;
import com.nio.ngfs.plm.bom.configuration.remote.IntegrationClient;
import com.nio.ngfs.plm.bom.configuration.remote.dto.common.SendEmailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wangchao.wang
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class SendEmailFacadeImpl implements EmailFacade {

    private final IntegrationClient integrationClient;


    @Override
    public void sendEmail(EmailParamDto emailParamDto) {
        Result result =
                integrationClient.sendEmail(BeanConvertUtils.convertTo(emailParamDto, SendEmailRequest::new));

        log.info("sendEmail request：{}，response：{}", JSONObject.toJSONString(emailParamDto),
                JSONObject.toJSONString(result));

    }
}
