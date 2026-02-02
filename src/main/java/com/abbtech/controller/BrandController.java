package com.abbtech.controller;

import com.abbtech.dto.request.ReqBrandDto;
import com.abbtech.dto.response.RespBrandDto;
import com.abbtech.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public RespBrandDto add(@Valid @RequestBody ReqBrandDto dto) {
        return brandService.addBrand(dto);
    }

    @GetMapping
    public List<RespBrandDto> getAll() {
        return brandService.getBrands();
    }

    @GetMapping("/{id}")
    public RespBrandDto getById(@PathVariable Integer id) {
        return brandService.getBrandById(id);
    }

    @PutMapping("/{id}")
    public RespBrandDto update(@PathVariable Integer id,
                               @Valid @RequestBody ReqBrandDto dto) {
        return brandService.updateBrand(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        brandService.deleteBrandById(id);
    }
}
