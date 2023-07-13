package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditCmd;
import lombok.Data;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Data
public class OxoRowsRespDto implements Dto {



        private String featureCode;


        private String displayName;


        private String chineseName;


        private String group;


        private String comments;


        private Long id;


        private String ruleCheck;
        

        private String catalog;

        /**
         * featureCode下面 optionCode
         */
        private List<OxoRowsRespDto> options;


        /**
         * 打点信息
         */
        private List<OxoEditCmd> packInfos;





}
