package com.pluralsight.springdataoverview.repository;

public interface DeleteFlightByOriginRepository {
    void deleteByOrigin(String origin);
}
