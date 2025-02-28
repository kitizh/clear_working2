package com.restaurant.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.entities.Stock;
import com.restaurant.entities.AllItems;
import com.restaurant.repositories.StockRepository;
import com.restaurant.repositories.AllItemsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/stock")
public class StockController {

    private final StockRepository stockRepository;
    private final AllItemsRepository allItemsRepository;
    private final ObjectMapper objectMapper;

    public StockController(StockRepository stockRepository, AllItemsRepository allItemsRepository, ObjectMapper objectMapper) {
        this.stockRepository = stockRepository;
        this.allItemsRepository = allItemsRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String getStock(Model model) throws Exception {
        // Получаем все продукты из таблицы all_items
        List<AllItems> allItems = allItemsRepository.findAll();
        // Получаем список уникальных типов продуктов
        List<String> productTypes = allItems.stream()
                .map(AllItems::getProductType)
                .distinct()
                .collect(Collectors.toList());

        // Получаем записи запаса (Stock)
        List<Stock> stockEntries = stockRepository.findAll();

        // Группируем записи запаса по типу продукта (через поле AllItems.productType)
        Map<String, List<Stock>> stockItems = new HashMap<>();
        for (String type : productTypes) {
            List<Stock> stocksForType = stockEntries.stream()
                    .filter(s -> type.equals(s.getItem().getProductType()))
                    .collect(Collectors.toList());
            stockItems.put(type, stocksForType);
        }

        // Формируем карту: productType -> List of AllItems (для динамического заполнения списка продуктов)
        Map<String, List<AllItems>> allItemsMap = allItems.stream()
                .collect(Collectors.groupingBy(AllItems::getProductType));

        model.addAttribute("productTypes", productTypes);
        model.addAttribute("stockItems", stockItems);
        // Передаём карту allItemsMap как JSON для JS
        model.addAttribute("allItemsJson", objectMapper.writeValueAsString(allItemsMap));

        // Определяем, является ли пользователь администратором
        // (пример – замените на свою логику; здесь для демонстрации всегда true)
        model.addAttribute("isAdmin", true);

        return "stock";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<?> updateStock(@RequestBody List<Stock> updatedStocks) {
        for (Stock updatedStock : updatedStocks) {
            Stock stock = stockRepository.findById(updatedStock.getStockId())
                    .orElseThrow(() -> new NoSuchElementException("Stock not found"));
            stock.setAmount(updatedStock.getAmount());
            stock.setUnit(updatedStock.getUnit());
            stockRepository.save(stock);
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteStock(@PathVariable Long id) {
        stockRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Метод добавления нового продукта в запас.
    // Ожидается JSON с полями: productType, itemName, amount, unit.
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Stock> addStock(@RequestBody Map<String, String> payload) {
        String productType = payload.get("productType");
        String itemName = payload.get("itemName");
        String amountStr = payload.get("amount");
        String unit = payload.get("unit");

        if (productType == null || productType.trim().isEmpty() ||
                itemName == null || itemName.trim().isEmpty() ||
                amountStr == null || amountStr.trim().isEmpty() ||
                unit == null || unit.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
        // Если продукта с таким именем нет, создаём новый AllItems
        AllItems item = allItemsRepository.findByItemName(itemName);
        if (item == null) {
            item = new AllItems();
            item.setItemName(itemName);
            item.setProductType(productType);
            item = allItemsRepository.save(item);
        }
        // Создаём новую запись Stock
        Stock stock = new Stock();
        stock.setItem(item);
        stock.setAmount(amount);
        stock.setUnit(unit);
        Stock savedStock = stockRepository.save(stock);
        return ResponseEntity.ok(savedStock);
    }
}
