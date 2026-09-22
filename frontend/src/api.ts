import type { Hotel, Room, BookingRequest, BookingResponse } from './types';

const API_BASE = 'http://localhost:8080/api';

export const api = {
  // Obtener lista de hoteles
  async getHotels(): Promise<Hotel[]> {
    const res = await fetch(`${API_BASE}/hotels`);
    if (!res.ok) throw new Error('Error al cargar hoteles');
    return res.json();
  },

  // Obtener habitaciones disponibles por rango de fechas
  async getAvailableRooms(hotelId: number, checkIn: string, checkOut: string): Promise<Room[]> {
    const res = await fetch(
      `${API_BASE}/hotels/${hotelId}/available-rooms?checkIn=${checkIn}&checkOut=${checkOut}`
    );
    if (!res.ok) throw new Error('Error al consultar disponibilidad');
    return res.json();
  },

  // Crear una reserva
  async createBooking(roomId: number, booking: BookingRequest): Promise<BookingResponse> {
    const res = await fetch(`${API_BASE}/rooms/${roomId}/bookings`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(booking),
    });
    if (!res.ok) {
      const err = await res.text();
      throw new Error(err || 'Error al procesar la reserva');
    }
    return res.json();
  }
};