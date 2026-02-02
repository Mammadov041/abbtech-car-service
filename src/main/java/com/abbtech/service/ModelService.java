package com.abbtech.service;

import com.abbtech.dto.request.ReqModelDto;
import com.abbtech.dto.response.RespModelDto;
import com.abbtech.model.Model;

import java.util.List;

public interface ModelService {
    RespModelDto addModel(ReqModelDto modelDto);
    RespModelDto updateModel(Integer id,ReqModelDto modelDto);
    List<RespModelDto> getModels();
    RespModelDto getModelById(Integer id);
    void deleteModelById(Integer id);
    RespModelDto mapToDto(Model model);
    List<RespModelDto> mapToDtoList(List<Model> cars);
}
