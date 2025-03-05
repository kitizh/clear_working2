package com.restaurant.controllers;

import com.restaurant.entities.Menu;
import com.restaurant.entities.OrderItem;
import com.restaurant.entities.Orders;
import com.restaurant.repositories.MenuRepository;
import com.restaurant.repositories.OrderItemRepository;
import com.restaurant.repositories.OrdersRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Controller
public class StatisticsController {

    private final OrdersRepository ordersRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuRepository menuRepository;

    public StatisticsController(OrdersRepository ordersRepository,
                                OrderItemRepository orderItemRepository,
                                MenuRepository menuRepository) {
        this.ordersRepository = ordersRepository;
        this.orderItemRepository = orderItemRepository;
        this.menuRepository = menuRepository;
    }

    /**
     * Отображение страницы /stats.
     * Позволяет выбрать период start - end, по умолчанию - текущая неделя.
     */
    @GetMapping("/stats")
    public String showStatsPage(
            Model model,
            @RequestParam(value = "start", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,

            @RequestParam(value = "end", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {

        // Если даты не указаны - по умолчанию берем "сегодня минус 7 дней" и "сегодня"
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(7);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        // Приведем к LocalDateTime (с началом и концом суток)
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        // 1) Считаем "популярность блюд по категориям" за период
        // Логика: находим все OrderItem, их order.orderTime попадает в [startDateTime, endDateTime].
        // Для каждого OrderItem -> menu -> menuType. Считаем кол-во таких позиций.
        List<OrderItem> itemsInRange = orderItemRepository.findAllByOrder_OrderTimeBetween(startDateTime, endDateTime);

        Map<String, Integer> categoryCount = new HashMap<>(); // category -> sum
        for (OrderItem oi : itemsInRange) {
            if (oi.getMenu() != null) {
                String cat = oi.getMenu().getMenuType();
                categoryCount.put(cat, categoryCount.getOrDefault(cat, 0) + 1);
            }
        }

        // Превращаем Map в две структуры (Labels, Data) для диаграммы:
        List<String> categoryLabels = new ArrayList<>(categoryCount.keySet());
        // сортируем по названию или по числу (необязательно)
        categoryLabels.sort(String::compareTo);
        List<Integer> categoryValues = new ArrayList<>();
        for (String cat : categoryLabels) {
            categoryValues.add(categoryCount.get(cat));
        }

        model.addAttribute("catLabels", categoryLabels);
        model.addAttribute("catValues", categoryValues);

        // 2) "Самое посещаемое время" за период
        // Будем считать почасовую активность (сколько заказов в 10:00, 11:00, etc.).
        // Для этого берем все заказы (Orders) в период, берем orderTime.getHour().
        // Считаем в массив/мапу hour -> count.
        List<Orders> ordersInRange = ordersRepository.findAllByOrderTimeBetween(startDateTime, endDateTime);
        int[] hoursCount = new int[24]; // индекс = 0..23
        for (Orders o : ordersInRange) {
            if (o.getOrderTime() != null) {
                int h = o.getOrderTime().getHour(); // 0..23
                hoursCount[h]++;
            }
        }

        // Превратим в списки для графика
        List<String> hoursLabels = new ArrayList<>();
        List<Integer> hoursValues = new ArrayList<>();
        for (int h = 0; h < 24; h++) {
            hoursLabels.add(String.format("%02d:00", h));
            hoursValues.add(hoursCount[h]);
        }

        model.addAttribute("hoursLabels", hoursLabels);
        model.addAttribute("hoursValues", hoursValues);

        return "statistics";
    }
}
