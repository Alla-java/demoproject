package com.gridnine.testing;

import com.gridnine.testing.filter.ArrivalBeforeDepartureFilter;
import com.gridnine.testing.filter.FlightFilter;
import com.gridnine.testing.filter.FutureFlightFilter;
import com.gridnine.testing.filter.GroundTimeFilter;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.service.FlightBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.util.List;

@SpringBootApplication
public class TestingApplication {

	public static void main(String[] args) {
		// Получаем тестовый набор перелётов
		List<Flight> flights = FlightBuilder.createFlights();

		// 1. Исключаем перелёты с вылетом до текущего момента времени
		FlightFilter futureFlightFilter = new FutureFlightFilter();
		List<Flight> futureFlights = futureFlightFilter.filter(flights);
		System.out.println("Перелёты с вылетом в будущем:");
		futureFlights.forEach(System.out::println);

		// 2. Исключаем перелёты, где есть сегменты с прилётом раньше вылета
		FlightFilter arrivalBeforeDepartureFilter = new ArrivalBeforeDepartureFilter();
		List<Flight> validArrivalBeforeDepartureFlights = arrivalBeforeDepartureFilter.filter(flights);
		System.out.println("Перелёты с прилётом раньше вылета:");
		validArrivalBeforeDepartureFlights.forEach(System.out::println);

		// 3. Исключаем перелёты с временем на Земле более 2 часов
		FlightFilter groundTimeFilter = new GroundTimeFilter(Duration.ofHours(2));
		List<Flight> validGroundTimeFlights = groundTimeFilter.filter(flights);
		System.out.println("Перелёты с временем на земле более 2 часов:");
		validGroundTimeFlights.forEach(System.out::println);
	}
}



