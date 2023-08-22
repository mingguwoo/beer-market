package com.nio.ngfs.plm.bom.configuration.infrastructure.facade;

import com.alibaba.fastjson.JSONObject;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.result.Result;
import com.nio.ngfs.common.utils.BeanConvertUtils;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
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

        Result result = null;

        try {
            result =
                    integrationClient.sendEmail(BeanConvertUtils.convertTo(emailParamDto, SendEmailRequest::new));
        } catch (Exception e) {
            log.error("sendEmail error", e);
            throw new BusinessException(ConfigErrorCode.EMAIL_ERROR);
        }

        log.info("sendEmail request：{}，response：{}", JSONObject.toJSONString(emailParamDto),
                JSONObject.toJSONString(result));

        if (result.isError()) {
            log.error("sendEmail error,request：{}，response：{}", JSONObject.toJSONString(emailParamDto), JSONObject.toJSONString(result));
            throw new BusinessException(ConfigErrorCode.EMAIL_ERROR);
        }

    }
}
