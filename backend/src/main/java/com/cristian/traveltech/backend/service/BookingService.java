package com.cristian.traveltech.backend.service;

import com.cristian.traveltech.backend.model.Booking;
import com.cristian.traveltech.backend.model.Room;
import com.cristian.traveltech.backend.repository.BookingRepository;
import com.cristian.traveltech.backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public Booking createBooking(Booking booking, Long roomId) {
        if (!booking.getCheckOutDate().isAfter(booking.getCheckInDate())) {
            throw new IllegalArgumentException("La fecha de salida debe ser posterior a la fecha de entrada");
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Habitación no encontrada con ID: " + roomId));

        // Validación crítica anti-overbooking
        boolean isOccupied = bookingRepository.existsOverlappingBooking(
                roomId,
                booking.getCheckInDate(),
                booking.getCheckOutDate()
        );

        if (isOccupied) {
            throw new IllegalStateException("La habitación ya está ocupada en el rango de fechas seleccionado");
        }

        // Cálculo dinámico del precio total
        long days = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(days));

        booking.setRoom(room);
        booking.setTotalPrice(totalPrice);

        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByGuest(String email) {
        return bookingRepository.findByGuestEmail(email);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}