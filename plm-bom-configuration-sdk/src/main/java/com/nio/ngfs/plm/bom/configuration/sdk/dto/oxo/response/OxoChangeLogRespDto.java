package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response;


import com.alibaba.fastjson2.annotation.JSONField;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author wangchao.wang
 */
@Getter
@Setter
@ToString
public class OxoChangeLogRespDto implements Dto {



    private String version;

    private String title;

    private String changeContent;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date releaseData;


    private String owner;


}
