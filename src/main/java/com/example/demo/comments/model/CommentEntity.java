package com.example.demo.comments.model;

import java.util.Objects;

import com.example.demo.apartments.model.ApartmentEntity;
import com.example.demo.core.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.example.demo.users.model.UserEntity;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "apartmentId")
    private ApartmentEntity apartment;
    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;
    @Column(nullable = false, length = 255)
    private String text;

    public CommentEntity() {
    }

    public CommentEntity(ApartmentEntity apartment, UserEntity user, String text) {
        this.apartment = apartment;
        this.user = user;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ApartmentEntity getApartment() {
        return apartment;
    }

    public void setApartment(ApartmentEntity apartment) {
        this.apartment = apartment;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, apartment, text);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final CommentEntity other = (CommentEntity) obj;
        return Objects.equals(other.getId(), getId())
                && Objects.equals(other.getUser(), getUser())
                && Objects.equals(other.getText(), getText())
                && Objects.equals(other.getApartment(), getApartment());
    }

}
