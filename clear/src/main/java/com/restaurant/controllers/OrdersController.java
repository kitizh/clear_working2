package com.restaurant.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.entities.*;
import com.restaurant.repositories.AllTablesRepository;
import com.restaurant.repositories.MenuRepository;
import com.restaurant.repositories.OrdersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersRepository ordersRepository;
    private final AllTablesRepository allTablesRepository;
    private final MenuRepository menuRepository;
    private final ObjectMapper objectMapper;

    public OrdersController(OrdersRepository ordersRepository, AllTablesRepository allTablesRepository, MenuRepository menuRepository, ObjectMapper objectMapper) {
        this.ordersRepository = ordersRepository;
        this.allTablesRepository = allTablesRepository;
        this.menuRepository = menuRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String showOrders(Model model, Authentication authentication) throws Exception {
        List<String> menuTypes = menuRepository.findAll().stream()
                .map(Menu::getMenuType)
                .distinct()
                .toList();

        Map<String, List<Menu>> menuItems = new HashMap<>();
        for (String type : menuTypes) {
            List<Menu> dishes = menuRepository.findByMenuType(type);
            menuItems.put(type, dishes);
        }
        model.addAttribute("allDishes", objectMapper.writeValueAsString(menuItems));
        List<Orders> orders = ordersRepository.findAll();
        List<AllTables> allTables = allTablesRepository.findAll();
        model.addAttribute("orders", orders);
        model.addAttribute("allTables", allTables);
        String ordersJson = objectMapper.writeValueAsString(orders);
        model.addAttribute("ordersJson", ordersJson);
        model.addAttribute("menuTypes", menuTypes);

        boolean isAdmin = authentication != null && authentication.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("menuItems", menuItems);

        return "orders";
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<?> updateOrders(@RequestBody List<Map<String, String>> updatedOrders) {
        for (Map<String, String> orderData : updatedOrders) {
            Long orderId = Long.parseLong(orderData.get("orderId"));
            int tableNumber = Integer.parseInt(orderData.get("tableNumber"));
            double totalAmount = Double.parseDouble(orderData.get("totalAmount"));
            String status = orderData.get("status");
            String description = orderData.get("description");

            Orders order = ordersRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));
            AllTables table = allTablesRepository.findById(tableNumber).orElse(null);
            order.setAllTables(table);
            order.setTotalAmount(totalAmount);
            order.setStatus(status);
            order.setDescription(description);
            ordersRepository.save(order);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Orders> addOrder(@RequestBody Map<String, String> payload) {
        Orders newOrder = new Orders();
        Integer tableId = Integer.parseInt(payload.get("allTables"));
        String description = payload.get("description");

        if (description == null || description.isEmpty()) {
            newOrder.setDescription("-");
        } else {
            newOrder.setDescription(description);
        }

        // Устанавливаем дату и время для заказа
        String orderDateStr = payload.get("orderDate");
        String orderTimeStr = payload.get("orderTime");

        newOrder.setOrderDate(LocalDate.parse(orderDateStr));  // Устанавливаем дату
        newOrder.setOrderTime(LocalTime.parse(orderTimeStr));  // Устанавливаем время

        AllTables table = allTablesRepository.findById(tableId).orElse(null);
        newOrder.setAllTables(table);
        newOrder.setTotalAmount(newOrder.getTotalAmount());
        newOrder.setStatus("Принят");

        Orders savedOrder = ordersRepository.save(newOrder);

        return ResponseEntity.ok(savedOrder);
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public void deleteOrder(@PathVariable Long id) {
        ordersRepository.deleteById(id);
    }
}
