package com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bill.wang
 * @date 2023/8/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OxoVersionReleaseEvent {

    String oxoSnapshot;
}
