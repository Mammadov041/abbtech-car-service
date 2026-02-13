package com.abbtech.mapper;

import com.abbtech.dto.response.RespCarDto;
import com.abbtech.model.Car;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CarMapper {

    public RespCarDto toDto(Car car) {
        return new RespCarDto(
                car.getId(),
                null, // no model to avoid cycle
                car.getVin(),
                car.getRegistrationNumber(),
                car.getMileageKm(),
                car.getProductionYear()
        );
    }

    public List<RespCarDto> toDtoList(List<Car> cars) {
        return cars == null
                ? List.of()
                : cars.stream().map(this::toDto).toList();
    }
}
