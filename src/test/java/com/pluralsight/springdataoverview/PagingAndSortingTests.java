package com.pluralsight.springdataoverview;

import com.pluralsight.springdataoverview.entity.Flight;
import com.pluralsight.springdataoverview.repository.FlightRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.Order.asc;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PagingAndSortingTests {

    @Autowired
    private FlightRepository flightRepository;

    @Test
    public void shouldSortFlightsByDestination() {
        // Given
        final ZonedDateTime now = ZonedDateTime.now();
        final Flight spain = createFlight("Spain", now);
        final Flight london = createFlight("London", now);
        final Flight paris = createFlight("Paris", now);

        flightRepository.saveAll(newArrayList(spain, london, paris));

        // When
        final List<Flight> flights = newArrayList(flightRepository.findAll(Sort.by("destination")));

        // Then
        assertThat(flights).hasSize(3);
        assertThat(flights.get(0).getDestination()).isEqualTo("London");
        assertThat(flights.get(1).getDestination()).isEqualTo("Paris");
        assertThat(flights.get(2).getDestination()).isEqualTo("Spain");
    }

    @Test
    public void shouldSortFlightsByScheduledAndThenName() {
        // Given
        final ZonedDateTime now = ZonedDateTime.now();
        final Flight paris1 = createFlight("Paris", now);
        final Flight paris2 = createFlight("Paris", now.plusHours(2));
        final Flight paris3 = createFlight("Paris", now.minusHours(1));
        final Flight london1 = createFlight("London", now.plusHours(1));
        final Flight london2 = createFlight("London", now);

        flightRepository.saveAll(newArrayList(paris1, paris2, paris3, london1, london2));

        // When
        final List<Flight> flights = newArrayList(flightRepository.findAll(Sort.by(asc("destination"), asc("scheduledAt"))));

        // Then
        assertThat(flights).hasSize(5);

        assertThat(flights.get(0).getDestination()).isEqualTo("London");
		assertThat(flights.get(0).getScheduledAt()).isEqualTo(now);

		assertThat(flights.get(1).getDestination()).isEqualTo("London");
		assertThat(flights.get(1).getScheduledAt()).isEqualTo(now.plusHours(1));

		assertThat(flights.get(2).getDestination()).isEqualTo("Paris");
		assertThat(flights.get(2).getScheduledAt()).isEqualTo(now.minusHours(1));

		assertThat(flights.get(3).getDestination()).isEqualTo("Paris");
		assertThat(flights.get(3).getScheduledAt()).isEqualTo(now);

		assertThat(flights.get(4).getDestination()).isEqualTo("Paris");
		assertThat(flights.get(4).getScheduledAt()).isEqualTo(now.plusHours(2));
    }

    @Test
    public void shouldPageResults() {
        // Given
        for (int i = 0; i < 50; i++) {
            flightRepository.save(createFlight(String.valueOf(i), ZonedDateTime.now()));
        }

        // When
        final Page<Flight> flightPage = flightRepository.findAll(PageRequest.of(2, 5));

        // Then
        assertThat(flightPage.getTotalElements()).isEqualTo(50);
        assertThat(flightPage.getNumberOfElements()).isEqualTo(5);
        assertThat(flightPage.getTotalPages()).isEqualTo(10);
        assertThat(flightPage.getContent().stream().map(Flight::getDestination))
                .containsExactly("10", "11", "12", "13", "14");
    }

    @Test
    public void shouldPageAndSortResults() {
        // Given
        for (int i = 0; i < 10; i++) {
            flightRepository.save(createFlight(String.valueOf(i), ZonedDateTime.now()));
        }

        // When
        final Page<Flight> flightPage = flightRepository.findAll(PageRequest.of(0, 5, Sort.by(DESC, "destination")));

        // Then
        assertThat(flightPage.getTotalElements()).isEqualTo(10);
        assertThat(flightPage.getNumberOfElements()).isEqualTo(5);
        assertThat(flightPage.getTotalPages()).isEqualTo(2);
        assertThat(flightPage.getContent().stream().map(Flight::getDestination))
                .containsExactly("9", "8", "7", "6", "5");
    }

    @Test
    public void shouldPageAndSortADerivedQuery() {
        // Given
        for (int i = 0; i < 10; i++) {
            final Flight flight = createFlight(String.valueOf(i), ZonedDateTime.now());
            flight.setOrigin("Paris");
            flightRepository.save(flight);
        }

        for (int i = 0; i < 10; i++) {
            final Flight flight = createFlight(String.valueOf(i), ZonedDateTime.now());
            flight.setOrigin("London");
            flightRepository.save(flight);
        }

        // When
        final Page<Flight> flightPage = flightRepository.findByOrigin("London", PageRequest.of(0, 5, Sort.by(DESC, "destination")));

        // Then
        assertThat(flightPage.getTotalElements()).isEqualTo(10);
        assertThat(flightPage.getNumberOfElements()).isEqualTo(5);
        assertThat(flightPage.getTotalPages()).isEqualTo(2);
        assertThat(flightPage.getContent().stream().map(Flight::getDestination))
                .containsExactly("9", "8", "7", "6", "5");
    }


    private Flight createFlight(final String destination, final ZonedDateTime scheduledAt) {
        final Flight flight = new Flight();
        flight.setOrigin("London");
        flight.setDestination(destination);
        flight.setScheduledAt(scheduledAt);
        return flight;
    }

}
