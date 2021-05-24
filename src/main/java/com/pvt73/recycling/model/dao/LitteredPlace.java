package com.pvt73.recycling.model.dao;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class LitteredPlace {

    @Id
    @GeneratedValue
    private Integer id;
    @NotBlank
    private String userId;

    @Setter(AccessLevel.NONE)
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;


    private CleaningStatus cleaningStatus;

    private LocalDateTime cleanedAt;
    private String cleanedBy;

    @Transient
    private double distance;
    private String address;
    private LatLng coordinates;


    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Image> imageSet = new HashSet<>();

    private boolean event;
    private String description;


    public LitteredPlace(LatLng coordinates, String userId) {
        this.coordinates = coordinates;
        this.userId = userId;

    }

    public boolean containImage(String imageId) {
        for (Image image : imageSet) {
            if (image.getId().equals(imageId))
                return true;
        }
        return false;
    }

    public void setCleaningStatus(CleaningStatus status) {
        if (status == CleaningStatus.CLEAN)
            cleanedAt = LocalDateTime.now();

        this.cleaningStatus = status;
    }


}
