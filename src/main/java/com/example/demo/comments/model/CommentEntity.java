package com.example.demo.comments.model;

import java.util.Date;
import java.util.Objects;

import com.example.demo.apartments.model.ApartmentEntity;
import com.example.demo.core.model.BaseEntity;
import com.example.demo.users.model.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {
    @Column(nullable = false, unique = true, length = 300)
    private String description;
    @Column
    private Date date;
    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "apartmentId")
    private ApartmentEntity apartment;

    public CommentEntity() {
    }

    public CommentEntity(String description, Date date, UserEntity user, ApartmentEntity apartment) {
        this.description = description;
        this.date = date;
        this.user = user;
        this.apartment = apartment;
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
        return Objects.hash(id, description, date, user, apartment);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CommentEntity other = (CommentEntity) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (apartment == null) {
            if (other.apartment != null)
                return false;
        } else if (!apartment.equals(other.apartment))
            return false;
        return true;
    }
}
