package com.restaurant;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Примеры паролей
        String adminPassword = "admin123";
        String managerPassword = "manager123";
        String cookPassword = "cook123";
        String waiterPassword = "waiter123";

        // Генерация хэшей
        String adminHash = encoder.encode(adminPassword);
        String managerHash = encoder.encode(managerPassword);
        String cookHash = encoder.encode(cookPassword);
        String waiterHash = encoder.encode(waiterPassword);

        // Вывод хэшей для использования
        System.out.println("Admin: " + adminHash);
        System.out.println("Manager: " + managerHash);
        System.out.println("Cook: " + cookHash);
        System.out.println("Waiter: " + waiterHash);
    }
}
