package com.restaurant;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Примеры паролей
        String adminPassword = "admin123";
        String managerPassword = "manager123";
        String employeePassword = "employee123";

        // Генерация хэшей
        String adminHash = encoder.encode(adminPassword);
        String managerHash = encoder.encode(managerPassword);
        String employeeHash = encoder.encode(employeePassword);

        // Вывод хэшей для использования
        System.out.println("Admin: " + adminHash);
        System.out.println("Manager: " + managerHash);
        System.out.println("Employee: " + employeeHash);
    }
}
