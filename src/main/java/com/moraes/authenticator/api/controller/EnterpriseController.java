package com.moraes.authenticator.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moraes.authenticator.api.mapper.Mapper;
import com.moraes.authenticator.api.model.dto.real_state.enterprise.EnterpriseDTO;
import com.moraes.authenticator.api.service.interfaces.real_state.IEnterpriseService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/enterprise")
@AllArgsConstructor
public class EnterpriseController {

    private final IEnterpriseService service;

    @GetMapping("/names")
    public ResponseEntity<List<String>> getNameByConstructionName(@RequestParam String constructionName) {
        return ResponseEntity.ok(service.getNameByConstructionName(constructionName));
    }

    @GetMapping
    public ResponseEntity<EnterpriseDTO> getByNameAndConstructionName(@RequestParam String name,
            @RequestParam String constructionName) {
        return ResponseEntity.ok(
                Mapper.parseObject(service.getByNameAndConstructionName(name, constructionName), EnterpriseDTO.class));
    }
}
