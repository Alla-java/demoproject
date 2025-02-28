package com.gridnine.testing.filterTests;

import com.gridnine.testing.filter.GroundTimeFilter;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroundTimeFilterTests {
    @Test
    void testFilterFlightsWithValidGroundTime() {
        // Устанавливаем максимальное время нахождения на земле (например, 2 часа)
        Duration maxGroundTime = Duration.ofHours(2);
        GroundTimeFilter filter = new GroundTimeFilter(maxGroundTime);

        // Создаем сегменты с допустимым временем нахождения на земле
        LocalDateTime arrivalTime1 = LocalDateTime.of(2025, 2, 25, 10, 0);
        LocalDateTime departureTime2 = LocalDateTime.of(2025, 2, 25, 12, 0);

        Segment segment1 = Mockito.mock(Segment.class);
        Segment segment2 = Mockito.mock(Segment.class);

        Mockito.when(segment1.getArrivalDate()).thenReturn(arrivalTime1);
        Mockito.when(segment2.getDepartureDate()).thenReturn(departureTime2);

        Flight flight = Mockito.mock(Flight.class);
        Mockito.when(flight.getSegments()).thenReturn(Arrays.asList(segment1, segment2));

        List<Flight> flights = Arrays.asList(flight);

        // Применяем фильтр
        List<Flight> filteredFlights = filter.filter(flights);

        // Проверяем, что рейс прошел фильтрацию, так как время нахождения на земле не превышает максимального
        assertEquals(1, filteredFlights.size());
        assertTrue(filteredFlights.contains(flight));
    }

    @Test
    void testFilterFlightsWithExcessiveGroundTime() {
        // Устанавливаем максимальное время нахождения на земле (например, 1 час)
        Duration maxGroundTime = Duration.ofHours(1);
        GroundTimeFilter filter = new GroundTimeFilter(maxGroundTime);

        // Создаем сегменты с временем нахождения на земле, превышающим максимальное время
        LocalDateTime arrivalTime1 = LocalDateTime.of(2025, 2, 25, 10, 0);
        LocalDateTime departureTime2 = LocalDateTime.of(2025, 2, 25, 13, 0); // 3 часа, что больше 1 часа

        Segment segment1 = Mockito.mock(Segment.class);
        Segment segment2 = Mockito.mock(Segment.class);

        Mockito.when(segment1.getArrivalDate()).thenReturn(arrivalTime1);
        Mockito.when(segment2.getDepartureDate()).thenReturn(departureTime2);

        Flight flight = Mockito.mock(Flight.class);
        Mockito.when(flight.getSegments()).thenReturn(Arrays.asList(segment1, segment2));

        List<Flight> flights = Arrays.asList(flight);

        // Применяем фильтр
        List<Flight> filteredFlights = filter.filter(flights);

        // Проверяем, что рейс не прошел фильтрацию, так как время нахождения на земле превышает максимальное
        assertTrue(filteredFlights.isEmpty());
    }

    @Test
    void testFilterFlightsWithMultipleSegments() {
        // Устанавливаем максимальное время нахождения на земле (например, 1 час)
        Duration maxGroundTime = Duration.ofHours(1);
        GroundTimeFilter filter = new GroundTimeFilter(maxGroundTime);

        // Создаем несколько сегментов
        LocalDateTime arrivalTime1 = LocalDateTime.of(2025, 2, 25, 10, 0);
        LocalDateTime departureTime2 = LocalDateTime.of(2025, 2, 25, 11, 30); // 1.5 часа
        LocalDateTime arrivalTime2 = LocalDateTime.of(2025, 2, 25, 12, 0);
        LocalDateTime departureTime3 = LocalDateTime.of(2025, 2, 25, 13, 0); // 1 час

        Segment segment1 = Mockito.mock(Segment.class);
        Segment segment2 = Mockito.mock(Segment.class);
        Segment segment3 = Mockito.mock(Segment.class);

        Mockito.when(segment1.getArrivalDate()).thenReturn(arrivalTime1);
        Mockito.when(segment2.getDepartureDate()).thenReturn(departureTime2);
        Mockito.when(segment2.getArrivalDate()).thenReturn(arrivalTime2);
        Mockito.when(segment3.getDepartureDate()).thenReturn(departureTime3);

        Flight flight = Mockito.mock(Flight.class);
        Mockito.when(flight.getSegments()).thenReturn(Arrays.asList(segment1, segment2, segment3));

        List<Flight> flights = Arrays.asList(flight);

        // Применяем фильтр
        List<Flight> filteredFlights = filter.filter(flights);

        // Проверяем, что рейс был исключен, так как время между сегментами 1 и 2 превышает 1 час
        assertTrue(filteredFlights.isEmpty());
    }

    @Test
    void testFilterFlightsWithNoSegments() {
        // Устанавливаем максимальное время нахождения на земле
        Duration maxGroundTime = Duration.ofHours(1);
        GroundTimeFilter filter = new GroundTimeFilter(maxGroundTime);

        // Создаем рейс без сегментов
        Flight flight = Mockito.mock(Flight.class);
        Mockito.when(flight.getSegments()).thenReturn(Arrays.asList());

        List<Flight> flights = Arrays.asList(flight);

        // Применяем фильтр
        List<Flight> filteredFlights = filter.filter(flights);

        // Проверяем, что рейс с пустыми сегментами прошел фильтрацию
        assertEquals(1, filteredFlights.size());
        assertTrue(filteredFlights.contains(flight));
    }

    @Test
    void testFilterEmptyList() {
        // Устанавливаем максимальное время нахождения на земле
        Duration maxGroundTime = Duration.ofHours(1);
        GroundTimeFilter filter = new GroundTimeFilter(maxGroundTime);

        // Применяем фильтр на пустом списке
        List<Flight> flights = Arrays.asList();

        // Применяем фильтр
        List<Flight> filteredFlights = filter.filter(flights);

        // Проверяем, что результат тоже пустой
        assertTrue(filteredFlights.isEmpty());
    }
}




