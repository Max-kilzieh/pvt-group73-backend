package com.pvt73.recycling.model.service;

import com.pvt73.recycling.model.dao.User;
import com.pvt73.recycling.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public User creat(User newUser) {

        return repository.save(newUser);
    }

    @Override
    public User findByID(String email) {

        return repository.findById(email)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public User update(User newUser, String email) {
        return repository.findById(email)
                .map(user -> {
                    user.setInfo(newUser.getInfo());
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(email);
                    return repository.save(newUser);
                });
    }

    @Override
    public void delete(String email) {

        repository.deleteById(email);
    }
}
