package com.abbtech.service.impl;

import com.abbtech.dto.request.ReqCarDto;
import com.abbtech.dto.response.RespCarDto;
import com.abbtech.exception.CarErrorEnum;
import com.abbtech.exception.CarException;
import com.abbtech.model.Car;
import com.abbtech.model.Model;
import com.abbtech.repository.CarRepository;
import com.abbtech.repository.ModelRepository;
import com.abbtech.service.CarService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ModelRepository modelRepository;

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

        return mapToDto(carRepository.save(car));
    }

    @Override
    public RespCarDto updateCar(Integer id, ReqCarDto dto) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.CAR_NOT_FOUND));

        car.setVin(dto.vin());
        car.setRegistrationNumber(dto.registrationNumber());
        car.setMileageKm(dto.mileageKm());
        car.setProductionYear(dto.productionYear());

        return mapToDto(car);
    }

    @Override
    public List<RespCarDto> getCars() {
        return mapToDtoList(carRepository.findAll());
    }

    @Override
    public RespCarDto getCarById(Integer id) {
        return mapToDto(
                carRepository.findById(id)
                        .orElseThrow(() -> new CarException(CarErrorEnum.CAR_NOT_FOUND))
        );
    }

    @Override
    public void deleteCarById(Integer id) {
        carRepository.deleteById(id);
    }

    @Override
    public RespCarDto mapToDto(Car car) {
        return new RespCarDto(
                car.getId(),
                null,
                car.getVin(),
                car.getRegistrationNumber(),
                car.getMileageKm(),
                car.getProductionYear()
        );
    }

    @Override
    public List<RespCarDto> mapToDtoList(List<Car> cars) {
        return cars.stream().map(this::mapToDto).toList();
    }
}

