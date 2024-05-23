package com.example.demo.types.service;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.core.error.NotFoundException;
import com.example.demo.types.model.TypeEntity;
import com.example.demo.types.repository.TypeRepository;

@Service
public class TypeService {
    private final TypeRepository repository;

    public TypeService(TypeRepository repos) {
        repository = repos;
    }

    private void checkName(String name) {
        if (repository.findByNameIgnoreCase(name).isPresent()) {
            throw new IllegalArgumentException(
                    String.format("Type with name %s is already exists", name));
        }
    }

    @Transactional(readOnly = true)
    public List<TypeEntity> getAll() {
        return StreamSupport.stream(repository.findAll(Sort.by("id")).spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public TypeEntity get(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(TypeEntity.class, id));
    }

    @Transactional
    public TypeEntity create(TypeEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }

        checkName(entity.getName());
        return repository.save(entity);
    }

    @Transactional
    public TypeEntity update(Long id, TypeEntity entity) {
        final TypeEntity existsEntity = get(id);
        checkName(entity.getName());
        existsEntity.setName(entity.getName());
        return repository.save(existsEntity);
    }

    @Transactional
    public TypeEntity delete(Long id) {
        final TypeEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
