package com.example.demo.comments.api;

import java.util.Map;

import org.modelmapper.ModelMapper;
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

import com.example.demo.apartments.service.ApartmentService;
import com.example.demo.comments.model.CommentEntity;
import com.example.demo.comments.service.CommentService;
import com.example.demo.core.api.PageAttributesMapper;
import com.example.demo.core.configuration.Constants;
import com.example.demo.users.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(CommentController.URL)
public class CommentController {
    public static final String URL = Constants.ADMIN_PREFIX + "/comment";
    private static final String COMMENT_VIEW = "comment";
    private static final String COMMENT_EDIT_VIEW = "comment-edit-admin";
    private static final String PAGE_ATTRIBUTE = "page";
    private static final String COMMENT_ATTRIBUTE = "comment";

    private final CommentService commentService;
    private final UserService userService;
    private final ApartmentService apartmentService;
    private final ModelMapper modelMapper;

    public CommentController(CommentService comment, UserService user, ApartmentService apartment, ModelMapper model) {
        commentService = comment;
        userService = user;
        apartmentService = apartment;
        modelMapper = model;
    }

    private CommentUserApartmentTitleDto toDto(CommentEntity entity) {
        CommentUserApartmentTitleDto dto = new CommentUserApartmentTitleDto();
        dto.setId(entity.getId());
        dto.setText(entity.getText());
        dto.setUserId(entity.getUser().getId());
        dto.setApartmentId(entity.getApartment().getId());
        dto.setUser(entity.getUser().getLogin());
        dto.setTitle(entity.getApartment().getName());
        return dto;
    }

    private CommentEntity toEntity(CommentDto dto) {
        final CommentEntity entity = modelMapper.map(dto, CommentEntity.class);
        entity.setUser(userService.get(dto.getUserId()));
        entity.setApartment(apartmentService.get(dto.getApartmentId()));
        return entity;
    }

    @GetMapping
    public String getAll(@RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            Model model) {
        final Map<String, Object> attrib = PageAttributesMapper.toAttributes(
                commentService.getAllByUser(0L, page, Constants.DEFUALT_PAGE_SIZE), this::toDto);
        model.addAllAttributes(attrib);
        model.addAttribute(PAGE_ATTRIBUTE, page);
        return COMMENT_VIEW;
    }

    @GetMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            Model model) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        model.addAttribute(COMMENT_ATTRIBUTE, toDto(commentService.get(id)));
        model.addAttribute(PAGE_ATTRIBUTE, page);
        return COMMENT_EDIT_VIEW;
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            @ModelAttribute(name = COMMENT_ATTRIBUTE) @Valid CommentDto comment,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(PAGE_ATTRIBUTE, page);
            return COMMENT_EDIT_VIEW;
        }
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        redirectAttributes.addAttribute(PAGE_ATTRIBUTE, page);
        commentService.update(id, toEntity(comment));
        return Constants.REDIRECT_VIEW + URL;
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute(PAGE_ATTRIBUTE, page);
        commentService.delete(id);
        return Constants.REDIRECT_VIEW + URL;
    }
}
