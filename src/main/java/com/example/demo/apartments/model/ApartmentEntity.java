package com.example.demo.apartments.model;

import java.util.List;
import java.util.Objects;

import com.example.demo.core.model.BaseEntity;
import com.example.demo.types.model.TypeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.example.demo.apartments.api.PropertyStatus;
import com.example.demo.comments.model.CommentEntity;
import com.example.demo.geolocations.model.GeolocationEntity;

@Entity
@Table(name = "apartments")
public class ApartmentEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "typeId")
    private TypeEntity type;
    @Column(nullable = false)
    private PropertyStatus propertyStatus;
    @Column(nullable = false)
    private Boolean popular;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "geolocationId")
    private GeolocationEntity geolocation;
    @Column(nullable = false)
    private Boolean shower;
    @Column(nullable = false)
    private Integer park;
    @OneToMany(mappedBy = "apartment")
    private List<CommentEntity> comments;

    public ApartmentEntity() {
    }

    public ApartmentEntity(TypeEntity type,
            PropertyStatus propertyStatus, Boolean popular, Double price, String name,
            String description, GeolocationEntity geolocation, Boolean shower, Integer park) {
        this.type = type;
        this.propertyStatus = propertyStatus;
        this.popular = popular;
        this.price = price;
        this.name = name;
        this.description = description;
        this.geolocation = geolocation;
        this.shower = shower;
        this.park = park;
    }

    public TypeEntity getType() {
        return type;
    }

    public void setType(TypeEntity type) {
        this.type = type;
    }

    public PropertyStatus getPropertyStatus() {
        return propertyStatus;
    }

    public void setPropertyStatus(PropertyStatus propertyStatus) {
        this.propertyStatus = propertyStatus;
    }

    public Boolean getPopular() {
        return popular;
    }

    public void setPopular(Boolean popular) {
        this.popular = popular;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GeolocationEntity getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(GeolocationEntity geolocation) {
        this.geolocation = geolocation;
    }

    public Boolean getShower() {
        return shower;
    }

    public void setShower(Boolean shower) {
        this.shower = shower;
    }

    public Integer getPark() {
        return park;
    }

    public void setPark(Integer park) {
        this.park = park;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, propertyStatus, popular, price, name, description, geolocation, shower, park,
                comments);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final ApartmentEntity other = (ApartmentEntity) obj;
        return Objects.equals(other.getId(), getId())
                && Objects.equals(other.getType(), type)
                && Objects.equals(other.getPropertyStatus(), propertyStatus)
                && Objects.equals(other.getPopular(), popular)
                && Objects.equals(other.getPrice(), price)
                && Objects.equals(other.getName(), name)
                && Objects.equals(other.getDescription(), description)
                && Objects.equals(other.getGeolocation(), geolocation)
                && Objects.equals(other.getShower(), shower)
                && Objects.equals(other.getPark(), park);
    }
}
