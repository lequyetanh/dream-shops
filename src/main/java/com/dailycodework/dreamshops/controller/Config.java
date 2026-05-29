package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.constant.ExceptionConstant;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.config.ConfigResponse;
import com.dailycodework.dreamshops.service.config.IConfigService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
public class Config {

    private final IConfigService configService;

    public Config(IConfigService configService) {
        this.configService = configService;
    }

    @GetMapping("/config/get-by-company/{companyId}")
    public ResponseEntity<BaseResultDTO> getByCompanyId(
            @PathVariable(value = "companyId")
            @NotNull(message = ExceptionConstant.ID_NOT_NULL)
            Long companyId
    ) {
        BaseResultDTO result = configService.getByCompanyId(companyId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/config/find-by-id/{id}")
    public ResponseEntity<BaseResultDTO> findById(
            @PathVariable(value = "id")
            @NotNull(message = ExceptionConstant.ID_NOT_NULL)
            Long id
    ) {
        BaseResultDTO result = configService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/config/update")
    public ResponseEntity<BaseResultDTO> updateConfig(@RequestBody ConfigResponse configResponse) {
        BaseResultDTO result = configService.updateConfig(configResponse);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/config/delete/{id}")
    public ResponseEntity<BaseResultDTO> deleteById(
            @PathVariable(value = "id")
            @NotNull(message = ExceptionConstant.ID_NOT_NULL)
            Long id
    ) {
        BaseResultDTO result = configService.deleteById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
