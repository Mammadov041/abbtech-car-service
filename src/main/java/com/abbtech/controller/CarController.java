package com.abbtech.controller;

import com.abbtech.dto.request.ReqCarDto;
import com.abbtech.dto.response.RespCarDto;
import com.abbtech.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    public RespCarDto add(@Valid @RequestBody ReqCarDto dto) {
        return carService.addCar(dto);
    }

    @GetMapping
    public List<RespCarDto> getAll() {
        return carService.getCars();
    }

    @GetMapping("/{id}")
    public RespCarDto getById(@PathVariable Integer id) {
        return carService.getCarById(id);
    }

    @PutMapping("/{id}")
    public RespCarDto update(@PathVariable Integer id,
                             @Valid @RequestBody ReqCarDto dto) {
        return carService.updateCar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        carService.deleteCarById(id);
    }
}

