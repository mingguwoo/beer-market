package com.nio.ngfs.plm.bom.configuration.application.task.productContext;

import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductContextDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductContextFeatureDao;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ImportProductContextRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author bill.wang
 * @date 2023/8/31
 */
@Component
@RequiredArgsConstructor
public class ImportProductContextTask {

    private final BomsProductContextDao bomsProductContextDao;
    private final BomsProductContextFeatureDao bomsProductContextFeatureDao;

    public ImportProductContextRespDto execute(MultipartFile file){
        //读取历史数据
        return new ImportProductContextRespDto();
    }

}
