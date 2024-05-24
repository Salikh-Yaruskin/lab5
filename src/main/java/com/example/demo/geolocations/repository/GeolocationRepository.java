package com.example.demo.geolocations.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import com.example.demo.geolocations.model.GeolocationEntity;

public interface GeolocationRepository extends CrudRepository<GeolocationEntity, Long>,
        PagingAndSortingRepository<GeolocationEntity, Long> {
    Optional<GeolocationEntity> findByNameIgnoreCase(String name);

    @Modifying
    @Transactional
    @Query("DELETE FROM ApartmentEntity d WHERE d.geolocation.id = :geolocationId")
    void deleteAllByGeolocationId(@Param("geolocationId") Long geolocationId);
}
