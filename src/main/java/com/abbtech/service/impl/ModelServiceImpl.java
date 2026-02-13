package com.abbtech.service.impl;

import com.abbtech.dto.request.ReqModelDto;
import com.abbtech.dto.response.RespModelDto;
import com.abbtech.exception.CarErrorEnum;
import com.abbtech.exception.CarException;
import com.abbtech.mapper.ModelMapper;
import com.abbtech.model.Brand;
import com.abbtech.model.Model;
import com.abbtech.repository.BrandRepository;
import com.abbtech.repository.ModelRepository;
import com.abbtech.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;
    private final BrandRepository brandRepository;
    private final ModelMapper modelMapper;

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

        return modelMapper.toDtoWithCars(modelRepository.save(model));
    }

    @Override
    public List<RespModelDto> getModels() {
        return modelMapper.toDtoListWithCars(modelRepository.findAll());
    }

    @Override
    public RespModelDto getModelById(Integer id) {
        return modelMapper.toDtoWithCars(
                modelRepository.findById(id)
                        .orElseThrow(() -> new CarException(CarErrorEnum.MODEL_NOT_FOUND))
        );
    }

    @Override
    public RespModelDto updateModel(Integer id, ReqModelDto dto) {
        Model model = modelRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.MODEL_NOT_FOUND));

        model.setName(dto.name());
        model.setCategory(dto.category());
        model.setYearFrom(dto.yearFrom());
        model.setYearTo(dto.yearTo());

        return modelMapper.toDtoWithCars(model);
    }

    @Override
    public void deleteModelById(Integer id) {
        modelRepository.deleteById(id);
    }
}
