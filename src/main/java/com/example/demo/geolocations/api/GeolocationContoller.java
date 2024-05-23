package com.example.demo.geolocations.api;

import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.core.configuration.Constants;
import com.example.demo.geolocations.model.GeolocationEntity;
import com.example.demo.geolocations.service.GeolocationService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(GeolocationContoller.URL)
public class GeolocationContoller {
    public static final String URL = Constants.ADMIN_PREFIX + "/geolocation";
    public static final String GEOLOCATION_VIEW = "geolocation";
    public static final String GEOLOCATION_EDIT_VIEW = "geolocation-edit";
    public static final String GEOLOCATION_ATTRIBUTE = "geolocation";

    private final GeolocationService geolocationService;
    private final ModelMapper modelMapper;

    public GeolocationContoller(GeolocationService geolocationService, ModelMapper modelMapper) {
        this.geolocationService = geolocationService;
        this.modelMapper = modelMapper;
    }

    private GeolocationDto toDto(GeolocationEntity entity) {
        return modelMapper.map(entity, GeolocationDto.class);
    }

    private GeolocationEntity toEntity(GeolocationDto dto) {
        return modelMapper.map(dto, GeolocationEntity.class);
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(
                "items",
                geolocationService.getAll().stream()
                        .map(this::toDto)
                        .toList());
        return GEOLOCATION_VIEW;
    }

    @GetMapping("/edit/")
    public String create(Model model) {
        model.addAttribute(GEOLOCATION_ATTRIBUTE, new GeolocationDto());
        return GEOLOCATION_EDIT_VIEW;
    }

    @PostMapping("/edit/")
    public String create(@ModelAttribute(name = GEOLOCATION_ATTRIBUTE) @Valid GeolocationDto geolocation,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return GEOLOCATION_EDIT_VIEW;
        }

        geolocationService.create(toEntity(geolocation));
        return Constants.REDIRECT_VIEW + URL;
    }

    @GetMapping("/edit/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        model.addAttribute(GEOLOCATION_ATTRIBUTE, toDto(geolocationService.get(id)));
        return GEOLOCATION_EDIT_VIEW;
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            @ModelAttribute(name = GEOLOCATION_ATTRIBUTE) @Valid GeolocationDto type,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return GEOLOCATION_EDIT_VIEW;
        }
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        geolocationService.update(id, toEntity(type));
        return Constants.REDIRECT_VIEW + URL;
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable(name = "id") Long id) {
        geolocationService.delete(id);
        return Constants.REDIRECT_VIEW + URL;
    }
}
