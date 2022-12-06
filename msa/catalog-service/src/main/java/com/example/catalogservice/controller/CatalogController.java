package com.example.catalogservice.controller;

import com.example.catalogservice.domain.entity.Catalog;
import com.example.catalogservice.domain.vo.ResponseCatalog;
import com.example.catalogservice.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CatalogController {

    private final Environment env;

    private final CatalogService catalogService;

    @GetMapping("/health_check")
    public String status() {
        return String.format("catalog-service works on PORT %s", env.getProperty("local.server.port"));
    }

    @GetMapping("/catalogs")
    public ResponseEntity<?> getCatalogs() {
        List<Catalog> users = catalogService.getAllCatalogs();

        ModelMapper modelMapper = new ModelMapper();
        List<ResponseCatalog> responseCatalogs = users.stream()
                .map(user -> modelMapper.map(user, ResponseCatalog.class))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(responseCatalogs);
    }
}
