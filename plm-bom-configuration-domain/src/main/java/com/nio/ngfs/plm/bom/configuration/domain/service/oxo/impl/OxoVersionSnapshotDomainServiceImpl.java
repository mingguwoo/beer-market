package com.nio.ngfs.plm.bom.configuration.domain.service.oxo.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoVersionSnapshotDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OxoVersionSnapshotDomainServiceImpl implements OxoVersionSnapshotDomainService {



    private final OxoVersionSnapshotRepository oxoVersionSnapshotRepository;



    @Override
    public String queryVersionByModelCode(String modelCode,String type) {



        List<OxoVersionSnapshotAggr> oxoVersionSnapshots=
                oxoVersionSnapshotRepository.queryOxoVersionSnapshotByModelCode(modelCode);


        // 根据 车型获取 oxo版本
        if(CollectionUtils.isNotEmpty(oxoVersionSnapshots)){

            //List<String>  versions =oxoVersionSnapshots.stream().sorted(Comparator.comparing(OxoVersionSnapshotAggr::getVersion).reversed()).toList();

        }


        return null;
    }
}
