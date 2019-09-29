package com.pluralsight.springdataoverview.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.ZonedDateTime;

@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String origin;
    private String destination;
    private ZonedDateTime originallyScheduledAt;
    private ZonedDateTime nowScheduledAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public ZonedDateTime getOriginallyScheduledAt() {
        return originallyScheduledAt;
    }

    public void setOriginallyScheduledAt(ZonedDateTime originallyScheduledAt) {
        this.originallyScheduledAt = originallyScheduledAt;
    }

    public ZonedDateTime getNowScheduledAt() {
        return nowScheduledAt;
    }

    public void setNowScheduledAt(ZonedDateTime nowScheduledAt) {
        this.nowScheduledAt = nowScheduledAt;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", originallyScheduledAt=" + originallyScheduledAt +
                ", nowScheduledAt=" + nowScheduledAt +
                '}';
    }
}
