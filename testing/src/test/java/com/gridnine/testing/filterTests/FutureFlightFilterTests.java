package com.gridnine.testing.filterTests;

import com.gridnine.testing.filter.FutureFlightFilter;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FutureFlightFilterTests {
    private final FutureFlightFilter filter = new FutureFlightFilter();

    @Test
    void testFilterFlightsInFuture() {
        // Создаем сегмент рейса с отправлением в будущем
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureTime = now.plusHours(2); // Время через 2 часа от текущего времени

        Segment segment = Mockito.mock(Segment.class);
        Flight flight = Mockito.mock(Flight.class);

        // Устанавливаем дату отправления в будущем
        Mockito.when(segment.getDepartureDate()).thenReturn(futureTime);
        Mockito.when(flight.getSegments()).thenReturn(Arrays.asList(segment));

        List<Flight> flights = Arrays.asList(flight);

        // Применяем фильтр
        List<Flight> filteredFlights = filter.filter(flights);

        // Проверяем, что рейс прошел фильтрацию, так как отправление в будущем
        assertEquals(1, filteredFlights.size());
        assertTrue(filteredFlights.contains(flight));
    }

    @Test
    void testFilterFlightsAlreadyDeparted() {
        // Создаем сегмент рейса с отправлением в прошлом
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pastTime = now.minusHours(2); // Время 2 часа назад

        Segment segment = Mockito.mock(Segment.class);
        Flight flight = Mockito.mock(Flight.class);

        // Устанавливаем дату отправления в прошлом
        Mockito.when(segment.getDepartureDate()).thenReturn(pastTime);
        Mockito.when(flight.getSegments()).thenReturn(Arrays.asList(segment));

        List<Flight> flights = Arrays.asList(flight);

        // Применяем фильтр
        List<Flight> filteredFlights = filter.filter(flights);

        // Проверяем, что рейс не прошел фильтрацию, так как отправление уже прошло
        assertTrue(filteredFlights.isEmpty());
    }

    @Test
    void testFilterMultipleFlights() {
        // Создаем несколько рейсов, с разными датами отправления
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureTime = now.plusHours(2); // Время через 2 часа
        LocalDateTime pastTime = now.minusHours(2);  // Время 2 часа назад

        // Рейс 1: отправление в будущем
        Segment segment1 = Mockito.mock(Segment.class);
        Flight flight1 = Mockito.mock(Flight.class);
        Mockito.when(segment1.getDepartureDate()).thenReturn(futureTime);
        Mockito.when(flight1.getSegments()).thenReturn(Arrays.asList(segment1));

        // Рейс 2: отправление в прошлом
        Segment segment2 = Mockito.mock(Segment.class);
        Flight flight2 = Mockito.mock(Flight.class);
        Mockito.when(segment2.getDepartureDate()).thenReturn(pastTime);
        Mockito.when(flight2.getSegments()).thenReturn(Arrays.asList(segment2));

        List<Flight> flights = Arrays.asList(flight1, flight2);

        // Применяем фильтр
        List<Flight> filteredFlights = filter.filter(flights);

        // Проверяем, что отфильтрованы только рейсы, чье отправление в будущем
        assertEquals(1, filteredFlights.size());
        assertTrue(filteredFlights.contains(flight1));
        assertFalse(filteredFlights.contains(flight2));
    }

    @Test
    void testFilterEmptyList() {
        // Применяем фильтр на пустой список
        List<Flight> flights = Arrays.asList();

        // Применяем фильтр
        List<Flight> filteredFlights = filter.filter(flights);

        // Проверяем, что результат тоже пустой
        assertTrue(filteredFlights.isEmpty());
    }
}




