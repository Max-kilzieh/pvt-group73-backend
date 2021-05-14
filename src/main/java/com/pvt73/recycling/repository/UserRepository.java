package com.pvt73.recycling.repository;

import com.pvt73.recycling.model.dao.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
