package com.nio.ngfs.plm.bom.configuration.remote.dto.common;

import lombok.Data;

import java.util.Map;


@Data
public class SendEmailRequest {


    private String templateNo;


    private String subject;


    private String receiverEmail;


    private Map<String, Object> variables;

}
