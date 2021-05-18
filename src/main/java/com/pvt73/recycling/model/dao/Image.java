package com.pvt73.recycling.model.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Image {

    @Id
    private String imageId;

    @CreationTimestamp
    private LocalDateTime createdOn;

    private LatLng coordinates;
    @NotBlank
    private String userId;
    private String url;
    private boolean clean;
    private String description;


    public Image(String userId, boolean clean, LatLng coordinates, String description, String imageId, String url) {
        this.userId = userId;
        this.clean = clean;
        this.coordinates = coordinates;
        this.description = description;
        this.imageId = imageId;
        this.url = url;
    }

}
