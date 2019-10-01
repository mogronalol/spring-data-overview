package com.pluralsight.springdataoverview.repository;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public class DeleteFlightByOriginRepositoryImpl implements DeleteFlightByOriginRepository {

    private final EntityManager entityManager;

    @Autowired
    public DeleteFlightByOriginRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void deleteByOrigin(String origin) {
        entityManager.createNativeQuery("DELETE from flight WHERE origin = ?")
                .setParameter(1, origin)
                .executeUpdate();
    }
}
