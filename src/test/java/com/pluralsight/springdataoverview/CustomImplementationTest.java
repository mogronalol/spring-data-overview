package com.pluralsight.springdataoverview;

import com.pluralsight.springdataoverview.entity.Flight;
import com.pluralsight.springdataoverview.repository.FlightRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomImplementationTest {

    @Autowired
    private FlightRepository flightRepository;

    @Test
    public void shouldSaveCustomImplementation() {
        // Given
        flightRepository.save(createFlight("London"));
        final Flight toKeep = createFlight("Paris");
        flightRepository.save(toKeep);

        // When
        flightRepository.deleteByOrigin("London");

        // Then
        assertThat(flightRepository.findAll())
                .hasSize(1)
                .first()
                .isEqualToComparingFieldByField(toKeep);
    }

    private Flight createFlight(final String destination) {
        final Flight flight = new Flight();
        flight.setOrigin(destination);
        flight.setDestination("Tokyo");
        flight.setScheduledAt(LocalDateTime.now());
        return flight;
    }
}
