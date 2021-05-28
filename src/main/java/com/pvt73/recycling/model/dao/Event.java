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

    public Event(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;

        Event event = (Event) o;

        if (getId() != null ? !getId().equals(event.getId()) : event.getId() != null) return false;
        return getUserId() != null ? getUserId().equals(event.getUserId()) : event.getUserId() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getUserId() != null ? getUserId().hashCode() : 0);
        return result;
    }
}
