package com.gridnine.testing.filter;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class GroundTimeFilter implements FlightFilter {
    private final Duration maxGroundTime;

    public GroundTimeFilter(Duration maxGroundTime) {
        this.maxGroundTime = maxGroundTime;
    }

    @Override
    public List<Flight> filter(List<Flight> flights) {
        return flights.stream()
                .filter(flight -> {
                    List<Segment> segments = flight.getSegments();
                    for (int i = 0; i < segments.size() - 1; i++) {
                        Duration groundTime = Duration.between(segments.get(i).getArrivalDate(),
                                segments.get(i + 1).getDepartureDate());
                        if (groundTime.compareTo(maxGroundTime) > 0) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
}
