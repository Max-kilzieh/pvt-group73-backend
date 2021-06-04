package com.pvt73.recycling.model.service.user;

import com.pvt73.recycling.exception.ResourceAlreadyExistException;
import com.pvt73.recycling.exception.ResourceNotFoundException;
import com.pvt73.recycling.model.dao.User;
import com.pvt73.recycling.model.service.event.EventService;
import com.pvt73.recycling.model.service.littered_place.LitteredPlaceService;
import com.pvt73.recycling.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final LitteredPlaceService litteredPlaceService;
    private final EventService eventService;
    private final Level level;

    @Override
    public User creat(@NonNull User newUser) {

        if (repository.existsById(newUser.getId()))
            throw new ResourceAlreadyExistException("user id", newUser.getId(), "user already exist");

        return repository.save(newUser);
    }

    @Override
    public User findByID(@NonNull String id) {

        return repository.findById(id)
                .map(user -> {
                    int placesCleaned = litteredPlaceService.countCleanedBy(id);
                    int eventParticipated = eventService.countByEventParticipated(id);
                    int placesReported = litteredPlaceService.countReportedBy(id);

                    user.setStatistic(placesCleaned, eventParticipated, placesReported,
                            level.getLevel(placesCleaned, eventParticipated, placesReported),
                            level.getProgressPoints(placesCleaned, eventParticipated, placesReported));

                    return repository.save(user);
                })
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("id", id, "user not found.");
                });
    }

    @Override
    public User update(@NonNull User newUser, @NonNull String id) {
        return repository.findById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setInfo(newUser.getInfo());
                    user.setRecentActivities(newUser.getRecentActivities());

                    return repository.save(user);
                })
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("id", id, "user not found.");
                });
    }

    @Override
    public void delete(@NonNull String userId) {
        repository.deleteById(userId);
    }
}
