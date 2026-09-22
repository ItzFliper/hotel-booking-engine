package com.cristian.traveltech.backend;

import com.cristian.traveltech.backend.model.Hotel;
import com.cristian.traveltech.backend.model.Room;
import com.cristian.traveltech.backend.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final HotelRepository hotelRepository;

    @Override
    public void run(String... args) {
        if (hotelRepository.count() == 0) {
            Hotel hotel = Hotel.builder()
                    .name("Palma Grand Luxury Hotel")
                    .city("Palma")
                    .address("Paseo Marítimo 15")
                    .build();

            Room room1 = Room.builder()
                    .roomNumber("101")
                    .type("Deluxe Sea View")
                    .pricePerNight(new BigDecimal("180.00"))
                    .capacity(2)
                    .hotel(hotel)
                    .build();

            Room room2 = Room.builder()
                    .roomNumber("102")
                    .type("Standard Double")
                    .pricePerNight(new BigDecimal("110.00"))
                    .capacity(2)
                    .hotel(hotel)
                    .build();

            hotel.setRooms(List.of(room1, room2));
            hotelRepository.save(hotel);
            System.out.println(">>> Datos de prueba iniciales cargados en la base de datos.");
        }
    }
}