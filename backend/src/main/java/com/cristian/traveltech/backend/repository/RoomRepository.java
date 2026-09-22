package com.cristian.traveltech.backend.repository;

import com.cristian.traveltech.backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelId(Long hotelId);

    // Consulta para encontrar habitaciones disponibles sin reservas solapadas en ese rango de fechas
    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId AND r.id NOT IN (" +
            "  SELECT b.room.id FROM Booking b " +
            "  WHERE b.room.hotel.id = :hotelId " +
            "  AND (:checkIn < b.checkOutDate AND :checkOut > b.checkInDate)" +
            ")")
    List<Room> findAvailableRooms(
            @Param("hotelId") Long hotelId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );
}