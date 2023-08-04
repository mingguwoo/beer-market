package com.nio.ngfs.plm.bom.configuration.domain.facade;

import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.EmailParamDto;

/**
 * @author wangchao.wang
 */
public interface EmailFacade {


      /**
       * 发送邮件
       * @param emailParamDto
       */
      void sendEmail(EmailParamDto emailParamDto);




}
