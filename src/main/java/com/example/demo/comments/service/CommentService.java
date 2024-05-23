package com.example.demo.comments.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.core.error.NotFoundException;
import com.example.demo.users.model.UserEntity;
import com.example.demo.users.service.UserService;
import com.example.demo.apartments.model.ApartmentEntity;
import com.example.demo.apartments.service.ApartmentService;
import com.example.demo.comments.model.CommentEntity;
import com.example.demo.comments.repository.CommentRepository;

@Service
public class CommentService {
    private final CommentRepository repository;
    private final UserService userService;
    private final ApartmentService apartmentService;

    public CommentService(CommentRepository repos, UserService user, ApartmentService apartment) {
        repository = repos;
        userService = user;
        apartmentService = apartment;
    }

    public List<CommentEntity> getAll(Long apartmentId) {
        if (Objects.equals(apartmentId, 0L)) {
            return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
        }
        return StreamSupport.stream(repository.findAllByApartmentId(apartmentId).spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public Page<CommentEntity> getAllByUser(Long userId, int page, int size) {
        if (!Objects.equals(userId, 0L)) {
            return repository.findAllByUserId(userId, PageRequest.of(page, size, Sort.by("id")));
        }
        return repository.findAll(PageRequest.of(page, size, Sort.by("id")));
    }

    @Transactional(readOnly = true)
    public Page<CommentEntity> getAllByApartment(Long apartmentId, int page, int size) {
        if (!Objects.equals(apartmentId, 0L)) {
            return repository.findAllByApartmentId(apartmentId, PageRequest.of(page, size, Sort.by("id")));
        }
        return repository.findAll(PageRequest.of(page, size, Sort.by("id")));
    }

    public CommentEntity get(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(CommentEntity.class, id));
    }

    public CommentEntity create(CommentEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }

    public CommentEntity update(Long id, CommentEntity entity) {
        final CommentEntity existsEntity = get(id);
        final ApartmentEntity apartment = apartmentService.get(entity.getApartment().getId());
        final UserEntity user = userService.get(entity.getUser().getId());
        existsEntity.setText(entity.getText());
        existsEntity.setUser(user);
        existsEntity.setApartment(apartment);
        return repository.save(existsEntity);
    }

    public CommentEntity delete(Long id) {
        final CommentEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
