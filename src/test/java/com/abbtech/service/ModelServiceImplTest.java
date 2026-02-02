package com.abbtech.service;

import com.abbtech.dto.request.ReqBrandDto;
import com.abbtech.dto.request.ReqModelDto;
import com.abbtech.dto.response.RespModelDto;
import com.abbtech.exception.CarErrorEnum;
import com.abbtech.exception.CarException;
import com.abbtech.model.Brand;
import com.abbtech.model.Model;
import com.abbtech.repository.BrandRepository;
import com.abbtech.repository.ModelRepository;
import com.abbtech.service.impl.ModelServiceImpl;
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
class ModelServiceImplTest {

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private ModelServiceImpl modelService;

    private Model testModel;
    private Brand testBrand;
    private ReqModelDto reqModelDto;

    @BeforeEach
    void setUp() {
        testBrand = Brand.builder()
                .id(1)
                .name("Toyota")
                .country("Japan")
                .foundedYear(1937)
                .build();

        testModel = Model.builder()
                .id(1)
                .name("Camry")
                .category("Sedan")
                .yearFrom(2020)
                .yearTo(2024)
                .brand(testBrand)
                .build();

        reqModelDto = new ReqModelDto(
                1,
                null,
                "Camry",
                "Sedan",
                2020,
                2024,
                null
        );
    }

    @Test
    void addModel_WithExistingBrandId_ShouldCreateModelWithExistingBrand() {
        // Arrange
        when(brandRepository.findById(reqModelDto.brandId())).thenReturn(Optional.of(testBrand));
        when(modelRepository.save(any(Model.class))).thenReturn(testModel);

        // Act
        RespModelDto result = modelService.addModel(reqModelDto);

        // Assert
        assertNotNull(result);
        assertEquals(testModel.getId(), result.id());
        assertEquals(testModel.getName(), result.name());
        assertEquals(testModel.getCategory(), result.category());
        assertEquals(testModel.getYearFrom(), result.yearFrom());
        assertEquals(testModel.getYearTo(), result.yearTo());
        assertNotNull(result.brand());
        assertEquals(testBrand.getId(), result.brand().id());
        verify(brandRepository, times(1)).findById(reqModelDto.brandId());
        verify(modelRepository, times(1)).save(any(Model.class));
        verify(brandRepository, never()).save(any(Brand.class));
    }

    @Test
    void addModel_WithNewBrand_ShouldCreateBothBrandAndModel() {
        // Arrange
        ReqBrandDto newBrandDto = new ReqBrandDto(
                "Honda",
                "Japan",
                1948,
                null
        );
        ReqModelDto dtoWithNewBrand = new ReqModelDto(
                null,
                newBrandDto,
                "Accord",
                "Sedan",
                2020,
                2024,
                null
        );

        Brand newBrand = Brand.builder()
                .id(2)
                .name("Honda")
                .country("Japan")
                .foundedYear(1948)
                .build();

        Model newModel = Model.builder()
                .id(2)
                .name("Accord")
                .category("Sedan")
                .yearFrom(2020)
                .yearTo(2024)
                .brand(newBrand)
                .build();

        when(brandRepository.save(any(Brand.class))).thenReturn(newBrand);
        when(modelRepository.save(any(Model.class))).thenReturn(newModel);

        // Act
        RespModelDto result = modelService.addModel(dtoWithNewBrand);

        // Assert
        assertNotNull(result);
        assertEquals(newModel.getId(), result.id());
        assertEquals(newModel.getName(), result.name());
        assertNotNull(result.brand());
        assertEquals(newBrand.getName(), result.brand().name());
        verify(brandRepository, times(1)).save(any(Brand.class));
        verify(modelRepository, times(1)).save(any(Model.class));
        verify(brandRepository, never()).findById(anyInt());
    }

    @Test
    void addModel_WithInvalidBrandId_ShouldThrowException() {
        // Arrange
        Integer invalidBrandId = 999;
        ReqModelDto invalidDto = new ReqModelDto(
                invalidBrandId,
                null,
                "TestModel",
                "SUV",
                2020,
                2024,
                null
        );
        when(brandRepository.findById(invalidBrandId)).thenReturn(Optional.empty());

        // Act & Assert
        CarException exception = assertThrows(CarException.class,
                () -> modelService.addModel(invalidDto));
        assertEquals(CarErrorEnum.BRAND_NOT_FOUND.toString(), exception.getMessage());
        verify(brandRepository, times(1)).findById(invalidBrandId);
        verify(modelRepository, never()).save(any(Model.class));
    }

