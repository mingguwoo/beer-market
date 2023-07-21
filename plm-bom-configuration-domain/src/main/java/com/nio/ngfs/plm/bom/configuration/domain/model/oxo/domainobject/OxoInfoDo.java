package com.nio.ngfs.plm.bom.configuration.domain.model.oxo.domainobject;


import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OxoInfoDo extends AbstractDo {

    private String modelCode;


    private String featureCode;

    private Integer ruleCheck;

    private Integer isHead;

    private String comments;

    private Integer sort;

    private Integer sortDelete;

    private String parentFeatureCode;


    private String displayName;

    private String chineseName;

    private String catalog;






}
