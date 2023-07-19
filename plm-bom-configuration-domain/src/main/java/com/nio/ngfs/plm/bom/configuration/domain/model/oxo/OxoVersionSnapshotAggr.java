package com.nio.ngfs.plm.bom.configuration.domain.model.oxo;

import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangchao.wang
 */
@Slf4j
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OxoVersionSnapshotAggr extends AbstractDo {


    private String modelCode;

    private String version;

    private String oxoSnapshot;

    private String type;

    private String brand;

    private String title;

    private String changeContent;

    private String emailGroup;


}
