package com.example.service;

import com.example.model.Trip;
import com.example.model.User;

import java.util.List;

public interface TripService {

    void save(Trip trip);
    List<Trip> findByDriver(User driver);
}
