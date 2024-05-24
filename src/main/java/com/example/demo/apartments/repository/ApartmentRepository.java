package com.example.demo.apartments.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import com.example.demo.apartments.model.ApartmentEntity;

public interface ApartmentRepository extends CrudRepository<ApartmentEntity, Long>,
        PagingAndSortingRepository<ApartmentEntity, Long> {
    Optional<ApartmentEntity> findByNameIgnoreCase(String name);

    List<ApartmentEntity> findAllByGeolocationId(Long geolocationId);

    Page<ApartmentEntity> findAllByGeolocationId(Long geolocationId, Pageable pageable);

    Page<ApartmentEntity> findAllByTypeId(Long typeId, Pageable pageable);

    List<ApartmentEntity> findAllByTypeId(Long typeId);

    Page<ApartmentEntity> findByTypeIdAndGeolocationId(long typeId, long geolocationId, Pageable pageable);

    List<ApartmentEntity> findByTypeIdAndGeolocationId(long typeId, long geolocationId);

    @Modifying
    @Transactional
    @Query("SELECT f.apartment FROM CommentEntity f GROUP BY f.apartment.id ORDER BY COUNT(f.apartment.id) DESC")
    List<ApartmentEntity> findAllOrderByCommentCountDesc();
}