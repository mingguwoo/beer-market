package com.nio.ngfs.plm.bom.configuration.application.command.productContext;

import com.nio.ngfs.plm.bom.configuration.application.service.ProductContextApplicationService;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoVersionSnapshotDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productContext.request.AddProductContextCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productContext.response.AddProductContextRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
@Component
@RequiredArgsConstructor
public class AddProductContextCommand {

    private final OxoVersionSnapshotDomainService oxoVersionSnapshotDomainService;
    private final ProductContextApplicationService productContextApplicationService;

    public AddProductContextRespDto execute(AddProductContextCmd cmd){
        OxoListQry oxoListQry = oxoVersionSnapshotDomainService.resolveSnapShot(cmd.getOxoSnapshot());
        return new AddProductContextRespDto();
    }
}
