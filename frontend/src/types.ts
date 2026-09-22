export interface Hotel {
  id: number;
  name: string;
  city: string;
  address: string;
}

export interface Room {
  id: number;
  roomNumber: string;
  type: string;
  pricePerNight: number;
  capacity: number;
}

export interface BookingRequest {
  guestName: string;
  guestEmail: string;
  checkInDate: string;
  checkOutDate: string;
}

export interface BookingResponse {
  id: number;
  guestName: string;
  guestEmail: string;
  checkInDate: string;
  checkOutDate: string;
  totalPrice: number;
  room: Room;
}