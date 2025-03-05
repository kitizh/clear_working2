package com.restaurant.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_time", nullable = false)
    private LocalDateTime reservationTime;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    // Вместо @ManyToMany – OneToMany к bridge-entity ReservedTable
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReservedTable> reservedTables = new HashSet<>();

    // Вместо @ManyToMany – OneToMany к bridge-entity ReservedService
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReservedService> reservedServices = new HashSet<>();

    // Геттеры/сеттеры

    public Long getId() {
        return id;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<ReservedTable> getReservedTables() {
        return reservedTables;
    }

    public void setReservedTables(Set<ReservedTable> reservedTables) {
        this.reservedTables = reservedTables;
    }

    public Set<ReservedService> getReservedServices() {
        return reservedServices;
    }

    public void setReservedServices(Set<ReservedService> reservedServices) {
        this.reservedServices = reservedServices;
    }
}
