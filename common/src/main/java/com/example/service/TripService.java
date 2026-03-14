package com.example.service;

import com.example.model.entitiy.Trip;
import com.example.model.entitiy.User;

import java.util.List;

public interface TripService {

    void save(Trip trip);
    List<Trip> findByDriver(User driver);
}
