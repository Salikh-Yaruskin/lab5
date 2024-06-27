package com.example.demo.apartments.api;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.apartments.model.ApartmentEntity;
import com.example.demo.apartments.service.ApartmentService;
import com.example.demo.core.configuration.Constants;
import com.example.demo.types.model.TypeEntity;
import com.example.demo.types.service.TypeService;
import com.example.demo.geolocations.model.GeolocationEntity;
import com.example.demo.geolocations.service.GeolocationService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(ApartmentCommnetTopController.URL)
public class ApartmentCommnetTopController {
    public static final String URL = Constants.ADMIN_PREFIX + "/top-apartment";
    private static final String APARTAMENT_VIEW = "top-apartment";

    private final ApartmentService apartmentService;
    private final TypeService typeService;
    private final GeolocationService geolocationService;
    private final ModelMapper modelMapper;

    public ApartmentCommnetTopController(ApartmentService apartmentService, TypeService typeService,
            GeolocationService geolocationService, ModelMapper modelMapper) {
        this.apartmentService = apartmentService;
        this.typeService = typeService;
        this.geolocationService = geolocationService;
        this.modelMapper = modelMapper;
    }

    private ApartmentDto toDto(ApartmentEntity entity) {
        ApartmentDto dto = modelMapper.map(entity, ApartmentDto.class);
        dto.setTypeName(entity.getType().getName());
        dto.setGeolocationName(entity.getGeolocation().getName());
        return dto;
    }

    private ApartmentEntity toEntity(ApartmentDto dto) {
        final ApartmentEntity entity = modelMapper.map(dto, ApartmentEntity.class);
        entity.setType(typeService.get(dto.getTypeId()));
        entity.setGeolocation(geolocationService.get(dto.getGeolocationId()));
        return entity;
    }

    @GetMapping
    public String getAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "typeId", required = false, defaultValue = "0") Long typeId,
            @RequestParam(name = "geolocationId", required = false, defaultValue = "0") Long geolocationId,
            Model model) {
        Page<ApartmentEntity> apartmentsPage;
        if (typeId != 0 && geolocationId != 0) {
            apartmentsPage = apartmentService.getAllByTypeAndGeolocation(typeId, geolocationId, page,
                    Constants.DEFUALT_PAGE_SIZE);
        } else if (typeId != 0) {
            apartmentsPage = apartmentService.getAll(typeId, page, Constants.DEFUALT_PAGE_SIZE);
        } else if (geolocationId != 0) {
            apartmentsPage = apartmentService.getAllByGeolocation(geolocationId, page, Constants.DEFUALT_PAGE_SIZE);
        } else {
            apartmentsPage = apartmentService.getAll(0L, page, Constants.DEFUALT_PAGE_SIZE);
        }

        List<TypeEntity> types = typeService.getAll();
        List<GeolocationEntity> geolocations = geolocationService.getAll();
        model.addAttribute("types", types);
        model.addAttribute("geolocations", geolocations);
        String url = "top-sapartment";
        if (typeId != null) {
            url += "?typeId=" + typeId;
        }
        if (geolocationId != null) {
            url += "&geolocationId=" + geolocationId;
        }
        model.addAttribute("url", url);
        model.addAttribute("apartments", apartmentsPage.getContent().stream().map(this::toDto).toList());
        model.addAttribute("currentPage", apartmentsPage.getNumber());
        model.addAttribute("totalPages", apartmentsPage.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("typeId", typeId);
        model.addAttribute("geolocationId", geolocationId);
        List<ApartmentEntity> topApartments = apartmentService.getAllOrderByCommentCountDesc();
        model.addAttribute("topApartments", topApartments.stream().limit(3).map(this::toDto).toList());
        return APARTAMENT_VIEW;
    }
}
