package com.restaurant.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс, представляющий резервирование столика в ресторане.
 * Содержит информацию о дате и времени резервирования, имени клиента, номере телефона,
 * связанном столе и дополнительных услугах, которые были забронированы.
 */
@Entity
@Table(name = "reservations")
public class Reservation {

    /**
     * Уникальный идентификатор резервации.
     * Этот идентификатор генерируется автоматически в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата резервирования.
     * Указывает, на какой день запланирована резервированная встреча.
     */
    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    /**
     * Время резервирования.
     * Указывает, во сколько была сделана резервировка.
     */
    @Column(name = "reservation_time", nullable = false)
    private LocalTime reservationTime;

    /**
     * Имя клиента, сделавшего резервирование.
     * Это обязательное поле.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Номер телефона клиента, сделавшего резервирование.
     * Это обязательное поле для связи с клиентом.
     */
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    /**
     * Связь с таблицей {@link AllTables}.
     * Это поле представляет стол, который был забронирован для клиента.
     */
    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private AllTables table;

    /**
     * Связь с таблицей {@link ReservedService}.
     * Это множество услуг, которые были забронированы вместе с резервацией.
     * Связь с {@link ReservedService} реализована через каскадирование, так что удаление резервации удаляет также связанные услуги.
     */
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<ReservedService> reservedServices = new HashSet<>();

    // Геттеры и сеттеры

    /**
     * Возвращает уникальный идентификатор резервации.
     *
     * @return Идентификатор резервации.
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор резервации.
     *
     * @param id Идентификатор резервации.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Возвращает дату резервирования.
     *
     * @return Дата резервирования.
     */
    public LocalDate getReservationDate() {
        return reservationDate;
    }

    /**
     * Устанавливает дату резервирования.
     *
     * @param reservationDate Дата резервирования.
     */
    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    /**
     * Возвращает время резервирования.
     *
     * @return Время резервирования.
     */
    public LocalTime getReservationTime() {
        return reservationTime;
    }

    /**
     * Устанавливает время резервирования.
     *
     * @param reservationTime Время резервирования.
     */
    public void setReservationTime(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    /**
     * Возвращает имя клиента, сделавшего резервирование.
     *
     * @return Имя клиента.
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает имя клиента, сделавшего резервирование.
     *
     * @param name Имя клиента.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает номер телефона клиента.
     *
     * @return Номер телефона клиента.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Устанавливает номер телефона клиента.
     *
     * @param phoneNumber Номер телефона клиента.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Возвращает стол, который был забронирован.
     *
     * @return Стол для резервирования.
     */
    public AllTables getTable() {
        return table;
    }

    /**
     * Устанавливает стол, который был забронирован.
     *
     * @param table Стол для резервирования.
     */
    public void setTable(AllTables table) {
        this.table = table;
    }

    /**
     * Возвращает список забронированных услуг для данной резервации.
     *
     * @return Множество забронированных услуг.
     */
    public Set<ReservedService> getReservedServices() {
        return reservedServices;
    }

    /**
     * Устанавливает список забронированных услуг для данной резервации.
     *
     * @param reservedServices Множество забронированных услуг.
     */
    public void setReservedServices(Set<ReservedService> reservedServices) {
        this.reservedServices = reservedServices;
    }
}
