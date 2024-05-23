package com.example.demo.geolocations.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo.geolocations.model.GeolocationEntity;

public interface GeolocationRepository extends CrudRepository<GeolocationEntity, Long>,
        PagingAndSortingRepository<GeolocationEntity, Long> {
    Optional<GeolocationEntity> findByNameIgnoreCase(String name);
}
