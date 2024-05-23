package com.example.demo.comments.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.comments.model.CommentEntity;

public interface CommentRepository extends CrudRepository<CommentEntity, Long> {
    List<CommentEntity> findByUserIdAndApartmentId(long userId, long apartmentId);
}
