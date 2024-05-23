package com.example.demo.comments.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CommentDto {
    private Long id;
    @NotNull
    @Min(1)
    private Long userId;
    @NotNull
    @Min(1)
    private Long apartmentId;
    @NotNull
    private String description;
    @NotNull
    private Date date;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}