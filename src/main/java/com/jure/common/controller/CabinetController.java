package com.jure.common.controller;


import com.jure.common.persistant.dto.CabinetCreationRequest;
import com.jure.common.persistant.dto.CabinetResponse;
import com.jure.common.service.CabinetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cabinets")
public class CabinetController {

    private final CabinetService cabinetService;

    public CabinetController(CabinetService cabinetService) {
        this.cabinetService = cabinetService;
    }

    @PostMapping("/create")
    public ResponseEntity<CabinetResponse> createCabinet(@Valid @RequestBody CabinetCreationRequest request) {
        CabinetResponse response = cabinetService.createCabinet(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CabinetResponse>> getAllCabinets() {
        List<CabinetResponse> cabinets = cabinetService.getAllCabinets();
        return ResponseEntity.ok(cabinets);
    }

    @GetMapping("/get-byid/{id}")
    public ResponseEntity<CabinetResponse> getCabinetById(@PathVariable Long id) {
        CabinetResponse cabinet = cabinetService.getCabinetById(id);
        return ResponseEntity.ok(cabinet);
    }
}
