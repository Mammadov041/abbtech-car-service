package com.abbtech.controller;

import com.abbtech.dto.request.ReqModelDto;
import com.abbtech.dto.response.RespModelDto;
import com.abbtech.service.ModelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/models")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    @PostMapping
    public RespModelDto add(@Valid @RequestBody ReqModelDto dto) {
        return modelService.addModel(dto);
    }

    @GetMapping
    public List<RespModelDto> getAll() {
        return modelService.getModels();
    }

    @GetMapping("/{id}")
    public RespModelDto getById(@PathVariable Integer id) {
        return modelService.getModelById(id);
    }

    @PutMapping("/{id}")
    public RespModelDto update(@PathVariable Integer id,
                               @Valid @RequestBody ReqModelDto dto) {
        return modelService.updateModel(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        modelService.deleteModelById(id);
    }
}
