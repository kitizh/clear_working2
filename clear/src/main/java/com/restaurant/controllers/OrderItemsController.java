package com.restaurant.controllers;

import com.restaurant.entities.Menu;
import com.restaurant.entities.Orders;
import com.restaurant.entities.OrderItem;
import com.restaurant.repositories.MenuRepository;
import com.restaurant.repositories.OrderItemRepository;
import com.restaurant.repositories.OrdersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class OrderItemsController {

    private final OrderItemRepository orderItemRepository;
    private final OrdersRepository orderRepository;
    private final MenuRepository menuRepository;

    public OrderItemsController(OrderItemRepository orderItemRepository,
                                OrdersRepository orderRepository,
                                MenuRepository menuRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
    }

    // GET /orders/{orderId}/items
    // Возвращает список элементов заказа для заданного orderId.
    // Для каждого элемента возвращаются: orderItemId, dishName, price и description.
    @GetMapping("/orders/{orderId}/items")
    @ResponseBody
    public List<Map<String, Object>> getOrderItems(@PathVariable Long orderId) {
        List<OrderItem> items = orderItemRepository.findByOrderOrderId(orderId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (OrderItem item : items) {
            Map<String, Object> map = new HashMap<>();
            map.put("orderItemId", item.getOrderItemId());
            map.put("dishName", item.getMenu().getDishName());
            map.put("price", item.getMenu().getPrice());
            map.put("description", item.getDescription());
            result.add(map);
        }
        return result;
    }


    // POST /orders/{orderId}/items/add
    // Добавляет новый элемент заказа к заказу с id orderId.
    // Ожидается JSON с полями: menuId и description.
    @PostMapping("/orders/{orderId}/items/add")
    @ResponseBody
    public Map<String, Object> addOrderItem(@PathVariable Long orderId, @RequestBody Map<String, String> payload) {
        Long menuId = Long.parseLong(payload.get("menuId"));
        String description = payload.get("description");
        if (description == null || description.trim().isEmpty()) {
            description = "-";
        }
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order id"));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid menu id"));
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setMenu(menu);
        orderItem.setDescription(description);
        OrderItem savedItem = orderItemRepository.save(orderItem);

        Map<String, Object> response = new HashMap<>();
        response.put("orderItemId", savedItem.getOrderItemId());
        response.put("dishName", savedItem.getMenu().getDishName());
        response.put("price", savedItem.getMenu().getPrice());
        response.put("description", savedItem.getDescription());
        return response;
    }


    // POST /orders/{orderId}/items/delete/{orderItemId}
    // Удаляет элемент заказа с указанным orderItemId, если он принадлежит заказу с orderId.
    @PostMapping("/orders/{orderId}/items/delete/{orderItemId}")
    @ResponseBody
    public ResponseEntity<?> deleteOrderItem(@PathVariable Long orderId, @PathVariable Long orderItemId) {
        OrderItem item = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order item id"));
        if (!item.getOrder().getOrderId().equals(orderId)) {
            return ResponseEntity.badRequest().body("Order item does not belong to specified order");
        }
        orderItemRepository.deleteById(orderItemId);
        return ResponseEntity.ok().build();
    }

}
