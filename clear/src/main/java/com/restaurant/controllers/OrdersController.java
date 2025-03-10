package com.restaurant.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.entities.*;
import com.restaurant.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Контроллер для работы с заказами.
 * Обрабатывает запросы для отображения, добавления, обновления и удаления заказов.
 */
@Controller
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersRepository ordersRepository;
    private final AllTablesRepository allTablesRepository;
    private final MenuRepository menuRepository;
    private final ObjectMapper objectMapper;
    private final StockRepository stockRepository;
    private final RecipeRepository recipeRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * Конструктор для инициализации зависимостей контроллера.
     *
     * @param orderItemRepository Репозиторий для работы с элементами заказа.
     * @param recipeRepository Репозиторий для работы с рецептами.
     * @param stockRepository Репозиторий для работы с запасами.
     * @param ordersRepository Репозиторий для работы с заказами.
     * @param allTablesRepository Репозиторий для работы с таблицами.
     * @param menuRepository Репозиторий для работы с меню.
     * @param objectMapper Объект для преобразования объектов в JSON.
     */
    public OrdersController(OrderItemRepository orderItemRepository, RecipeRepository recipeRepository, StockRepository stockRepository, OrdersRepository ordersRepository, AllTablesRepository allTablesRepository, MenuRepository menuRepository, ObjectMapper objectMapper) {
        this.ordersRepository = ordersRepository;
        this.allTablesRepository = allTablesRepository;
        this.menuRepository = menuRepository;
        this.objectMapper = objectMapper;
        this.stockRepository = stockRepository;
        this.recipeRepository = recipeRepository;
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * Отображает страницу заказов, включая меню и информацию о столах.
     *
     * @param model Модель, в которую добавляются данные для отображения на странице.
     * @param authentication Данные аутентификации пользователя.
     * @return Имя шаблона для отображения.
     * @throws Exception При возникновении ошибок при обработке данных.
     */
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

        // Получаем роль пользователя и передаем в модель
        String role = authentication != null ? authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("GUEST") : "GUEST";  // Если не аутентифицирован, то роль GUEST

        model.addAttribute("role", role);
        boolean isAdmin = role.equals("ROLE_WAITER") || role.equals("ROLE_ADMIN");
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("menuItems", menuItems);

        return "orders";
    }

    /**
     * Обновляет заказы на основе полученных данных.
     *
     * @param updatedOrders Список карт с данными об обновляемых заказах.
     * @return Ответ с кодом состояния 200 OK при успешном обновлении.
     */
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

    /**
     * Добавляет новый заказ в систему.
     *
     * @param payload Данные для создания нового заказа, включая ID стола и описание.
     * @return Ответ с добавленным заказом.
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Orders> addOrder(@RequestBody Map<String, String> payload) {
        Orders newOrder = new Orders();
        Integer tableId = Integer.parseInt(payload.get("allTables"));
        String description = payload.get("description");
        AllTables table = allTablesRepository.findById(tableId).orElse(null);
        newOrder.setAllTables(table);
        newOrder.setDescription(description);

        Orders savedOrder = ordersRepository.save(newOrder);

        return ResponseEntity.ok(savedOrder);
    }

    /**
     * Удаляет заказ по заданному ID.
     *
     * @param id ID заказа, который нужно удалить.
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public void deleteOrder(@PathVariable Long id) {
        ordersRepository.deleteById(id);
    }
}
