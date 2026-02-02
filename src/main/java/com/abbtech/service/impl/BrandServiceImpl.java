package com.abbtech.service.impl;

import com.abbtech.dto.request.ReqBrandDto;
import com.abbtech.dto.response.RespBrandDto;
import com.abbtech.dto.response.RespCarDto;
import com.abbtech.exception.CarErrorEnum;
import com.abbtech.exception.CarException;
import com.abbtech.model.Brand;
import com.abbtech.model.Model;
import com.abbtech.repository.BrandRepository;
import com.abbtech.repository.ModelRepository;
import com.abbtech.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;

    @Override
    public RespBrandDto addBrand(ReqBrandDto dto) {
        Brand brand = Brand.builder()
                .name(dto.name())
                .country(dto.country())
                .foundedYear(dto.foundedYear())
                .build();

        if (dto.models() != null) {
            List<Model> models = dto.models().stream()
                    .map(m -> Model.builder()
                            .name(m.name())
                            .category(m.category())
                            .yearFrom(m.yearFrom())
                            .yearTo(m.yearTo())
                            .brand(brand)
                            .build())
                    .toList();
            brand.setModels(models);
        }

        return mapToDto(brandRepository.save(brand));
    }

    @Override
    public RespBrandDto updateBrand(Integer id, ReqBrandDto dto) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.CAR_NOT_FOUND));

        brand.setName(dto.name());
        brand.setCountry(dto.country());
        brand.setFoundedYear(dto.foundedYear());

        return mapToDto(brand);
    }

    @Override
    public List<RespBrandDto> getBrands() {
        return mapToDtoList(brandRepository.findAll());
    }

    @Override
    public RespBrandDto getBrandById(Integer id) {
        return mapToDto(
                brandRepository.findById(id)
                        .orElseThrow(() -> new CarException(CarErrorEnum.CAR_NOT_FOUND))
        );
    }

    @Override
    public void deleteBrandById(Integer id) {
        brandRepository.deleteById(id);
    }

    @Override
    public RespBrandDto mapToDto(Brand brand) {
        return new RespBrandDto(
                brand.getId(),
                brand.getName(),
                brand.getCountry(),
                brand.getFoundedYear(),
                brand.getModels() == null ? List.of() :
                        brand.getModels().stream()
                                .map(m -> new RespCarDto(
                                        m.getId(),
                                        null,
                                        null,
                                        null,
                                        null,
                                        null
                                ))
                                .toList()
        );
    }

    @Override
    public List<RespBrandDto> mapToDtoList(List<Brand> brands) {
        return brands.stream().map(this::mapToDto).toList();
    }
}
