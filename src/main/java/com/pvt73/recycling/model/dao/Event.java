package com.pvt73.recycling.model.dao;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Event {

    @Id
    @GeneratedValue
    private Integer id;
    @NotBlank
    private String userId;

    @Setter(AccessLevel.NONE)
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Future
    private LocalDateTime eventDateTime;

    @Transient
    private double distance;
    private String address;
    private LatLng coordinates;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Image> imageSet = new HashSet<>();

    @Transient
    private int numberOfParticipant;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> participantSet = new HashSet<>();

    private String description;

}
