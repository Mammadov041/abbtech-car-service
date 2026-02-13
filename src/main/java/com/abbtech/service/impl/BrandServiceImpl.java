package com.abbtech.service.impl;

import com.abbtech.dto.request.ReqBrandDto;
import com.abbtech.dto.response.RespBrandDto;
import com.abbtech.exception.CarErrorEnum;
import com.abbtech.exception.CarException;
import com.abbtech.mapper.BrandMapper;
import com.abbtech.model.Brand;
import com.abbtech.model.Model;
import com.abbtech.repository.BrandRepository;
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
    private final BrandMapper brandMapper;

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

        return brandMapper.toDtoWithModels(brandRepository.save(brand));
    }

    @Override
    public List<RespBrandDto> getBrands() {
        return brandMapper.toDtoListWithModels(brandRepository.findAll());
    }

    @Override
    public RespBrandDto getBrandById(Integer id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.BRAND_NOT_FOUND));
        return brandMapper.toDtoWithModels(brand);
    }

    @Override
    public RespBrandDto updateBrand(Integer id, ReqBrandDto dto) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.BRAND_NOT_FOUND));

        brand.setName(dto.name());
        brand.setCountry(dto.country());
        brand.setFoundedYear(dto.foundedYear());

        return brandMapper.toDtoWithModels(brand);
    }

    @Override
    public void deleteBrandById(Integer id) {
        brandRepository.deleteById(id);
    }
}
