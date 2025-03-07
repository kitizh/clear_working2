package com.restaurant.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

/**
 * Класс, представляющий забронированную услугу для определенной резервации.
 * Содержит информацию о связи между резервацией и услугой, которая была выбрана клиентом.
 */
@Entity
@Table(name = "reserved_services")
public class ReservedService {

    /**
     * Уникальный идентификатор забронированной услуги.
     * Этот идентификатор генерируется автоматически в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Связь с таблицей {@link Reservation}.
     * Это поле представляет собой резервацию, к которой относится забронированная услуга.
     */
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    /**
     * Связь с таблицей {@link Service}.
     * Это поле представляет собой услугу, которая была забронирована.
     */
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    // Геттеры и сеттеры

    /**
     * Возвращает уникальный идентификатор забронированной услуги.
     *
     * @return Идентификатор забронированной услуги.
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор забронированной услуги.
     *
     * @param id Идентификатор забронированной услуги.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Возвращает резервацию, к которой относится данная услуга.
     *
     * @return Резервация, к которой привязана услуга.
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * Устанавливает резервацию, к которой относится данная услуга.
     *
     * @param reservation Резервация, к которой привязана услуга.
     */
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    /**
     * Возвращает услугу, которая была забронирована для резервации.
     *
     * @return Услуга, забронированная для резервации.
     */
    public Service getService() {
        return service;
    }

    /**
     * Устанавливает услугу, которая была забронирована для резервации.
     *
     * @param service Услуга, забронированная для резервации.
     */
    public void setService(Service service) {
        this.service = service;
    }
}
