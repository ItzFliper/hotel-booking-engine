package com.cristian.traveltech.backend;

import com.cristian.traveltech.backend.model.Booking;
import com.cristian.traveltech.backend.model.Room;
import com.cristian.traveltech.backend.repository.BookingRepository;
import com.cristian.traveltech.backend.repository.RoomRepository;
import com.cristian.traveltech.backend.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private BookingService bookingService;

    private Room sampleRoom;
    private Booking sampleBooking;

    @BeforeEach
    void setUp() {
        sampleRoom = Room.builder()
                .id(1L)
                .roomNumber("101")
                .type("Deluxe")
                .pricePerNight(new BigDecimal("100.00"))
                .capacity(2)
                .build();

        sampleBooking = Booking.builder()
                .guestName("John Doe")
                .guestEmail("john@example.com")
                .checkInDate(LocalDate.now().plusDays(2))
                .checkOutDate(LocalDate.now().plusDays(5))
                .build();
    }

    @Test
    @DisplayName("Debe crear la reserva con éxito cuando la habitación está disponible")
    void shouldCreateBookingSuccessfully() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(sampleRoom));
        when(bookingRepository.existsOverlappingBooking(any(), any(), any())).thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.createBooking(sampleBooking, 1L);

        assertNotNull(result);
        assertEquals(sampleRoom, result.getRoom());
        assertEquals(new BigDecimal("300.00"), result.getTotalPrice()); // 3 noches x 100
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si las fechas se solapan con otra reserva")
    void shouldThrowExceptionWhenRoomIsAlreadyBooked() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(sampleRoom));
        when(bookingRepository.existsOverlappingBooking(any(), any(), any())).thenReturn(true);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> bookingService.createBooking(sampleBooking, 1L)
        );

        assertTrue(exception.getMessage().contains("ya está ocupada"));
        verify(bookingRepository, never()).save(any(Booking.class));
    }
}