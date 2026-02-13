package com.abbtech.service.impl;

import com.abbtech.dto.request.ReqCarDto;
import com.abbtech.dto.response.RespCarDto;
import com.abbtech.exception.CarErrorEnum;
import com.abbtech.exception.CarException;
import com.abbtech.mapper.CarMapper;
import com.abbtech.model.Car;
import com.abbtech.model.Model;
import com.abbtech.repository.CarRepository;
import com.abbtech.repository.ModelRepository;
import com.abbtech.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ModelRepository modelRepository;
    private final CarMapper carMapper;

    @Override
    public RespCarDto addCar(ReqCarDto dto) {
        Model model = modelRepository.findById(dto.modelId())
                .orElseThrow(() -> new CarException(CarErrorEnum.MODEL_NOT_FOUND));

        Car car = Car.builder()
                .model(model)
                .vin(dto.vin())
                .registrationNumber(dto.registrationNumber())
                .mileageKm(dto.mileageKm())
                .productionYear(dto.productionYear())
                .build();

        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    public List<RespCarDto> getCars() {
        return carMapper.toDtoList(carRepository.findAll());
    }

    @Override
    public RespCarDto getCarById(Integer id) {
        return carMapper.toDto(
                carRepository.findById(id)
                        .orElseThrow(() -> new CarException(CarErrorEnum.CAR_NOT_FOUND))
        );
    }

    @Override
    public RespCarDto updateCar(Integer id, ReqCarDto dto) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.CAR_NOT_FOUND));

        car.setVin(dto.vin());
        car.setRegistrationNumber(dto.registrationNumber());
        car.setMileageKm(dto.mileageKm());
        car.setProductionYear(dto.productionYear());

        return carMapper.toDto(car);
    }

    @Override
    public void deleteCarById(Integer id) {
        carRepository.deleteById(id);
    }
}
