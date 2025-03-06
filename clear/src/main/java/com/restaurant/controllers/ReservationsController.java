package com.restaurant.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.entities.AllTables;
import com.restaurant.entities.Reservation;
import com.restaurant.entities.ReservedService;
import com.restaurant.entities.Service;
import com.restaurant.repositories.AllTablesRepository;
import com.restaurant.repositories.ReservationRepository;
import com.restaurant.repositories.ReservedServiceRepository;
import com.restaurant.repositories.ServiceRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/reserve")
public class ReservationsController {

    private final ReservationRepository reservationRepository;
    private final AllTablesRepository allTablesRepository;
    private final ServiceRepository serviceRepository;
    private final ReservedServiceRepository reservedServiceRepository;
    private final ObjectMapper objectMapper;

    public ReservationsController(
            ReservationRepository reservationRepository,
            AllTablesRepository allTablesRepository,
            ServiceRepository serviceRepository,
            ReservedServiceRepository reservedServiceRepository,
            ObjectMapper objectMapper
    ) {
        this.reservationRepository = reservationRepository;
        this.allTablesRepository = allTablesRepository;
        this.serviceRepository = serviceRepository;
        this.reservedServiceRepository = reservedServiceRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Страница с бронями.
     * - Неавторизованный пользователь увидит только форму для создания брони.
     * - Админ (ROLE_ADMIN) увидит таблицу со всеми бронями (и возможность редактировать/удалять).
     */
    @GetMapping
    public String showReservations(Model model, Authentication authentication) throws Exception {
        // Список столов (для выпадающего списка)
        List<AllTables> allTables = allTablesRepository.findAll();
        model.addAttribute("allTables", allTables);

        // Список всех услуг (для мультиселекта и для админ-раздела)
        List<Service> services = serviceRepository.findAll();
        model.addAttribute("services", services);

        // Превращаем в JSON, если понадобится в JS
        model.addAttribute("servicesJson", objectMapper.writeValueAsString(services));

        // Проверяем, является ли пользователь админом
        String role = authentication != null ? authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("GUEST") : "GUEST";  // Если не аутентифицирован, то роль GUEST

        // Передаем роль в модель
        model.addAttribute("role", role);

        System.out.println(role);

        boolean isAdmin = role.equals("ROLE_WAITER") || role.equals("ROLE_ADMIN");
        model.addAttribute("isAdmin", isAdmin);

        LocalDateTime currentDateTime = LocalDateTime.now();

        // Форматируем дату и время в строку
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = currentDateTime.format(formatter);
        System.out.println(formattedDateTime);

        // Передаем дату и время в модель
        model.addAttribute("currentDT", formattedDateTime);
        List<Reservation> reservations = reservationRepository.findAll();
        model.addAttribute("reservations", reservations);
        String reservationsJson = objectMapper.writeValueAsString(reservations);
        model.addAttribute("reservationsJson", reservationsJson);

        return "reservations";
    }

    /**
     * (Для неавторизованных) Создание новой брони (POST /reserve/new).
     * Принимаем:
     *  - tableId
     *  - reservationDate (строка в формате "yyyy-MM-dd")
     *  - reservationTime (строка в формате "HH:mm:ss")
     *  - name
     *  - phone
     *  - services[] (список Long ID услуг)
     */
    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<?> createReservation(@RequestBody Map<String, Object> payload) {
        try {
            Integer tableId = Integer.valueOf(payload.get("tableId").toString());
            String reservationDateStr = payload.get("reservationDate").toString();
            String reservationTimeStr = payload.get("reservationTime").toString();
            String name = payload.get("name").toString();
            String phone = payload.get("phone").toString();

            // Получаем выбранный столик
            AllTables table = allTablesRepository.findById(tableId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid table ID"));

            // Создаем объект Reservation и устанавливаем столик
            Reservation reservation = new Reservation();
            reservation.setReservationDate(LocalDate.parse(reservationDateStr));
            reservation.setReservationTime(LocalTime.parse(reservationTimeStr));
            reservation.setName(name);
            reservation.setPhoneNumber(phone);
            reservation.setTable(table);

            // Сохраняем бронь
            Reservation savedRes = reservationRepository.save(reservation);

            // Добавляем услуги (если они были выбраны)
            List<Integer> serviceIds = (List<Integer>) payload.get("services");
            if (serviceIds != null) {
                for (Integer sid : serviceIds) {
                    Service srv = serviceRepository.findById(sid.longValue())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid service ID: " + sid));
                    ReservedService rs = new ReservedService();
                    rs.setReservation(savedRes);
                    rs.setService(srv);
                    reservedServiceRepository.save(rs);
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("reservationId", savedRes.getId());
            result.put("message", "Reservation created successfully!");
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }





    /**
     * Возврат списка услуг (ReservedService) для конкретной брони
     */
    @GetMapping("/{resId}/services")
    @ResponseBody
    public ResponseEntity<?> getReservedServices(@PathVariable Long resId) {
        Reservation reservation = reservationRepository.findById(resId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID"));

        List<ReservedService> rsList = reservedServiceRepository.findByReservation_Id(resId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (ReservedService rs : rsList) {
            Map<String, Object> item = new HashMap<>();
            item.put("reservedServiceId", rs.getId());
            item.put("serviceName", rs.getService().getServiceName());
            item.put("description", rs.getService().getDescription());
            result.add(item);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Добавление услуг (ReservedService) к существующей брони (admin)
     * Принимаем JSON с { "serviceIds": [1, 2, 3] }
     */
    @PostMapping("/{resId}/services/add")
    @ResponseBody
    public ResponseEntity<?> addServices(@PathVariable Long resId, @RequestBody Map<String, Object> payload) {
        Reservation reservation = reservationRepository.findById(resId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID"));

        List<Integer> serviceIds = (List<Integer>) payload.get("serviceIds");
        if (serviceIds == null || serviceIds.isEmpty()) {
            return ResponseEntity.badRequest().body("No service IDs provided.");
        }

        for (Integer sid : serviceIds) {
            Service srv = serviceRepository.findById(sid.longValue())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid service ID: " + sid));
            ReservedService rs = new ReservedService();
            rs.setReservation(reservation);
            rs.setService(srv);
            reservedServiceRepository.save(rs);
        }
        return ResponseEntity.ok("Services added successfully");
    }

    /**
     * Удаление одной записи ReservedService
     */
    @PostMapping("/{resId}/services/delete/{reservedServiceId}")
    @ResponseBody
    public ResponseEntity<?> deleteService(@PathVariable Long resId, @PathVariable Long reservedServiceId) {
        ReservedService rs = reservedServiceRepository.findById(reservedServiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reserved service ID"));
        if (!rs.getReservation().getId().equals(resId)) {
            return ResponseEntity.badRequest().body("Service does not belong to this reservation");
        }
        reservedServiceRepository.delete(rs);
        return ResponseEntity.ok("Service removed.");
    }

    /**
     * Редактирование (inline) списка бронирований.
     * Принимает массив объектов:
     * [{ "id": 1, "reservationDate": "yyyy-MM-dd", "reservationTime": "HH:mm:ss", "name": "...", "phoneNumber": "..." }, ...]
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<?> updateReservations(@RequestBody List<Map<String, String>> updatedData) {
        for (Map<String, String> item : updatedData) {
            Long rId = Long.parseLong(item.get("id"));
            Reservation reservation = reservationRepository.findById(rId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID: " + rId));

            String reservationDateStr = item.get("reservationDate");
            String reservationTimeStr = item.get("reservationTime");
            reservation.setReservationDate(LocalDate.parse(reservationDateStr));
            reservation.setReservationTime(LocalTime.parse(reservationTimeStr));
            reservation.setName(item.get("name"));
            reservation.setPhoneNumber(item.get("phoneNumber"));
            reservationRepository.save(reservation);
        }
        return ResponseEntity.ok("Updated successfully");
    }

    /**
     * Удаление брони (admin)
     */
    @PostMapping("/delete/{resId}")
    @ResponseBody
    public ResponseEntity<?> deleteReservation(@PathVariable Long resId) {
        reservationRepository.deleteById(resId);
        return ResponseEntity.ok("Reservation deleted");
    }

    /**
     * Получить занятые времена для конкретного столика и даты
     * @param tableId - ID стола
     * @param reservationDateStr - строка с датой в формате "yyyy-MM-dd"
     * @return Список занятых времен на этот день для указанного стола
     */
    @GetMapping("/getReservedTimes")
    @ResponseBody
    public ResponseEntity<?> getReservedTimes(@RequestParam("tableId") Integer tableId, @RequestParam("reservationDate") String reservationDateStr) {
        try {
            // Преобразуем строку в LocalDate
            LocalDate reservationDate = LocalDate.parse(reservationDateStr);

            // Получаем столик по ID
            AllTables table = allTablesRepository.findById(tableId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid table ID"));

            // Получаем все бронирования для выбранного стола и даты
            List<Reservation> reservations = reservationRepository.findByTableAndReservationDate(table, reservationDate);

            // Извлекаем все времена бронирований
            List<String> reservedTimes = new ArrayList<>();
            for (Reservation reservation : reservations) {
                reservedTimes.add(reservation.getReservationTime().toString());
            }

            // Возвращаем занятые времена
            return ResponseEntity.ok(reservedTimes);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid input or error occurred: " + e.getMessage()));
        }
    }

}
