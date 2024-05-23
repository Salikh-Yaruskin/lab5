package com.example.demo.geolocations.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.core.error.NotFoundException;
import com.example.demo.geolocations.model.GeolocationEntity;
import com.example.demo.geolocations.repository.GeolocationRepository;

@Service
public class GeolocationService {
    private final GeolocationRepository repository;

    public GeolocationService(GeolocationRepository repository) {
        this.repository = repository;
    }

    private void checkName(Long id, String name) {
        final Optional<GeolocationEntity> existsGeolocation = repository.findByNameIgnoreCase(name);
        if (existsGeolocation.isPresent() && !existsGeolocation.get().getId().equals(id)) {
            throw new IllegalArgumentException(
                    String.format("Type with name %s is already exists", name));
        }
    }

    @Transactional(readOnly = true)
    public List<GeolocationEntity> getAll() {
        return StreamSupport.stream(repository.findAll(Sort.by("id")).spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public List<GeolocationEntity> getByIds(Collection<Long> ids) {
        final List<GeolocationEntity> types = StreamSupport.stream(repository.findAllById(ids).spliterator(), false)
                .toList();
        if (types.size() < ids.size()) {
            throw new IllegalArgumentException("Invalid type");
        }
        return types;
    }

    @Transactional(readOnly = true)
    public GeolocationEntity get(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(GeolocationEntity.class, id));
    }

    @Transactional
    public GeolocationEntity create(GeolocationEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        checkName(null, entity.getName());
        return repository.save(entity);
    }

    @Transactional
    public GeolocationEntity update(Long id, GeolocationEntity entity) {
        final GeolocationEntity existsEntity = get(id);
        checkName(id, entity.getName());
        existsEntity.setName(entity.getName());
        return repository.save(existsEntity);
    }

    @Transactional
    public GeolocationEntity delete(Long id) {
        final GeolocationEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
