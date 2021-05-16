package com.pvt73.recycling.model.service.user;

import com.pvt73.recycling.model.dao.User;

public interface UserService {
    User creat(User user);

    User findByID(String email);

    User update(User user, String email);

    void delete(String email);


}
