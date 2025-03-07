package com.restaurant.controllers;

import com.restaurant.entities.*;
import com.restaurant.repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Контроллер для работы с элементами заказов.
 * Обрабатывает запросы для получения, добавления и удаления элементов заказа.
 */
@RestController
public class OrderItemsController {

    private final OrderItemRepository orderItemRepository;
    private final OrdersRepository orderRepository;
    private final MenuRepository menuRepository;
    private final RecipeRepository recipeRepository;
    private final StockRepository stockRepository;

    /**
     * Конструктор для инициализации репозиториев, используемых в контроллере.
     *
     * @param orderItemRepository Репозиторий для работы с элементами заказа.
     * @param orderRepository Репозиторий для работы с заказами.
     * @param menuRepository Репозиторий для работы с меню.
     * @param recipeRepository Репозиторий для работы с рецептами.
     * @param stockRepository Репозиторий для работы с запасами ингредиентов.
     */
    public OrderItemsController(OrderItemRepository orderItemRepository,
                                OrdersRepository orderRepository,
                                MenuRepository menuRepository, RecipeRepository recipeRepository, StockRepository stockRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.recipeRepository = recipeRepository;
        this.stockRepository = stockRepository;
    }

    /**
     * Возвращает список элементов заказа для заданного orderId.
     * Для каждого элемента возвращаются: orderItemId, dishName, price и description.
     *
     * @param orderId ID заказа, для которого нужно получить элементы.
     * @return Список элементов заказа с их деталями.
     */
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

    /**
     * Проверяет наличие необходимых ингредиентов на складе для выбранного блюда.
     *
     * @param menuId ID блюда из меню, для которого нужно проверить наличие ингредиентов.
     * @return true, если все ингредиенты доступны в необходимом количестве; false в противном случае.
     */
    public boolean isIngredientAvailable(Long menuId) {
        List<Recipe> recipes = recipeRepository.findByMenuMenuId(menuId);

        for (Recipe recipe : recipes) {
            AllItems ingredient = recipe.getItem(); // Получаем ингредиент из рецепта
            Double requiredAmount = recipe.getAmount(); // Получаем количество ингредиента, необходимое для блюда

            // Ищем ингредиент на складе
            Stock stock = stockRepository.findByItemItemId(ingredient.getItemId());

            if (stock == null || stock.getAmount() < requiredAmount) {
                return false; // Если ингредиента недостаточно, возвращаем false
            }
        }
        return true; // Если все ингредиенты есть в нужном количестве
    }

    /**
     * Добавляет новый элемент в заказ.
     * Проверяет наличие ингредиентов на складе перед добавлением.
     *
     * @param orderId ID заказа, в который нужно добавить элемент.
     * @param payload Содержит данные о меню и описании для нового элемента заказа.
     * @return Ответ с данными добавленного элемента заказа.
     */
    @PostMapping("/orders/{orderId}/items/add")
    @ResponseBody
    public Map<String, Object> addOrderItem(@PathVariable Long orderId, @RequestBody Map<String, String> payload) {
        Long menuId = Long.parseLong(payload.get("menuId"));
        String description = payload.get("description");

        if (description == null || description.trim().isEmpty()) {
            description = "-";
        }

        // Проверка наличия ингредиентов
        if (!isIngredientAvailable(menuId)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", true);
            errorResponse.put("message", "Недостаточно ингредиентов на складе для выбранного блюда");
            return errorResponse;  // Отправляем сообщение об ошибке
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

    /**
     * Удаляет элемент заказа с указанным orderItemId, если он принадлежит заказу с orderId.
     *
     * @param orderId ID заказа, из которого нужно удалить элемент.
     * @param orderItemId ID элемента заказа, который нужно удалить.
     * @return Ответ с кодом состояния 200 OK при успешном удалении.
     */
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
