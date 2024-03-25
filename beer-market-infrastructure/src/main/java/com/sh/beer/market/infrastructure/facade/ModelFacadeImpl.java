package com.sh.beer.market.infrastructure.facade;

import com.sh.beer.market.domain.facade.FeatureFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author
 * @date 2023/8/10
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ModelFacadeImpl implements FeatureFacade {

    /*private final BomMiddlePlatformClient bomMiddlePlatformClient;

    @Override
    public String getBrandByModel(String model) {
        ModelDto modelDto = FeignInvokeUtils.invoke(bomMiddlePlatformClient::getModel, model, "bomMiddlePlatformClient.getModel");
        if (modelDto == null) {
            throw new BusinessException(ConfigErrorCode.BOM_MIDDLE_PLATFORM_MODEL_NOT_EXIST);
        }
        return modelDto.getBrand();
    }

    @Override
    public List<String> getModelYearByModel(String model) {
        ResultInfo<ModelDto> response;
        try {
            response = bomMiddlePlatformClient.getModel(model);
        } catch (Exception e) {
            log.error("bomMiddlePlatformClient getModel error", e);
            throw new BusinessException(CommonErrorCode.THIRD_PARTY_ERROR, e.getMessage());
        }
        if (response == null || !response.isSuccess()) {
            throw new BusinessException(ConfigErrorCode.BOM_MIDDLE_PLATFORM_GET_MODEL_FAIL);
        }
        if (response.getData() == null) {
            throw new BusinessException(ConfigErrorCode.BOM_MIDDLE_PLATFORM_MODEL_NOT_EXIST);
        }
        return response.getData().getModelYear();
    }

    @Override
    public Set<String> getModelListByBrand(String brand) {
        ResultInfo<List<ModelDto>> response;
        try {
            response = bomMiddlePlatformClient.getModelListByBrand(brand);
        } catch (Exception e) {
            log.error("bomMiddlePlatformClient getModel error", e);
            throw new BusinessException(CommonErrorCode.THIRD_PARTY_ERROR, e.getMessage());
        }
        if (response == null || !response.isSuccess()) {
            throw new BusinessException(ConfigErrorCode.BOM_MIDDLE_PLATFORM_GET_MODEL_FAIL);
        }
        if (response.getData() == null) {
            throw new BusinessException(ConfigErrorCode.BOM_MIDDLE_PLATFORM_MODEL_NOT_EXIST);
        }
        return response.getData().stream().map(data -> data.getModel()).collect(Collectors.toSet());
    }

    @Override
    public List<ModelRespDto> getAllModelList() {
        List<ModelDto> modelDtoList = FeignInvokeUtils.invoke(req -> bomMiddlePlatformClient.getAllModelList(), null, "bomMiddlePlatformClient.getAllModelList");
        return LambdaUtil.map(modelDtoList, i -> {
            ModelRespDto respDto = new ModelRespDto();
            BeanUtils.copyProperties(i, respDto);
            return respDto;
        });
    }

    @Override
    public ModelRespDto getModel(String model) {
        ModelDto modelDto = FeignInvokeUtils.invoke(bomMiddlePlatformClient::getModel, model, "bomMiddlePlatformClient.getModel");
        if (modelDto == null) {
            return null;
        }
        ModelRespDto respDto = new ModelRespDto();
        BeanUtils.copyProperties(modelDto, respDto);
        return respDto;
    }*/

}
