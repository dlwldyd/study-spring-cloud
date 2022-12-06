package com.example.catalogservice.service;

import com.example.catalogservice.domain.entity.Catalog;

import java.util.List;

public interface CatalogService {

    List<Catalog> getAllCatalogs();
}
