package com.nio.ngfs.plm.bom.configuration.application.query.v36codelibrary;

import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.enums.V36CodeLibraryTypeEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsV36CodeLibraryDao;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.QueryV36DigitCodeQry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/9/19
 */
@Component
@RequiredArgsConstructor
public class QueryV36DigitCodeQuery {

    private final BomsV36CodeLibraryDao bomsV36CodeLibraryDao;

    public List<String> execute(QueryV36DigitCodeQry qry){
        return bomsV36CodeLibraryDao.queryByType(V36CodeLibraryTypeEnum.DIGIT.getType()).stream().map(entity->entity.getCode()).distinct().sorted().toList();
    }
}
