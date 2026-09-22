package com.cristian.traveltech.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @NotBlank(message = "El nombre del huésped es obligatorio")
    private String guestName;

    @Email(message = "Formato de email no válido")
    @NotBlank(message = "El email del huésped es obligatorio")
    private String guestEmail;

    @NotNull(message = "La fecha de entrada es obligatoria")
    private LocalDate checkInDate;

    @NotNull(message = "La fecha de salida es obligatoria")
    @Future(message = "La fecha de salida debe ser futura")
    private LocalDate checkOutDate;

    @Column(nullable = false)
    private BigDecimal totalPrice;
}