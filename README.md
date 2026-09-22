# 🏨 TravelTech Booking Engine

[![Java CI with Maven](https://github.com/ItzFliper/hotel-booking-engine/actions/workflows/ci.yml/badge.svg)](https://github.com/ItzFliper/hotel-booking-engine/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4+-brightgreen?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)
![React](https://img.shields.io/badge/React-19-61dafb?logo=react)
![TypeScript](https://img.shields.io/badge/TypeScript-5.0+-blue?logo=typescript)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue?logo=docker)

A full-stack, enterprise-grade booking and reservation engine designed for high-concurrency travel platforms. The system implements real-time room availability lookups, prevents overbooking via optimized relational SQL constraints, and provides a modern reactive client interface.

---

## 🛠 Tech Stack & Architecture

- **Backend:** Java 21 (Eclipse Temurin), Spring Boot 3, Spring Data JPA / Hibernate, Jakarta Validation, Lombok.
- **Frontend:** React 19, TypeScript, Vite, Lucide Icons, Modern Responsive CSS.
- **Database:** PostgreSQL 16 (running via Docker container).
- **Testing & Quality:** JUnit 5, Mockito, H2 in-memory DB for isolated CI environments.
- **CI/CD:** GitHub Actions automated build and test pipeline.

---

## 📐 System Features & Domain Logic

1. **Anti-Overbooking Algorithm:** Custom JPQL queries prevent double bookings within overlapping check-in/check-out date ranges.
2. **Automated Pricing Computation:** Calculates total reservation costs on the backend based on room day rates and stay durations.
3. **Reactive UI:** Instant date-range availability queries with live room filtering and visual checkout confirmations.
4. **Isolated CI Testing:** Unit and integration tests validate domain logic independently of external database infrastructure using in-memory profiles.

---

## 🚀 Getting Started

### Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [Java Development Kit (JDK) 21](https://adoptium.net/)
- [Node.js 20+](https://nodejs.org/)

### 1. Clone the repository

```bash
git clone [https://github.com/ItzFliper/hotel-booking-engine.git](https://github.com/ItzFliper/hotel-booking-engine.git)
cd hotel-booking-engine
2. Start PostgreSQL DatabaseBashdocker compose up -d
3. Run the Backend APIBashcd backend
./mvnw spring-boot:run
The REST API will be running at http://localhost:8080 and will seed initial mock data automatically.4. Run the Frontend ClientBashcd ../frontend
npm install
npm run dev
Access the web interface at http://localhost:5173.🔌 API Endpoints ReferenceMethodEndpointDescriptionGET/api/hotelsRetrieve all hotels or filter by ?city=POST/api/hotelsCreate and register a new hotelGET/api/hotels/{id}/available-roomsQuery rooms available between checkIn & checkOutPOST/api/rooms/{id}/bookingsCreate a booking with anti-overbooking verificationGET/api/bookingsList all bookings or filter by ?email=🧪 Running TestsTo run the automated test suite locally:Bashcd backend
./mvnw clean test
👤 AuthorCristian Navarro - GitHub Profile