package com.pvt73.recycling.model.dao;

import com.pvt73.recycling.model.util.LevelUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @NotBlank
    @Email
    private String id;

    @Setter(AccessLevel.NONE)
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdOn;

    @Setter(AccessLevel.NONE)
    @UpdateTimestamp
    private LocalDateTime updatedOn;


    @Setter(AccessLevel.NONE)
    private int level;
    private int points;

    @Setter(AccessLevel.NONE)
    private int placesCleaned;
    @Setter(AccessLevel.NONE)
    private int eventParticipated;
    @Setter(AccessLevel.NONE)
    private int litteredPlacesReported;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> recentActivities;
    private String name;
    private String info;


    public User(String id, String name, String info) {
        this.id = id;
        this.name = name;
        this.info = info;
    }

    public void setLevel(int placesCleaned, int eventParticipated, int litteredPlacesReported) {
        this.litteredPlacesReported = litteredPlacesReported;
        this.placesCleaned = placesCleaned;
        this.eventParticipated = eventParticipated;

        this.level = LevelUtil.getLevel(placesCleaned, eventParticipated, litteredPlacesReported);
        this.points = LevelUtil.getProgressPoints(placesCleaned, eventParticipated, litteredPlacesReported);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
