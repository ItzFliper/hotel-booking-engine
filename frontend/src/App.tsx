import { useState, useEffect } from 'react';
import { api } from './api';
import type { Hotel, Room, BookingResponse } from './types';
import { Calendar, Building, CheckCircle, AlertCircle } from 'lucide-react';

export default function App() {
  const [hotels, setHotels] = useState<Hotel[]>([]);
  const [selectedHotel, setSelectedHotel] = useState<number | null>(null);
  const [checkIn, setCheckIn] = useState('');
  const [checkOut, setCheckOut] = useState('');
  const [availableRooms, setAvailableRooms] = useState<Room[]>([]);
  const [selectedRoom, setSelectedRoom] = useState<Room | null>(null);

  // Formulario reserva
  const [guestName, setGuestName] = useState('');
  const [guestEmail, setGuestEmail] = useState('');
  const [bookingResult, setBookingResult] = useState<BookingResponse | null>(null);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  // Cargar lista de hoteles inicial
  useEffect(() => {
    api.getHotels()
      .then((data) => {
        setHotels(data);
        if (data.length > 0) setSelectedHotel(data[0].id);
      })
      .catch((err) => setErrorMsg(err.message));
  }, []);

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedHotel || !checkIn || !checkOut) return;

    setErrorMsg(null);
    setBookingResult(null);
    setSelectedRoom(null);
    setLoading(true);

    try {
      const rooms = await api.getAvailableRooms(selectedHotel, checkIn, checkOut);
      setAvailableRooms(rooms);
    } catch (err: any) {
      setErrorMsg(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleBook = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedRoom) return;

    setErrorMsg(null);
    setLoading(true);

    try {
      const result = await api.createBooking(selectedRoom.id, {
        guestName,
        guestEmail,
        checkInDate: checkIn,
        checkOutDate: checkOut,
      });
      setBookingResult(result);
      setSelectedRoom(null);
      // Refrescar disponibilidad
      if (selectedHotel) {
        const rooms = await api.getAvailableRooms(selectedHotel, checkIn, checkOut);
        setAvailableRooms(rooms);
      }
    } catch (err: any) {
      setErrorMsg(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <header style={{ marginBottom: '2rem' }}>
        <h1 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: '#0f172a' }}>
          <Building color="#2563eb" /> TravelTech Booking Engine
        </h1>
        <p style={{ color: '#64748b' }}>Motor de reservas y disponibilidad en tiempo real</p>
      </header>

      {errorMsg && (
        <div className="card" style={{ borderLeft: '5px solid #ef4444', color: '#b91c1c', display: 'flex', gap: '0.5rem', alignItems: 'center' }}>
          <AlertCircle size={20} />
          <span>{errorMsg}</span>
        </div>
      )}

      {bookingResult && (
        <div className="card" style={{ borderLeft: '5px solid #10b981', color: '#047857' }}>
          <div style={{ display: 'flex', gap: '0.5rem', alignItems: 'center', marginBottom: '0.5rem' }}>
            <CheckCircle size={24} />
            <h3 style={{ margin: 0 }}>¡Reserva confirmada con éxito!</h3>
          </div>
          <p>Localizador: <strong>#{bookingResult.id}</strong></p>
          <p>Huésped: {bookingResult.guestName} ({bookingResult.guestEmail})</p>
          <p>Total: <strong>{bookingResult.totalPrice} €</strong></p>
        </div>
      )}

      {/* Buscador de Fechas y Hoteles */}
      <section className="card">
        <h2 style={{ fontSize: '1.25rem', marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <Calendar size={20} /> Buscar Disponibilidad
        </h2>
        <form onSubmit={handleSearch} style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem' }}>
          <div className="input-group">
            <label>Hotel</label>
            <select
              value={selectedHotel ?? ''}
              onChange={(e) => setSelectedHotel(Number(e.target.value))}
            >
              {hotels.map((h) => (
                <option key={h.id} value={h.id}>{h.name} ({h.city})</option>
              ))}
            </select>
          </div>

          <div className="input-group">
            <label>Entrada (Check-in)</label>
            <input
              type="date"
              required
              value={checkIn}
              onChange={(e) => setCheckIn(e.target.value)}
            />
          </div>

          <div className="input-group">
            <label>Salida (Check-out)</label>
            <input
              type="date"
              required
              value={checkOut}
              onChange={(e) => setCheckOut(e.target.value)}
            />
          </div>

          <div style={{ display: 'flex', alignItems: 'flex-end', marginBottom: '1rem' }}>
            <button type="submit" disabled={loading} style={{ width: '100%' }}>
              {loading ? 'Consultando...' : 'Consultar'}
            </button>
          </div>
        </form>
      </section>

      {/* Resultados de Habitaciones */}
      {availableRooms.length > 0 && (
        <section style={{ marginBottom: '2rem' }}>
          <h2 style={{ fontSize: '1.25rem', marginBottom: '1rem' }}>Habitaciones Disponibles</h2>
          <div className="grid">
            {availableRooms.map((room) => (
              <div key={room.id} className="card" style={{ border: selectedRoom?.id === room.id ? '2px solid #2563eb' : '1px solid #e2e8f0' }}>
                <h3 style={{ fontSize: '1.1rem', marginBottom: '0.25rem' }}>{room.type}</h3>
                <p style={{ color: '#64748b', fontSize: '0.9rem', marginBottom: '0.75rem' }}>Habitación {room.roomNumber} · Hasta {room.capacity} personas</p>
                <p style={{ fontSize: '1.25rem', fontWeight: 700, color: '#0f172a', marginBottom: '1rem' }}>
                  {room.pricePerNight} € <span style={{ fontSize: '0.85rem', fontWeight: 400, color: '#64748b' }}>/ noche</span>
                </p>
                <button
                  type="button"
                  onClick={() => setSelectedRoom(room)}
                  style={{ width: '100%', backgroundColor: selectedRoom?.id === room.id ? '#059669' : '#2563eb' }}
                >
                  {selectedRoom?.id === room.id ? 'Seleccionada' : 'Seleccionar'}
                </button>
              </div>
            ))}
          </div>
        </section>
      )}

      {/* Formulario de Checkout / Confirmación */}
      {selectedRoom && (
        <section className="card" style={{ border: '2px solid #2563eb' }}>
          <h2 style={{ fontSize: '1.25rem', marginBottom: '1rem' }}>Confirmar Reserva ({selectedRoom.type})</h2>
          <form onSubmit={handleBook}>
            <div className="input-group">
              <label>Nombre y Apellidos</label>
              <input
                type="text"
                required
                placeholder="Ej. Cristian Navarro"
                value={guestName}
                onChange={(e) => setGuestName(e.target.value)}
              />
            </div>
            <div className="input-group">
              <label>Correo Electrónico</label>
              <input
                type="email"
                required
                placeholder="ejemplo@email.com"
                value={guestEmail}
                onChange={(e) => setGuestEmail(e.target.value)}
              />
            </div>
            <button type="submit" disabled={loading} style={{ width: '100%', marginTop: '0.5rem' }}>
              {loading ? 'Confirmando...' : 'Confirmar Reserva'}
            </button>
          </form>
        </section>
      )}
    </div>
  );
}