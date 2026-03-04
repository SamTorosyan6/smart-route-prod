package com.example.service.impl;

import com.example.model.Trip;
import com.example.model.User;
import com.example.repository.TripRepository;
import com.example.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;

    @Override
    public void save(Trip trip) {
        tripRepository.save(trip);
    }

    @Override
    public List<Trip> findByDriver(User driver) {
        return tripRepository.findByDriver(driver);
    }
}
