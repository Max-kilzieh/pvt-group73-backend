package com.pvt73.recycling.model.service;

import com.pvt73.recycling.exception.ResourceAlreadyExistException;
import com.pvt73.recycling.exception.ResourceNotFoundException;
import com.pvt73.recycling.model.dao.User;
import com.pvt73.recycling.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public User creat(@NonNull User newUser) {

        if (repository.existsById(newUser.getId()))
            throw new ResourceAlreadyExistException("user id", newUser.getId(), " user already exist");

        return repository.save(newUser);
    }

    @Override
    public User findByID(@NonNull String id) {

        return repository.findById(id)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("id", id, "user not found.");
                });
    }

    @Override
    public User update(@NonNull User newUser, @NonNull String id) {
        return repository.findById(id)
                .map(user -> {
                    user.setInfo(newUser.getInfo());
                    return repository.save(user);
                })
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("id", id, "user not found.");
                });
    }

    @Override
    public void delete(@NonNull String email) {

        repository.deleteById(email);
    }
}
