package com.example.demo.apartments.api;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.apartments.model.ApartmentEntity;
import com.example.demo.apartments.service.ApartmentService;
import com.example.demo.core.configuration.Constants;
import com.example.demo.geolocations.model.GeolocationEntity;
import com.example.demo.geolocations.service.GeolocationService;
import com.example.demo.types.model.TypeEntity;
import com.example.demo.types.service.TypeService;

@Controller
@RequestMapping(ApartmentListController.URL)
public class ApartmentListController {
    public static final String URL = "/apartments";
    private static final String APARTAMENT_VIEW = "apartments";
    private static final String PAGE_ATTRIBUTE = "page";
    private static final String APARTAMENT_ATTRIBUTE = "apartments";

    private final ApartmentService apartmentService;
    private final TypeService typeService;
    private final GeolocationService geolocationService;
    private final ModelMapper modelMapper;

    public ApartmentListController(ApartmentService apartmentService, TypeService typeService,
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
        String url = "apartments";
        if (typeId != null) {
            url += "?typeId=" + typeId;
        }
        model.addAttribute("url", url);
        model.addAttribute("apartments", apartmentsPage.getContent().stream().map(this::toDto).toList());
        model.addAttribute("currentPage", apartmentsPage.getNumber());
        model.addAttribute("totalPages", apartmentsPage.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("typeId", typeId);
        model.addAttribute("geolocationId", geolocationId);
        return APARTAMENT_VIEW;
    }
    // @GetMapping("/comment/{videoId}")
    // public String getAllComments(@PathVariable(name = "videoId") Long videoId,
    // @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
    // Model model) {
    // final Map<String, Object> attrib = PageAttributesMapper
    // .toAttributes(commentService.getAllByVideo(videoId, page,
    // Constants.DEFUALT_PAGE_SIZE), this::toDto);
    // model.addAllAttributes(attrib);
    // model.addAttribute(PAGE_ATTRIBUTE, page);
    // return COMMENT_VIEW;
    // }
}
