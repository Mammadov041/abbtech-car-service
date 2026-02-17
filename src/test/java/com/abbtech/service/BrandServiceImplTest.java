package com.abbtech.service;

import com.abbtech.dto.request.ReqBrandDto;
import com.abbtech.dto.request.ReqModelDto;
import com.abbtech.dto.response.RespBrandDto;
import com.abbtech.exception.CarErrorEnum;
import com.abbtech.exception.CarException;
import com.abbtech.mapper.BrandMapper;
import com.abbtech.model.Brand;
import com.abbtech.model.Model;
import com.abbtech.repository.BrandRepository;
import com.abbtech.repository.ModelRepository;
import com.abbtech.service.impl.BrandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ModelRepository modelRepository;

    @InjectMocks
    private BrandServiceImpl brandService;

    @InjectMocks
    private BrandMapper brandMapper;

    private Brand testBrand;
    private ReqBrandDto reqBrandDto;
    private Model testModel;

    @BeforeEach
    void setUp() {
        testBrand = Brand.builder()
                .id(1)
                .name("Toyota")
                .country("Japan")
                .foundedYear(1937)
                .models(new ArrayList<>())
                .build();

        testModel = Model.builder()
                .id(1)
                .name("Camry")
                .category("Sedan")
                .yearFrom(2020)
                .yearTo(2024)
                .brand(testBrand)
                .build();

        reqBrandDto = new ReqBrandDto(
                "Toyota",
                "Japan",
                1937,
                null
        );
    }

    @Test
    void addBrand_WithoutModels_ShouldReturnRespBrandDto() {
        // Arrange
        when(brandRepository.save(any(Brand.class))).thenReturn(testBrand);

        // Act
        RespBrandDto result = brandService.addBrand(reqBrandDto);

        // Assert
        assertNotNull(result);
        assertEquals(testBrand.getId(), result.id());
        assertEquals(testBrand.getName(), result.name());
        assertEquals(testBrand.getCountry(), result.country());
        assertEquals(testBrand.getFoundedYear(), result.foundedYear());
        assertTrue(result.models().isEmpty());
        verify(brandRepository, times(1)).save(any(Brand.class));
    }

    @Test
    void addBrand_WithModels_ShouldCreateBrandWithModels() {
        // Arrange
        ReqModelDto reqModelDto = new ReqModelDto(
                1,
                null,
                "Camry",
                "Sedan",
                2020,
                2024,
                null
        );
        ReqBrandDto dtoWithModels = new ReqBrandDto(
                "Toyota",
                "Japan",
                1937,
                List.of(reqModelDto)
        );

        Brand brandWithModels = Brand.builder()
                .id(1)
                .name("Toyota")
                .country("Japan")
                .foundedYear(1937)
                .build();

        Model model = Model.builder()
                .name("Camry")
                .category("Sedan")
                .yearFrom(2020)
                .yearTo(2024)
                .brand(brandWithModels)
                .build();

        brandWithModels.setModels(List.of(model));

        when(brandRepository.save(any(Brand.class))).thenReturn(brandWithModels);

        // Act
        RespBrandDto result = brandService.addBrand(dtoWithModels);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.models().size());
        verify(brandRepository, times(1)).save(any(Brand.class));
    }

    @Test
    void updateBrand_WithValidId_ShouldUpdateAndReturnBrand() {
        // Arrange
        Integer brandId = 1;
        ReqBrandDto updateDto = new ReqBrandDto(
                "Honda",
                "Japan",
                1948,
                null
        );

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(testBrand));

        // Act
        RespBrandDto result = brandService.updateBrand(brandId, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(updateDto.name(), result.name());
        assertEquals(updateDto.country(), result.country());
        assertEquals(updateDto.foundedYear(), result.foundedYear());
        verify(brandRepository, times(1)).findById(brandId);
    }

    @Test
    void updateBrand_WithInvalidId_ShouldThrowException() {
        // Arrange
        Integer invalidId = 999;
        when(brandRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        CarException exception = assertThrows(CarException.class,
                () -> brandService.updateBrand(invalidId, reqBrandDto));
        assertEquals(CarErrorEnum.CAR_NOT_FOUND.toString(), exception.baseErrorService.getMessage());
        verify(brandRepository, times(1)).findById(invalidId);
    }

    @Test
    void getBrands_ShouldReturnAllBrands() {
        // Arrange
        Brand brand2 = Brand.builder()
                .id(2)
                .name("Honda")
                .country("Japan")
                .foundedYear(1948)
                .models(new ArrayList<>())
                .build();

        List<Brand> brands = Arrays.asList(testBrand, brand2);
        when(brandRepository.findAll()).thenReturn(brands);

        // Act
        List<RespBrandDto> result = brandService.getBrands();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Toyota", result.get(0).name());
        assertEquals("Honda", result.get(1).name());
        verify(brandRepository, times(1)).findAll();
    }

    @Test
    void getBrands_WhenNoBrands_ShouldReturnEmptyList() {
        // Arrange
        when(brandRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<RespBrandDto> result = brandService.getBrands();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(brandRepository, times(1)).findAll();
    }

    @Test
    void getBrandById_WithValidId_ShouldReturnBrand() {
        // Arrange
        Integer brandId = 1;
        when(brandRepository.findById(brandId)).thenReturn(Optional.of(testBrand));

        // Act
        RespBrandDto result = brandService.getBrandById(brandId);

        // Assert
        assertNotNull(result);
        assertEquals(testBrand.getId(), result.id());
        assertEquals(testBrand.getName(), result.name());
        verify(brandRepository, times(1)).findById(brandId);
    }

    @Test
    void getBrandById_WithInvalidId_ShouldThrowException() {
        // Arrange
        Integer invalidId = 999;
        when(brandRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        CarException exception = assertThrows(CarException.class,
                () -> brandService.getBrandById(invalidId));
        assertEquals(CarErrorEnum.CAR_NOT_FOUND.toString(), exception.getMessage());
        verify(brandRepository, times(1)).findById(invalidId);
    }

    @Test
    void deleteBrandById_ShouldCallRepositoryDelete() {
        // Arrange
        Integer brandId = 1;
        doNothing().when(brandRepository).deleteById(brandId);

        // Act
        brandService.deleteBrandById(brandId);

        // Assert
        verify(brandRepository, times(1)).deleteById(brandId);
    }

    @Test
    void mapToDto_WithNullModels_ShouldReturnDtoWithEmptyModelsList() {
        // Arrange
        testBrand.setModels(null);

        // Act
        RespBrandDto result = brandMapper.toDto(testBrand);

        // Assert
        assertNotNull(result);
        assertTrue(result.models().isEmpty());
    }

    @Test
    void mapToDto_WithModels_ShouldMapModelsCorrectly() {
        // Arrange
        testBrand.setModels(List.of(testModel));

        // Act
        RespBrandDto result = brandMapper.toDto(testBrand);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.models().size());
        assertEquals(testModel.getId(), result.models().getFirst().id());
    }

    @Test
    void mapToDtoList_ShouldMapAllBrands() {
        // Arrange
        Brand brand2 = Brand.builder()
                .id(2)
                .name("Honda")
                .country("Japan")
                .foundedYear(1948)
                .models(new ArrayList<>())
                .build();
        List<Brand> brands = Arrays.asList(testBrand, brand2);

        // Act
        List<RespBrandDto> result = brandMapper.toDtoListWithModels(brands);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testBrand.getName(), result.get(0).name());
        assertEquals(brand2.getName(), result.get(1).name());
    }
}