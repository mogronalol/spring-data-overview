package com.pluralsight.springdataoverview;

import com.pluralsight.springdataoverview.entity.Flight;
import com.pluralsight.springdataoverview.repository.FlightRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class DerivedQueryTests {

	@Autowired
	private FlightRepository flightRepository;

	@Before
	public void setUp(){
	    flightRepository.deleteAll();
	}

	@Test
	public void shouldFindFlightsToLondon() {
		// Given
		final Flight flight1 = createFlight("London", "New York");
		final Flight flight2 = createFlight("London", "Paris");
		final Flight flight3 = createFlight("Spain", "Paris");

		flightRepository.saveAll(Lists.newArrayList(flight1, flight2, flight3));

		// When
		final List<Flight> flights = flightRepository.findByOrigin("London");

		// Then
		assertThat(flights).hasSize(2);
		assertThat(flights.get(0)).isEqualToComparingFieldByField(flight1);
		assertThat(flights.get(1)).isEqualToComparingFieldByField(flight2);
	}

	@Test
	public void shouldFindFlightsFromLondonToParis() {
		// Given
		final Flight flight1 = createFlight("London", "New York");
		final Flight flight2 = createFlight("London", "Paris");
		final Flight flight3 = createFlight("Spain", "Paris");

		flightRepository.saveAll(Lists.newArrayList(flight1, flight2, flight3));

		// When
		final List<Flight> flights = flightRepository.findByOriginAndDestination("London", "Paris");

		// Then
		assertThat(flights).hasSize(1);
		assertThat(flights.get(0)).isEqualToComparingFieldByField(flight2);
	}

	@Test
	public void shouldFindFlightsFromLondonOrSpain() {
		// Given
		final Flight flight1 = createFlight("London", "New York");
		final Flight flight2 = createFlight("London", "Paris");
		final Flight flight3 = createFlight("Spain", "Paris");

		flightRepository.saveAll(Lists.newArrayList(flight1, flight2, flight3));

		// When
		final List<Flight> flights = flightRepository.findByOriginIn("London", "Spain");

		// Then
		assertThat(flights).hasSize(3);
		assertThat(flights.get(0)).isEqualToComparingFieldByField(flight1);
		assertThat(flights.get(1)).isEqualToComparingFieldByField(flight2);
		assertThat(flights.get(2)).isEqualToComparingFieldByField(flight3);
	}

	@Test
	public void shouldFindFlightsFromLondonIgnoringCase() {
		// Given
		final Flight flight1 = createFlight("London", "New York");
		final Flight flight2 = createFlight("LONDON", "Paris");
		final Flight flight3 = createFlight("Spain", "Paris");

		flightRepository.saveAll(Lists.newArrayList(flight1, flight2, flight3));

		// When
		final List<Flight> flights = flightRepository.findByOriginIgnoreCase("LONDON");

		// Then
		assertThat(flights).hasSize(2);
		assertThat(flights.get(0)).isEqualToComparingFieldByField(flight1);
		assertThat(flights.get(1)).isEqualToComparingFieldByField(flight2);
	}

	private Flight createFlight(String origin, String destination) {
		final Flight flight = new Flight();
		flight.setOrigin(origin);
		flight.setDestination(destination);
		flight.setScheduledAt(LocalDateTime.parse("2011-12-13T12:12:00"));
		return flight;
	}

}
