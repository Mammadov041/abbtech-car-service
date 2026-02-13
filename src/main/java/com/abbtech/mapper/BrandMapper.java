package com.abbtech.mapper;

import com.abbtech.dto.response.RespBrandDto;
import com.abbtech.model.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BrandMapper {

    private final ModelMapper modelMapper;

    public RespBrandDto toDto(Brand brand) {
        return new RespBrandDto(
                brand.getId(),
                brand.getName(),
                brand.getCountry(),
                brand.getFoundedYear(),
                List.of()
        );
    }

    public RespBrandDto toDtoWithModels(Brand brand) {
        return new RespBrandDto(
                brand.getId(),
                brand.getName(),
                brand.getCountry(),
                brand.getFoundedYear(),
                modelMapper.toDtoListWithCars(brand.getModels())
        );
    }

    public List<RespBrandDto> toDtoListWithModels(List<Brand> brands) {
        return brands.stream().map(this::toDtoWithModels).toList();
    }
}
