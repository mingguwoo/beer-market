package com.sh.beer.market.remote;

import org.springframework.cloud.openfeign.FeignClient;


//@FeignClient(name = "plm-portal-middle-integration")
public interface IntegrationClient {


    /**
     * 发送邮件通用API V3
     * JsonFlagWithOutSid=true 代表请求的时候不会校验sid
     *
     * @param sendEmailRequest 发送邮件参数
     * @return
     *//*
    @PostMapping(value = "integration/common/sendEmailV3", headers = {"JsonFlagWithOutSid=true"})
    Result sendEmail(@RequestBody SendEmailRequest sendEmailRequest);*/
}
