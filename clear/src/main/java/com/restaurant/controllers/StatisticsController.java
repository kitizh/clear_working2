package com.restaurant.controllers;

import com.restaurant.entities.AllTables;
import com.restaurant.entities.Menu;
import com.restaurant.repositories.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {

    private final OrdersRepository ordersRepository;
    private final MenuRepository menuRepository;
    private final ReservationRepository reservationRepository;
    private final OrderItemRepository orderItemRepository;

    public StatisticsController(OrdersRepository ordersRepository, MenuRepository menuRepository,
                                ReservationRepository reservationRepository, OrderItemRepository orderItemRepository) {
        this.ordersRepository = ordersRepository;
        this.menuRepository = menuRepository;
        this.reservationRepository = reservationRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @GetMapping
    public String showStatistics(Model model) {
        model.addAttribute("timePeriod", "day");  // Default period is 'day'
        model.addAttribute("popularDish", getTopDishes("day"));
        model.addAttribute("popularTable", getTopTables("day"));
        model.addAttribute("popularTime", getTopReservationTimes("day"));
        model.addAttribute("averageCheck", getAverageCheck("day"));

        return "statistics";
    }

    @PostMapping("/update")
    public String updateStatistics(@RequestParam String period, Model model) {
        model.addAttribute("timePeriod", period);
        model.addAttribute("popularDish", getTopDishes(period));
        model.addAttribute("popularTable", getTopTables(period));
        model.addAttribute("popularTime", getTopReservationTimes(period));
        model.addAttribute("averageCheck", getAverageCheck(period));

        return "statistics";
    }

    // Helper methods for popular dishes, tables, times, and average check
    private List<Menu> getTopDishes(String period) {
        LocalDate startDate = calculateStartDate(period);
        LocalDate endDate = LocalDate.now().minusDays(1);  // Include yesterday
        Pageable pageable = PageRequest.of(0, 3); // Get top 3 results

        List<Object[]> results = orderItemRepository.findTopDishesByDateRange(startDate, endDate, pageable);
        List<Menu> topDishes = new ArrayList<>();
        for (Object[] result : results) {
            topDishes.add((Menu) result[0]); // Extract dish from the result
        }

        return topDishes;
    }

    private List<AllTables> getTopTables(String period) {
        LocalDate startDate = calculateStartDate(period);
        LocalDate endDate = LocalDate.now().minusDays(1);  // Include yesterday
        Pageable pageable = PageRequest.of(0, 3); // Get top 3 results

        List<Object[]> results = reservationRepository.findTopTablesByDateRange(startDate, endDate, pageable);
        List<AllTables> topTables = new ArrayList<>();
        for (Object[] result : results) {
            topTables.add((AllTables) result[0]); // Extract table from the result
        }

        return topTables;
    }

    private List<String> getTopReservationTimes(String period) {
        LocalDate startDate = calculateStartDate(period);
        LocalDate endDate = LocalDate.now().minusDays(1);  // Include yesterday
        Pageable pageable = PageRequest.of(0, 3); // Get top 3 results

        List<Object[]> results = reservationRepository.findTopReservationTimesByDateRange(startDate, endDate, pageable);
        List<String> topTimes = new ArrayList<>();

        // Конвертируем LocalTime в строку
        for (Object[] result : results) {
            LocalTime reservationTime = (LocalTime) result[0];
            topTimes.add(reservationTime.toString());  // Преобразуем LocalTime в строку
        }

        return topTimes;
    }


    private Double getAverageCheck(String period) {
        LocalDate startDate = calculateStartDate(period);
        LocalDate endDate = LocalDate.now().minusDays(1);  // Include yesterday
        return ordersRepository.calculateAverageCheckByDateRange(startDate, endDate);
    }

    private LocalDate calculateStartDate(String period) {
        LocalDate startDate = LocalDate.now();
        switch (period) {
            case "week":
                startDate = startDate.minusWeeks(1);
                break;
            case "month":
                startDate = startDate.minusMonths(1);
                break;
            case "day":
            default:
                startDate = startDate.minusDays(1); // For day we consider yesterday
                break;
        }
        return startDate;
    }
}

