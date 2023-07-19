package com.nio.ngfs.plm.bom.configuration.application.query.feature;

import com.nio.ngfs.plm.bom.configuration.application.query.Query;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.GetGroupCodeListQry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/19
 */
@Component
@RequiredArgsConstructor
public class GetGroupCodeListQuery implements Query<GetGroupCodeListQry, List<String>> {

    @Override
    public List<String> execute(GetGroupCodeListQry getGroupCodeListQry) {
        return null;
    }

}
