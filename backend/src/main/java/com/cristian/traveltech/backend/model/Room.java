package com.cristian.traveltech.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El número de habitación es obligatorio")
    private String roomNumber;

    @NotBlank(message = "El tipo de habitación es obligatorio (ej. Doble, Suite)")
    private String type;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio por noche debe ser mayor que cero")
    @Column(nullable = false)
    private BigDecimal pricePerNight;

    @Min(value = 1, message = "La capacidad mínima es de 1 persona")
    @Column(nullable = false)
    private Integer capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    @JsonIgnore
    private Hotel hotel;
}