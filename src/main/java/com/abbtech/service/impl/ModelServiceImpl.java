package com.abbtech.service.impl;

import com.abbtech.dto.request.ReqModelDto;
import com.abbtech.dto.response.RespBrandDto;
import com.abbtech.dto.response.RespModelDto;
import com.abbtech.exception.CarErrorEnum;
import com.abbtech.exception.CarException;
import com.abbtech.model.Brand;
import com.abbtech.model.Model;
import com.abbtech.repository.BrandRepository;
import com.abbtech.repository.ModelRepository;
import com.abbtech.service.ModelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;
    private final BrandRepository brandRepository;

    @Override
    public RespModelDto addModel(ReqModelDto dto) {
        Brand brand = dto.brandId() != null
                ? brandRepository.findById(dto.brandId())
                .orElseThrow(() -> new CarException(CarErrorEnum.BRAND_NOT_FOUND))
                : brandRepository.save(
                Brand.builder()
                        .name(dto.brand().name())
                        .country(dto.brand().country())
                        .foundedYear(dto.brand().foundedYear())
                        .build()
        );

        Model model = Model.builder()
                .name(dto.name())
                .category(dto.category())
                .yearFrom(dto.yearFrom())
                .yearTo(dto.yearTo())
                .brand(brand)
                .build();

        return mapToDto(modelRepository.save(model));
    }

    @Override
    public RespModelDto updateModel(Integer id, ReqModelDto dto) {
        Model model = modelRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.CAR_NOT_FOUND));

        model.setName(dto.name());
        model.setCategory(dto.category());
        model.setYearFrom(dto.yearFrom());
        model.setYearTo(dto.yearTo());

        return mapToDto(model);
    }

    @Override
    public List<RespModelDto> getModels() {
        return mapToDtoList(modelRepository.findAll());
    }

    @Override
    public RespModelDto getModelById(Integer id) {
        return mapToDto(
                modelRepository.findById(id)
                        .orElseThrow(() -> new CarException(CarErrorEnum.CAR_NOT_FOUND))
        );
    }

    @Override
    public void deleteModelById(Integer id) {
        modelRepository.deleteById(id);
    }

    @Override
    public RespModelDto mapToDto(Model model) {
        return new RespModelDto(
                model.getId(),
                new RespBrandDto(
                        model.getBrand().getId(),
                        model.getBrand().getName(),
                        model.getBrand().getCountry(),
                        model.getBrand().getFoundedYear(),
                        List.of()
                ),
                List.of(),
                model.getName(),
                model.getCategory(),
                model.getYearFrom(),
                model.getYearTo()
        );
    }

    @Override
    public List<RespModelDto> mapToDtoList(List<Model> models) {
        return models.stream().map(this::mapToDto).toList();
    }
}
