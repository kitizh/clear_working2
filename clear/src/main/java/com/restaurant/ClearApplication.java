package com.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения для запуска Spring Boot приложения.
 * Эта аннотация {@link SpringBootApplication} включает в себя конфигурацию и настройки
 * для автоконфигурации, компоненты Spring Boot и компонент сканирования пакетов.
 */
@SpringBootApplication
public class ClearApplication {

    /**
     * Главный метод, который запускает приложение.
     *
     * @param args Аргументы командной строки, передаваемые приложению при запуске.
     */
    public static void main(String[] args) {
        SpringApplication.run(ClearApplication.class, args);
    }
}
