package com.abbtech.service;

import com.abbtech.dto.request.ReqCarDto;
import com.abbtech.dto.response.RespCarDto;
import com.abbtech.exception.CarErrorEnum;
import com.abbtech.exception.CarException;
import com.abbtech.model.Brand;
import com.abbtech.model.Car;
import com.abbtech.model.Model;
import com.abbtech.repository.CarRepository;
import com.abbtech.repository.ModelRepository;
import com.abbtech.service.impl.CarServiceImpl;
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
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private ModelRepository modelRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private Car testCar;
    private Model testModel;
    private Brand testBrand;
    private ReqCarDto reqCarDto;

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

        testCar = Car.builder()
                .id(1)
                .model(testModel)
                .vin("1HGBH41JXMN109186")
                .registrationNumber("ABC123")
                .mileageKm(50000)
                .productionYear(2022)
                .build();

        reqCarDto = new ReqCarDto(
                1,
                null,
                "1HGBH41JXMN109186",
                "ABC123",
                50000,
                2022
        );
    }

    @Test
    void addCar_WithValidModelId_ShouldReturnRespCarDto() {
        // Arrange
        when(modelRepository.findById(reqCarDto.modelId())).thenReturn(Optional.of(testModel));
        when(carRepository.save(any(Car.class))).thenReturn(testCar);

        // Act
        RespCarDto result = carService.addCar(reqCarDto);

        // Assert
        assertNotNull(result);
        assertEquals(testCar.getId(), result.id());
        assertEquals(testCar.getVin(), result.vin());
        assertEquals(testCar.getRegistrationNumber(), result.registrationNumber());
        assertEquals(testCar.getMileageKm(), result.mileageKm());
        assertEquals(testCar.getProductionYear(), result.productionYear());
        verify(modelRepository, times(1)).findById(reqCarDto.modelId());
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void addCar_WithInvalidModelId_ShouldThrowException() {
        // Arrange
        Integer invalidModelId = 999;
        ReqCarDto invalidDto = new ReqCarDto(
                invalidModelId,
                null,
                "VIN123",
                "REG123",
                10000,
                2020
        );
        when(modelRepository.findById(invalidModelId)).thenReturn(Optional.empty());

        // Act & Assert
        CarException exception = assertThrows(CarException.class,
                () -> carService.addCar(invalidDto));
        assertEquals(CarErrorEnum.MODEL_NOT_FOUND.toString(), exception.getMessage());
        verify(modelRepository, times(1)).findById(invalidModelId);
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void updateCar_WithValidId_ShouldUpdateAndReturnCar() {
        // Arrange
        Integer carId = 1;
        ReqCarDto updateDto = new ReqCarDto(
                1,
                null,
                "NEW_VIN_NUMBER",
                "XYZ789",
                75000,
                2023
        );
        when(carRepository.findById(carId)).thenReturn(Optional.of(testCar));

        // Act
        RespCarDto result = carService.updateCar(carId, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(updateDto.vin(), result.vin());
        assertEquals(updateDto.registrationNumber(), result.registrationNumber());
        assertEquals(updateDto.mileageKm(), result.mileageKm());
        assertEquals(updateDto.productionYear(), result.productionYear());
        verify(carRepository, times(1)).findById(carId);
    }

    @Test
    void updateCar_WithInvalidId_ShouldThrowException() {
        // Arrange
        Integer invalidId = 999;
        when(carRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        CarException exception = assertThrows(CarException.class,
                () -> carService.updateCar(invalidId, reqCarDto));
        assertEquals(CarErrorEnum.CAR_NOT_FOUND.toString(), exception.getMessage());
        verify(carRepository, times(1)).findById(invalidId);
    }

    @Test
    void getCars_ShouldReturnAllCars() {
        // Arrange
        Car car2 = Car.builder()
                .id(2)
                .model(testModel)
                .vin("2HGBH41JXMN109187")
                .registrationNumber("DEF456")
                .mileageKm(30000)
                .productionYear(2023)
                .build();

        List<Car> cars = Arrays.asList(testCar, car2);
        when(carRepository.findAll()).thenReturn(cars);

        // Act
        List<RespCarDto> result = carService.getCars();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testCar.getVin(), result.get(0).vin());
        assertEquals(car2.getVin(), result.get(1).vin());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void getCars_WhenNoCars_ShouldReturnEmptyList() {
        // Arrange
        when(carRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<RespCarDto> result = carService.getCars();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void getCarById_WithValidId_ShouldReturnCar() {
        // Arrange
        Integer carId = 1;
        when(carRepository.findById(carId)).thenReturn(Optional.of(testCar));

        // Act
        RespCarDto result = carService.getCarById(carId);

        // Assert
        assertNotNull(result);
        assertEquals(testCar.getId(), result.id());
        assertEquals(testCar.getVin(), result.vin());
        assertEquals(testCar.getRegistrationNumber(), result.registrationNumber());
        assertEquals(testCar.getMileageKm(), result.mileageKm());
        assertEquals(testCar.getProductionYear(), result.productionYear());
        verify(carRepository, times(1)).findById(carId);
    }

    @Test
    void getCarById_WithInvalidId_ShouldThrowException() {
        // Arrange
        Integer invalidId = 999;
        when(carRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        CarException exception = assertThrows(CarException.class,
                () -> carService.getCarById(invalidId));
        assertEquals(CarErrorEnum.CAR_NOT_FOUND.toString(), exception.getMessage());
        verify(carRepository, times(1)).findById(invalidId);
    }

    @Test
    void deleteCarById_ShouldCallRepositoryDelete() {
        // Arrange
        Integer carId = 1;
        doNothing().when(carRepository).deleteById(carId);

        // Act
        carService.deleteCarById(carId);

        // Assert
        verify(carRepository, times(1)).deleteById(carId);
    }

    @Test
    void mapToDto_ShouldMapCarToDtoCorrectly() {
        // Act
        RespCarDto result = carService.mapToDto(testCar);

        // Assert
        assertNotNull(result);
        assertEquals(testCar.getId(), result.id());
        assertNull(result.model()); // As per implementation
        assertEquals(testCar.getVin(), result.vin());
        assertEquals(testCar.getRegistrationNumber(), result.registrationNumber());
        assertEquals(testCar.getMileageKm(), result.mileageKm());
        assertEquals(testCar.getProductionYear(), result.productionYear());
    }

    @Test
    void mapToDtoList_ShouldMapAllCars() {
        // Arrange
        Car car2 = Car.builder()
                .id(2)
                .model(testModel)
                .vin("2HGBH41JXMN109187")
                .registrationNumber("DEF456")
                .mileageKm(30000)
                .productionYear(2023)
                .build();
        List<Car> cars = Arrays.asList(testCar, car2);

        // Act
        List<RespCarDto> result = carService.mapToDtoList(cars);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testCar.getVin(), result.get(0).vin());
        assertEquals(car2.getVin(), result.get(1).vin());
    }

    @Test
    void addCar_ShouldSetModelCorrectly() {
        // Arrange
        when(modelRepository.findById(reqCarDto.modelId())).thenReturn(Optional.of(testModel));
        when(carRepository.save(any(Car.class))).thenAnswer(invocation -> {
            Car savedCar = invocation.getArgument(0);
            assertEquals(testModel, savedCar.getModel());
            return savedCar;
        });

        // Act
        carService.addCar(reqCarDto);

        // Assert
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void updateCar_ShouldNotUpdateModelId() {
        // Arrange
        Integer carId = 1;
        Model originalModel = testCar.getModel();
        when(carRepository.findById(carId)).thenReturn(Optional.of(testCar));

        // Act
        carService.updateCar(carId, reqCarDto);

        // Assert
        assertEquals(originalModel, testCar.getModel());
        verify(carRepository, times(1)).findById(carId);
    }
}