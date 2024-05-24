package com.example.demo.types.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import com.example.demo.types.model.TypeEntity;

public interface TypeRepository extends CrudRepository<TypeEntity, Long>, PagingAndSortingRepository<TypeEntity, Long> {
    Optional<TypeEntity> findByNameIgnoreCase(String name);

    @Modifying
    @Transactional
    @Query("DELETE FROM ApartmentEntity d WHERE d.type.id = :typeId")
    void deleteAllByTypeId(@Param("typeId") Long typeId);
}
