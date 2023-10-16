package com.nio.ngfs.plm.bom.configuration.sdk;

import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.*;
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
     * 查询Based On Base Vehicle列表
     *
     * @param qry 查询
     * @return 响应
     */
    @PostMapping("/productConfig/getBasedOnBaseVehicleList")
    ResultInfo<List<GetBasedOnBaseVehicleListRespDto>> getBasedOnBaseVehicleList(GetBasedOnBaseVehicleListQry qry);

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

    /**
     * 删除PC
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/productConfig/deletePc")
    ResultInfo<DeletePcRespDto> deletePc(DeletePcCmd cmd);

    /**
     * 查询PC列表
     *
     * @param qry 查询
     * @return 响应
     */
    @PostMapping("/productConfig/queryPc")
    ResultInfo<List<QueryPcRespDto>> queryPc(QueryPcQry qry);

    /**
     * 查询Product Config列表
     *
     * @param qry 查询
     * @return 响应
     */
    @PostMapping("/productConfig/queryProductConfig")
    ResultInfo<QueryProductConfigRespDto> queryProductConfig(QueryProductConfigQry qry);

    /**
     * 查询PC选项列表
     *
     * @param qry 查询
     * @return 响应
     */
    @PostMapping("/productConfig/getPcOptionList")
    ResultInfo<List<GetPcOptionListRespDto>> getPcOptionList(GetPcOptionListQry qry);

    /**
     * 编辑Product Config
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/productConfig/editProductConfig")
    ResultInfo<EditProductConfigRespDto> editProductConfig(EditProductConfigCmd cmd);

    /**
     * 同步Product Config Feature/Option行
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/productConfig/syncProductConfigModelOption")
    ResultInfo<SyncProductConfigModelOptionRespDto> syncProductConfigModelOption(SyncProductConfigModelOptionCmd cmd);

}
