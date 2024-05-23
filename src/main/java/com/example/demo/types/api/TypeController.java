package com.example.demo.types.api;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.core.configuration.Constants;
import com.example.demo.types.model.TypeEntity;
import com.example.demo.types.service.TypeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(TypeController.URL)
public class TypeController {
    public static final String URL = Constants.ADMIN_PREFIX + "/type";
    private static final String TYPE_VIEW = "type";
    private static final String TYPE_EDIT_VIEW = "type-edit";
    private static final String TYPE_ATTRIBUTE = "type";

    private final TypeService typeService;
    private final ModelMapper modelMapper;

    public TypeController(TypeService typeService, ModelMapper modelMapper) {
        this.typeService = typeService;
        this.modelMapper = modelMapper;
    }

    private TypeDto toDto(TypeEntity entity) {
        return modelMapper.map(entity, TypeDto.class);
    }

    private TypeEntity toEntity(TypeDto dto) {
        return modelMapper.map(dto, TypeEntity.class);
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(
                "items",
                typeService.getAll().stream()
                        .map(this::toDto)
                        .toList());
        return TYPE_VIEW;
    }

    @GetMapping("/edit/")
    public String create(Model model) {
        model.addAttribute(TYPE_ATTRIBUTE, new TypeDto());
        return TYPE_EDIT_VIEW;
    }

    @PostMapping("/edit/")
    public String create(
            @ModelAttribute(name = TYPE_ATTRIBUTE) @Valid TypeDto type,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return TYPE_EDIT_VIEW;
        }
        typeService.create(toEntity(type));
        return Constants.REDIRECT_VIEW + URL;
    }

    @GetMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            Model model) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        model.addAttribute(TYPE_ATTRIBUTE, toDto(typeService.get(id)));
        return TYPE_EDIT_VIEW;
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            @ModelAttribute(name = TYPE_ATTRIBUTE) @Valid TypeDto type,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return TYPE_EDIT_VIEW;
        }
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        typeService.update(id, toEntity(type));
        return Constants.REDIRECT_VIEW + URL;
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable(name = "id") Long id) {
        typeService.delete(id);
        return Constants.REDIRECT_VIEW + URL;
    }
}
