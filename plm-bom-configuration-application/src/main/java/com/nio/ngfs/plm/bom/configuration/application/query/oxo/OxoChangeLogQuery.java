package com.nio.ngfs.plm.bom.configuration.application.query.oxo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nio.ngfs.plm.bom.configuration.application.query.Query;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.enums.OxoSnapshotEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoVersionSnapshotDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoVersionSnapshotEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.PageData;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoChangeLogRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoChangeLogQuery implements Query<OxoBaseCmd, PageData<OxoChangeLogRespDto>> {


    private final BomsOxoVersionSnapshotDao bomsOxoVersionSnapshotDao;

    @Override
    public PageData<OxoChangeLogRespDto> execute(OxoBaseCmd oxoBaseCmd) {


        String modelCode= oxoBaseCmd.getModelCode();

        Page<BomsOxoVersionSnapshotEntity> page =bomsOxoVersionSnapshotDao.querySnapshotByModel(
                modelCode, OxoSnapshotEnum.FORMAL.getCode(),oxoBaseCmd.getPageSize(),oxoBaseCmd.getOffSet() );


        List<OxoChangeLogRespDto> oxoChangeLogs = new ArrayList<>();

        if(CollectionUtils.isNotEmpty(page.getRecords())){

            page.getRecords().forEach(x->{
                OxoChangeLogRespDto changeLogRespDto=new OxoChangeLogRespDto();
                changeLogRespDto.setChangeContent(x.getChangeContent());
                changeLogRespDto.setOwner(x.getCreateUser());
                changeLogRespDto.setVersion(x.getVersion());
                changeLogRespDto.setTitle(x.getTitle());
                changeLogRespDto.setReleaseData(x.getCreateTime());
                oxoChangeLogs.add(changeLogRespDto);
            });
        }

        return new PageData<>(oxoBaseCmd.getOffSet(),oxoBaseCmd.getPageSize(),(int) page.getTotal(),oxoChangeLogs);

    }
}
