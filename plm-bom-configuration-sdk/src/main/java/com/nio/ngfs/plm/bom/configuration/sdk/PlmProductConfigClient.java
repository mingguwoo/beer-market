package com.nio.ngfs.plm.bom.configuration.sdk;

import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.AddPcCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.EditPcCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.GetBasedOnPcListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.GetModelListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.AddPcRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.EditPcRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.GetBasedOnPcListRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.GetModelListRespDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@FeignClient(name = "plm-bom-configuration")
public interface PlmProductConfigClient {

    /**
     * 查询Product Config车型列表
     *
     * @param qry 查询
     * @return 响应
     */
    @PostMapping("/productConfig/getModelList")
    ResultInfo<List<GetModelListRespDto>> getModelList(GetModelListQry qry);

    /**
     * 查询Based On PC列表
     *
     * @param qry 查询
     * @return 响应
     */
    @PostMapping("/productConfig/getBasedOnPcList")
    ResultInfo<List<GetBasedOnPcListRespDto>> getBasedOnPcList(GetBasedOnPcListQry qry);

    /**
     * 新增PC
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/productConfig/addPc")
    ResultInfo<AddPcRespDto> addPc(AddPcCmd cmd);

    /**
     * 编辑PC
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/productConfig/editPc")
    ResultInfo<EditPcRespDto> editPc(EditPcCmd cmd);

}
