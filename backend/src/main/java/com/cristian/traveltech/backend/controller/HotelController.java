package com.cristian.traveltech.backend.controller;

import com.cristian.traveltech.backend.model.Booking;
import com.cristian.traveltech.backend.model.Hotel;
import com.cristian.traveltech.backend.model.Room;
import com.cristian.traveltech.backend.repository.HotelRepository;
import com.cristian.traveltech.backend.repository.RoomRepository;
import com.cristian.traveltech.backend.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Habilita peticiones cruzadas desde el frontend
@RequiredArgsConstructor
public class HotelController {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final BookingService bookingService;

    // Obtener todos los hoteles o filtrar por ciudad
    @GetMapping("/hotels")
    public ResponseEntity<List<Hotel>> getHotels(@RequestParam(required = false) String city) {
        if (city != null && !city.isBlank()) {
            return ResponseEntity.ok(hotelRepository.findByCityIgnoreCase(city));
        }
        return ResponseEntity.ok(hotelRepository.findAll());
    }

    // Guardar nuevo hotel
    @PostMapping("/hotels")
    public ResponseEntity<Hotel> createHotel(@Valid @RequestBody Hotel hotel) {
        return new ResponseEntity<>(hotelRepository.save(hotel), HttpStatus.CREATED);
    }

    // Buscar habitaciones disponibles para un hotel en un rango de fechas
    @GetMapping("/hotels/{hotelId}/available-rooms")
    public ResponseEntity<List<Room>> getAvailableRooms(
            @PathVariable Long hotelId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {
        return ResponseEntity.ok(roomRepository.findAvailableRooms(hotelId, checkIn, checkOut));
    }

    // Crear una reserva
    @PostMapping("/rooms/{roomId}/bookings")
    public ResponseEntity<Booking> createBooking(
            @PathVariable Long roomId,
            @Valid @RequestBody Booking booking) {
        return new ResponseEntity<>(bookingService.createBooking(booking, roomId), HttpStatus.CREATED);
    }

    // Listar reservas de un cliente por email
    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getBookings(@RequestParam(required = false) String email) {
        if (email != null && !email.isBlank()) {
            return ResponseEntity.ok(bookingService.getBookingsByGuest(email));
        }
        return ResponseEntity.ok(bookingService.getAllBookings());
    }
}