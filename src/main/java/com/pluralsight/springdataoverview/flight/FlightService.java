package com.pluralsight.springdataoverview.flight;

import com.pluralsight.springdataoverview.entity.Flight;
import com.pluralsight.springdataoverview.repository.FlightRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class FlightService {
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public void saveFlightsWithoutTransaction() {
        saveFlights();
        throw new RuntimeException();
    }

    @Transactional()
    public void saveFlightsWithTransaction() {
        saveFlights();
        throw new RuntimeException();
    }

    @Transactional
    public void saveFlightsWithDefaultIsolationAndWait() throws InterruptedException {
        saveFlights();
        this.wait();
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void saveFlightsWithReadComittedIsolationAndWait() throws InterruptedException {
        saveFlights();
        this.wait();
    }

    private void saveFlights() {
        final Flight flight1 = new Flight();
        flight1.setOrigin("London");
        flight1.setDestination("Paris");
        flight1.setScheduledAt(LocalDateTime.now());

        final Flight flight2 = new Flight();
        flight2.setOrigin("Tokyo");
        flight2.setDestination("Milan");
        flight2.setScheduledAt(LocalDateTime.now());

        final Flight flight3 = new Flight();
        flight3.setOrigin("New York");
        flight3.setDestination("San Francisco");
        flight3.setScheduledAt(LocalDateTime.now());

        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);
    }
}
