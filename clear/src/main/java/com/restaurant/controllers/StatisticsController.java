package com.restaurant.controllers;

import com.restaurant.entities.AllTables;
import com.restaurant.entities.Menu;
import com.restaurant.repositories.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

    /**
     * Отображение страницы статистики с популярными блюдами, столами, временем бронирования и средним чеком.
     * Использует период "day" по умолчанию.
     *
     * @param model           модель для передачи данных в представление
     * @param authentication объект аутентификации для проверки роли пользователя
     * @return имя представления для отображения статистики
     */
    @GetMapping
    public String showStatistics(Model model, Authentication authentication) {
        model.addAttribute("timePeriod", "day");  // Default period is 'day'
        model.addAttribute("popularDish", getTopDishes("day"));
        model.addAttribute("popularTable", getTopTables("day"));
        model.addAttribute("popularTime", getTopReservationTimes("day"));
        model.addAttribute("averageCheck", getAverageCheck("day"));

        String role = authentication != null ? authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("GUEST") : "GUEST";  // Если не аутентифицирован, то роль GUEST

        // Передаем роль в модель
        model.addAttribute("role", role);

        boolean isAdmin = role.equals("ROLE_MANAGER") || role.equals("ROLE_ADMIN");
        model.addAttribute("isAdmin", isAdmin);

        return "statistics";
    }

    /**
     * Обновление статистики на основе выбранного периода.
     * Периоды: "day", "week", "month".
     *
     * @param period выбранный период для статистики
     * @param model  модель для передачи данных в представление
     * @return имя представления для обновленной статистики
     */
    @PostMapping("/update")
    public String updateStatistics(@RequestParam String period, Model model) {
        model.addAttribute("timePeriod", period);
        model.addAttribute("popularDish", getTopDishes(period));
        model.addAttribute("popularTable", getTopTables(period));
        model.addAttribute("popularTime", getTopReservationTimes(period));
        model.addAttribute("averageCheck", getAverageCheck(period));

        return "statistics";
    }

    /**
     * Получение списка популярных блюд для выбранного периода.
     *
     * @param period выбранный период для статистики
     * @return список популярных блюд
     */
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

    /**
     * Получение списка популярных столов для выбранного периода.
     *
     * @param period выбранный период для статистики
     * @return список популярных столов
     */
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

    /**
     * Получение списка популярных времен бронирования для выбранного периода.
     *
     * @param period выбранный период для статистики
     * @return список популярных времен бронирования
     */
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

    /**
     * Расчет среднего чека для выбранного периода.
     *
     * @param period выбранный период для статистики
     * @return средний чек
     */
    private Double getAverageCheck(String period) {
        LocalDate startDate = calculateStartDate(period);
        LocalDate endDate = LocalDate.now().minusDays(1);  // Include yesterday
        return ordersRepository.calculateAverageCheckByDateRange(startDate, endDate);
    }

    /**
     * Вычисление начала периода в зависимости от выбранного периода (день, неделя, месяц).
     *
     * @param period выбранный период для статистики
     * @return дата начала периода
     */
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
