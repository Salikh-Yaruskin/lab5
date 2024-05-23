package com.example.demo.comments.service;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.example.demo.apartments.repository.ApartmentRepository;
import com.example.demo.comments.model.CommentEntity;
import com.example.demo.comments.repository.CommentRepository;
import com.example.demo.core.error.NotFoundException;
import com.example.demo.users.model.UserEntity;
import com.example.demo.users.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final ApartmentRepository apartmentRepository;

    public CommentService(CommentRepository repository, UserRepository userRepository,
            ApartmentRepository apartmentRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.apartmentRepository = apartmentRepository;
    }

    @Transactional(readOnly = true)
    public List<CommentEntity> getAll(Long userId, Long apartmentId) {
        if (userId <= 0 && apartmentId <= 0) {
            return (List<CommentEntity>) repository.findAll();
        }
        return repository.findByUserIdAndApartmentId(userId, apartmentId);
    }

    @Transactional(readOnly = true)
    public List<CommentEntity> getAlll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public CommentEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(CommentEntity.class, id));
    }

    @Transactional
    public CommentEntity create(CommentEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }

    public CommentEntity update(Long id, CommentEntity entity) {
        final CommentEntity existsEntity = get(id);
        existsEntity.setUser(entity.getUser());
        existsEntity.setApartment(entity.getApartment());
        existsEntity.setDescription(entity.getDescription());
        existsEntity.setDate(entity.getDate());
        return repository.save(existsEntity);
    }

    @Transactional
    public CommentEntity delete(Long id) {
        final CommentEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}