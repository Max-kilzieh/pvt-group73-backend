package com.pvt73.recycling.model.service.user;

import com.pvt73.recycling.model.dao.User;

public interface UserService {
    User creat(User user);

    User findByID(String id);

    User update(User user, String id);

    void delete(String userId);


}
