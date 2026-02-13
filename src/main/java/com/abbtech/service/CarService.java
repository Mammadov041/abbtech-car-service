package com.abbtech.service;

import com.abbtech.dto.request.ReqCarDto;
import com.abbtech.dto.response.RespCarDto;
import com.abbtech.model.Car;

import java.util.List;

public interface CarService {
    RespCarDto addCar(ReqCarDto carDto);
    RespCarDto updateCar(Integer id,ReqCarDto carDto);
    List<RespCarDto> getCars();
    RespCarDto getCarById(Integer id);
    void deleteCarById(Integer id);
}
