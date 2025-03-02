package com.restaurant.controllers;

import com.restaurant.entities.AllTables;
import com.restaurant.entities.OrderItem;
import com.restaurant.entities.Orders;
import com.restaurant.repositories.AllTablesRepository;
import com.restaurant.repositories.OrderItemRepository;
import com.restaurant.repositories.OrdersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersRepository ordersRepository;
    private final AllTablesRepository allTablesRepository;
    private final OrderItemRepository orderItemRepository;

    public OrdersController(OrdersRepository ordersRepository, AllTablesRepository allTablesRepository, OrderItemRepository orderItemRepository) {
        this.ordersRepository = ordersRepository;
        this.allTablesRepository = allTablesRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @GetMapping
    public String showOrders(Model model) {
        List<Orders> orders = ordersRepository.findAll();
        model.addAttribute("orders", orders);
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



    @PostMapping("/delete/{id}")
    @ResponseBody
    public void deleteOrder(@PathVariable Long id) {
        ordersRepository.deleteById(id);
    }




}
