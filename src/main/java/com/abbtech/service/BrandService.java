package com.abbtech.service;

import com.abbtech.dto.request.ReqBrandDto;
import com.abbtech.dto.response.RespBrandDto;
import com.abbtech.model.Brand;

import java.util.List;

public interface BrandService {
    RespBrandDto addBrand(ReqBrandDto brandDto);
    RespBrandDto updateBrand(Integer id, ReqBrandDto brandDto);
    List<RespBrandDto> getBrands();
    RespBrandDto getBrandById(Integer id);
    void deleteBrandById(Integer id);
}