    @Test
    void updateModel_WithValidId_ShouldUpdateAndReturnModel() {
        // Arrange
        Integer modelId = 1;
        ReqModelDto updateDto = new ReqModelDto(
                1,
                null,
                "Corolla",
                "Compact",
                2021,
                2025,
                null
        );
        when(modelRepository.findById(modelId)).thenReturn(Optional.of(testModel));

        // Act
        RespModelDto result = modelService.updateModel(modelId, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(updateDto.name(), result.name());
        assertEquals(updateDto.category(), result.category());
        assertEquals(updateDto.yearFrom(), result.yearFrom());
        assertEquals(updateDto.yearTo(), result.yearTo());
        verify(modelRepository, times(1)).findById(modelId);
    }

    @Test
    void updateModel_WithInvalidId_ShouldThrowException() {
        // Arrange
        Integer invalidId = 999;
        when(modelRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        CarException exception = assertThrows(CarException.class,
                () -> modelService.updateModel(invalidId, reqModelDto));
        assertEquals(CarErrorEnum.CAR_NOT_FOUND.toString(), exception.getMessage());
        verify(modelRepository, times(1)).findById(invalidId);
    }

    @Test
    void getModels_ShouldReturnAllModels() {
        // Arrange
        Model model2 = Model.builder()
                .id(2)
                .name("Corolla")
                .category("Compact")
                .yearFrom(2021)
                .yearTo(2025)
                .brand(testBrand)
                .build();

        List<Model> models = Arrays.asList(testModel, model2);
        when(modelRepository.findAll()).thenReturn(models);

        // Act
        List<RespModelDto> result = modelService.getModels();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testModel.getName(), result.get(0).name());
        assertEquals(model2.getName(), result.get(1).name());
        verify(modelRepository, times(1)).findAll();
    }

    @Test
    void getModels_WhenNoModels_ShouldReturnEmptyList() {
        // Arrange
        when(modelRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<RespModelDto> result = modelService.getModels();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(modelRepository, times(1)).findAll();
    }

    @Test
    void getModelById_WithValidId_ShouldReturnModel() {
        // Arrange
        Integer modelId = 1;
        when(modelRepository.findById(modelId)).thenReturn(Optional.of(testModel));

        // Act
        RespModelDto result = modelService.getModelById(modelId);

        // Assert
        assertNotNull(result);
        assertEquals(testModel.getId(), result.id());
        assertEquals(testModel.getName(), result.name());
        assertEquals(testModel.getCategory(), result.category());
        assertEquals(testModel.getYearFrom(), result.yearFrom());
        assertEquals(testModel.getYearTo(), result.yearTo());
        assertNotNull(result.brand());
        assertEquals(testBrand.getId(), result.brand().id());
        verify(modelRepository, times(1)).findById(modelId);
    }

    @Test
    void getModelById_WithInvalidId_ShouldThrowException() {
        // Arrange
        Integer invalidId = 999;
        when(modelRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        CarException exception = assertThrows(CarException.class,
                () -> modelService.getModelById(invalidId));
        assertEquals(CarErrorEnum.CAR_NOT_FOUND.toString(), exception.getMessage());
        verify(modelRepository, times(1)).findById(invalidId);
    }

    @Test
    void deleteModelById_ShouldCallRepositoryDelete() {
        // Arrange
        Integer modelId = 1;
        doNothing().when(modelRepository).deleteById(modelId);

        // Act
        modelService.deleteModelById(modelId);

        // Assert
        verify(modelRepository, times(1)).deleteById(modelId);
    }

    @Test
    void mapToDto_ShouldMapModelToDtoCorrectly() {
        // Act
        RespModelDto result = modelService.mapToDto(testModel);

        // Assert
        assertNotNull(result);
        assertEquals(testModel.getId(), result.id());
        assertEquals(testModel.getName(), result.name());
        assertEquals(testModel.getCategory(), result.category());
        assertEquals(testModel.getYearFrom(), result.yearFrom());
        assertEquals(testModel.getYearTo(), result.yearTo());
        assertNotNull(result.brand());
        assertEquals(testBrand.getId(), result.brand().id());
        assertEquals(testBrand.getName(), result.brand().name());
        assertEquals(testBrand.getCountry(), result.brand().country());
        assertEquals(testBrand.getFoundedYear(), result.brand().foundedYear());
        assertTrue(result.brand().models().isEmpty());
        assertTrue(result.cars().isEmpty());
    }

    @Test
    void mapToDtoList_ShouldMapAllModels() {
        // Arrange
        Model model2 = Model.builder()
                .id(2)
                .name("Corolla")
                .category("Compact")
                .yearFrom(2021)
                .yearTo(2025)
                .brand(testBrand)
                .build();
        List<Model> models = Arrays.asList(testModel, model2);

        // Act
        List<RespModelDto> result = modelService.mapToDtoList(models);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testModel.getName(), result.get(0).name());
        assertEquals(model2.getName(), result.get(1).name());
    }

    @Test
    void addModel_WithNullBrandIdAndNullBrand_ShouldHandleGracefully() {
        // Arrange
        ReqModelDto invalidDto = new ReqModelDto(
                null,
                null,
                "TestModel",
                "SUV",
                2020,
                2024,
                null
        );

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> modelService.addModel(invalidDto));
    }

    @Test
    void updateModel_ShouldNotUpdateBrand() {
        // Arrange
        Integer modelId = 1;
        Brand originalBrand = testModel.getBrand();
        when(modelRepository.findById(modelId)).thenReturn(Optional.of(testModel));

        // Act
        modelService.updateModel(modelId, reqModelDto);

        // Assert
        assertEquals(originalBrand, testModel.getBrand());
        verify(modelRepository, times(1)).findById(modelId);
    }

    @Test
    void addModel_BrandDtoContainsAllRequiredFields_ShouldCreateBrandCorrectly() {
        // Arrange
        ReqBrandDto brandDto = new ReqBrandDto(
                "Nissan",
                "Japan",
                1933,
                null
        );
        ReqModelDto dtoWithBrand = new ReqModelDto(
                null,
                brandDto,
                "Altima",
                "Sedan",
                2020,
                2024,
                null
        );

        Brand savedBrand = Brand.builder()
                .id(3)
                .name("Nissan")
                .country("Japan")
                .foundedYear(1933)
                .build();

        when(brandRepository.save(any(Brand.class))).thenAnswer(invocation -> {
            Brand brand = invocation.getArgument(0);
            assertEquals(brandDto.name(), brand.getName());
            assertEquals(brandDto.country(), brand.getCountry());
            assertEquals(brandDto.foundedYear(), brand.getFoundedYear());
            return savedBrand;
        });

        when(modelRepository.save(any(Model.class))).thenReturn(testModel);

        // Act
        modelService.addModel(dtoWithBrand);

        // Assert
        verify(brandRepository, times(1)).save(any(Brand.class));
    }
}