package com.pluralsight.springdataoverview;

import com.pluralsight.springdataoverview.flight.FlightService;
import com.pluralsight.springdataoverview.repository.FlightRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionalTests {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private FlightService flightService;

    @Before
    public void setUp(){
        flightRepository.deleteAll();
    }

    @Test
    public void shouldNotRollbackTransaction() {
        try {
            flightService.saveFlightsWithoutTransaction();
        }
        catch (Exception e) {
            // do nothing
        }
        finally {
            assertThat(flightRepository.findAll()).isNotEmpty();
        }
    }

    @Test
    public void shouldRollbackTransaction() {
        try {
            flightService.saveFlightsWithTransaction();
        }
        catch (Exception e) {
            // do nothing
        }
        finally {
            assertThat(flightRepository.findAll()).isEmpty();
        }
    }

    @Test
    public void shouldIsolateDataInTransactions() throws InterruptedException {

        new Thread(() -> {
            try {
                flightService.saveFlightsWithDefaultIsolationAndWait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread.sleep(1000);

        assertThat(flightRepository.findAll()).isEmpty();
    }

    @Test
    public void shouldIsolateTransactions() throws InterruptedException {

        new Thread(() -> {
            try {
                flightService.saveFlightsWithReadComittedIsolationAndWait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread.sleep(1000);

        assertThat(flightRepository.findAll()).isNotEmpty();
    }
}
