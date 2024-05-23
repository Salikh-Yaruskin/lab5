package com.example.demo.comments.api;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.comments.model.CommentEntity;
import com.example.demo.comments.service.CommentService;
import com.example.demo.core.configuration.Constants;
import com.example.demo.apartments.service.ApartmentService;
import com.example.demo.users.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(CommentController.URL)
public class CommentController {
    public static final String URL = Constants.ADMIN_PREFIX + "/comments";
    private final CommentService commentService;
    private final ApartmentService apartmentService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public CommentController(
            CommentService commentService,
            ApartmentService apartmentService,
            UserService userService, ModelMapper modelMapper) {
        this.commentService = commentService;
        this.apartmentService = apartmentService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    private CommentDto toDto(CommentEntity entity) {
        return modelMapper.map(entity, CommentDto.class);
    }

    private CommentEntity toEntity(CommentDto dto) {
        final CommentEntity entity = modelMapper.map(dto, CommentEntity.class);
        entity.setApartment(apartmentService.get(dto.getApartmentId()));
        entity.setUser(userService.get(dto.getUserId()));
        return entity;
    }

    @GetMapping
    public List<CommentDto> getAll(@RequestParam(name = "apartmentId", defaultValue = "0") Long apartmentId,
            @RequestParam(name = "userId", defaultValue = "0") Long userId) {
        return commentService.getAll(apartmentId, userId).stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public CommentDto get(@PathVariable(name = "id") Long id) {
        return toDto(commentService.get(id));
    }

    @PostMapping
    public CommentDto create(@RequestBody @Valid CommentDto dto) {
        return toDto(commentService.create(toEntity(dto)));
    }

    @PutMapping("/{id}")
    public CommentDto update(@PathVariable(name = "id") Long id, @RequestBody CommentDto dto) {
        return toDto(commentService.update(id, toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public CommentDto delete(@PathVariable(name = "id") Long id) {
        return toDto(commentService.delete(id));
    }
}