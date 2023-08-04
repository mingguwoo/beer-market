package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditCmd;
import lombok.Data;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Data
public class OxoRowsQry implements Qry {

        private String featureCode;

        private String displayName;

        private String chineseName;

        private String group;

        private String comments;

        private Long headId;

        private String ruleCheck;

        private String catalog;

        private Integer isSortDelete;


        /**
         * 对比
         * @see com.nio.ngfs.plm.bom.configuration.domain.model.oxo.enums.CompareChangeTypeEnum
         */
        private String changeType;

        /**
         * featureCode下面 optionCode
         */
        private List<OxoRowsQry> options;

        /**
         * 打点信息
         */
        private List<OxoEditCmd> packInfos;
}
