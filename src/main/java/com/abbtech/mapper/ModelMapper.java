package com.abbtech.mapper;

import com.abbtech.dto.response.RespModelDto;
import com.abbtech.model.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ModelMapper {

    private final CarMapper carMapper;

    public RespModelDto toDto(Model model) {
        return new RespModelDto(
                model.getId(),
                null,       // no brand (break cycle)
                List.of(),
                model.getName(),
                model.getCategory(),
                model.getYearFrom(),
                model.getYearTo()
        );
    }

    public RespModelDto toDtoWithCars(Model model) {
        return new RespModelDto(
                model.getId(),
                null,
                carMapper.toDtoList(model.getCars()),
                model.getName(),
                model.getCategory(),
                model.getYearFrom(),
                model.getYearTo()
        );
    }

    public List<RespModelDto> toDtoList(List<Model> models) {
        return models == null
                ? List.of()
                : models.stream().map(this::toDto).toList();
    }

    public List<RespModelDto> toDtoListWithCars(List<Model> models) {
        return models == null
                ? List.of()
                : models.stream().map(this::toDtoWithCars).toList();
    }
}
