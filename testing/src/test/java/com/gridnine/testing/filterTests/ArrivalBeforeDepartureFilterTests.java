package com.gridnine.testing.filterTests;

import com.gridnine.testing.filter.ArrivalBeforeDepartureFilter;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArrivalBeforeDepartureFilterTests {
    private final ArrivalBeforeDepartureFilter filter = new ArrivalBeforeDepartureFilter();

    @Test
    void testFilterAllValidFlights() {
        // Создаем несколько сегментов рейсов, где прибытие всегда после отправления
        Segment segment1 = Mockito.mock(Segment.class);
        Segment segment2 = Mockito.mock(Segment.class);
        Flight flight1 = Mockito.mock(Flight.class);

        Mockito.when(segment1.getDepartureDate()).thenReturn(LocalDateTime.of(2025, 2, 25, 10, 0));
        Mockito.when(segment1.getArrivalDate()).thenReturn(LocalDateTime.of(2025, 2, 25, 12, 0));

        Mockito.when(segment2.getDepartureDate()).thenReturn(LocalDateTime.of(2025, 2, 25, 13, 0));
        Mockito.when(segment2.getArrivalDate()).thenReturn(LocalDateTime.of(2025, 2, 25, 14, 0));

        Mockito.when(flight1.getSegments()).thenReturn(Arrays.asList(segment1, segment2));

        List<Flight> flights = Arrays.asList(flight1);

        // Применяем фильтр
        List<Flight> filteredFlights = filter.filter(flights);

        // Проверяем, что фильтр возвращает рейс, так как все сегменты корректны
        assertEquals(1, filteredFlights.size());
        assertTrue(filteredFlights.contains(flight1));
    }

    @Test
    void testFilterFlightWithInvalidSegment() {
        // Создаем сегмент рейса с неправильным временем (прибытие до отправления)
        Segment segment1 = Mockito.mock(Segment.class);
        Flight flight2 = Mockito.mock(Flight.class);

        Mockito.when(segment1.getDepartureDate()).thenReturn(LocalDateTime.of(2025, 2, 25, 10, 0));
        Mockito.when(segment1.getArrivalDate()).thenReturn(LocalDateTime.of(2025, 2, 25, 8, 0)); // Ошибка, время прибытия раньше отправления

        Mockito.when(flight2.getSegments()).thenReturn(Arrays.asList(segment1));

        List<Flight> flights = Arrays.asList(flight2);

        // Применяем фильтр
        List<Flight> filteredFlights = filter.filter(flights);

        // Проверяем, что фильтр исключил этот рейс из списка
        assertTrue(filteredFlights.isEmpty());
    }

    @Test
    void testFilterMultipleFlights() {
        // Создаем несколько рейсов
        Segment segment1 = Mockito.mock(Segment.class);
        Flight flight1 = Mockito.mock(Flight.class);

        Mockito.when(segment1.getDepartureDate()).thenReturn(LocalDateTime.of(2025, 2, 25, 10, 0));
        Mockito.when(segment1.getArrivalDate()).thenReturn(LocalDateTime.of(2025, 2, 25, 12, 0));
        Mockito.when(flight1.getSegments()).thenReturn(Arrays.asList(segment1));

        Segment segment2 = Mockito.mock(Segment.class);
        Flight flight2 = Mockito.mock(Flight.class);

        Mockito.when(segment2.getDepartureDate()).thenReturn(LocalDateTime.of(2025, 2, 25, 13, 0));
        Mockito.when(segment2.getArrivalDate()).thenReturn(LocalDateTime.of(2025, 2, 25, 14, 0));
        Mockito.when(flight2.getSegments()).thenReturn(Arrays.asList(segment2));

        Segment segment3 = Mockito.mock(Segment.class);
        Flight flight3 = Mockito.mock(Flight.class);

        Mockito.when(segment3.getDepartureDate()).thenReturn(LocalDateTime.of(2025, 2, 25, 9, 0));
        Mockito.when(segment3.getArrivalDate()).thenReturn(LocalDateTime.of(2025, 2, 25, 8, 0)); // Некорректный сегмент

        Mockito.when(flight3.getSegments()).thenReturn(Arrays.asList(segment3));

        List<Flight> flights = Arrays.asList(flight1, flight2, flight3);

        // Применяем фильтр
        List<Flight> filteredFlights = filter.filter(flights);

        // Проверяем, что фильтр исключил рейс с некорректным сегментом
        assertEquals(2, filteredFlights.size());
        assertTrue(filteredFlights.contains(flight1));
        assertTrue(filteredFlights.contains(flight2));
        assertFalse(filteredFlights.contains(flight3));
    }
}

