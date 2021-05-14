package com.pvt73.recycling.model.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @NotBlank
    private String id;
    private String info;

    public User(String id) {
        this.id = id;
    }
}
