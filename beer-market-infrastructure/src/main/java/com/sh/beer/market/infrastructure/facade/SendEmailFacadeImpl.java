package com.sh.beer.market.infrastructure.facade;


import com.sh.beer.market.domain.facade.EmailFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class SendEmailFacadeImpl implements EmailFacade {

    /*private final IntegrationClient integrationClient;


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

    }*/
}
