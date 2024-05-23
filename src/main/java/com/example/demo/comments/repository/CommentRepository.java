package com.example.demo.comments.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo.comments.model.CommentEntity;

public interface CommentRepository
        extends CrudRepository<CommentEntity, Long>, PagingAndSortingRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByApartmentId(Long apartmentId);

    Page<CommentEntity> findAllByUserId(Long userId, Pageable pageable);

    Page<CommentEntity> findAllByApartmentId(Long apartmentId, Pageable pageable);
}
