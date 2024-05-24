package com.example.demo.apartments.service;

import java.util.List;
import java.util.stream.StreamSupport;
import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Objects;

import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.apartments.model.ApartmentEntity;
import com.example.demo.apartments.repository.ApartmentRepository;
import com.example.demo.core.error.NotFoundException;
import com.example.demo.geolocations.repository.GeolocationRepository;
import com.example.demo.types.repository.TypeRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ApartmentService {
    private final ApartmentRepository repository;
    private final TypeRepository typeRepository;
    private final GeolocationRepository geolocationRepository;

    public ApartmentService(ApartmentRepository repository, TypeRepository typeRepository,
            GeolocationRepository geolocationRepository) {
        this.repository = repository;
        this.typeRepository = typeRepository;
        this.geolocationRepository = geolocationRepository;
    }

    @Transactional(readOnly = true)
    public List<ApartmentEntity> getByIds(Collection<Long> ids) {
        final List<ApartmentEntity> apartments = StreamSupport.stream(repository.findAllById(ids).spliterator(), false)
                .toList();
        if (apartments.size() < ids.size()) {
            throw new IllegalArgumentException("Invalid type");
        }
        return apartments;
    }

    public List<ApartmentEntity> getAllByGeolocation(Long geolocationId) {
        return StreamSupport.stream(repository.findAllByGeolocationId(geolocationId).spliterator(), false).toList();
    }

    public Page<ApartmentEntity> getAllByGeolocation(Long geolocationId, int page, int size) {
        return repository.findAllByGeolocationId(geolocationId, PageRequest.of(page, size, Sort.by("id")));
    }

    public Page<ApartmentEntity> getAllByTypeAndGeolocation(Long typeId, Long geolocationId, int page, int size) {
        return repository.findByTypeIdAndGeolocationId(typeId,
                geolocationId, PageRequest.of(page, size, Sort.by("id")));
    }

    @Transactional(readOnly = true)
    public List<ApartmentEntity> getAll(Long typeId, Long geolocationId) {
        if (Objects.equals(typeId, 0L) || Objects.equals(geolocationId, 0L)) {
            return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
        } else {
            return repository.findByTypeIdAndGeolocationId(typeId, geolocationId);
        }
    }

    @Transactional(readOnly = true)
    public Page<ApartmentEntity> getAll(long typeId, long geolocationId, int page, int size) {
        if (!Objects.equals(typeId, 0L) || !Objects.equals(geolocationId, 0L)) {
            return repository.findByTypeIdAndGeolocationId(typeId,
                    geolocationId, PageRequest.of(page, size, Sort.by("id")));
        }
        return repository.findAll(PageRequest.of(page, size, Sort.by("id")));
    }

    public Page<ApartmentEntity> getAll(Long typeId, int page, int size) {
        if (Objects.equals(typeId, 0L)) {
            return repository.findAll(PageRequest.of(page, size, Sort.by("id")));
        }
        return repository.findAllByTypeId(typeId, PageRequest.of(page, size, Sort.by("id")));
    }

    @Transactional(readOnly = true)
    public ApartmentEntity get(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(ApartmentEntity.class, id));
    }

    @Transactional
    public ApartmentEntity create(ApartmentEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }

    @Transactional
    public ApartmentEntity update(Long id, ApartmentEntity entity) {
        final ApartmentEntity existsEntity = get(id);

        existsEntity.setType(entity.getType());
        existsEntity.setPropertyStatus(entity.getPropertyStatus());
        existsEntity.setPopular(entity.getPopular());
        existsEntity.setPrice(entity.getPrice());
        existsEntity.setName(entity.getName());
        existsEntity.setDescription(entity.getDescription());
        existsEntity.setGeolocation(entity.getGeolocation());
        existsEntity.setShower(entity.getShower());
        existsEntity.setPark(entity.getPark());
        return repository.save(existsEntity);
    }

    @Transactional
    public ApartmentEntity delete(Long id) {
        final ApartmentEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }

    @Transactional(readOnly = true)
    public List<ApartmentEntity> getAllOrderByCommentCountDesc() {
        return repository.findAllOrderByCommentCountDesc();
    }
}
