# RideMatchingService

A Spring Boot REST API for matching riders with the nearest available driver.

## Prerequisites
Make sure you have the following installed:
- Java 21+
- Maven 3.8+

## How to Run

### 1. Clone the Repository
```bash
git clone https://github.com/GeoMall/RideMatchingService.git
cd RideMatchingService
```

### 2. Build the Project
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

The API will start on `http://localhost:8080`

---

## API Endpoints

### Driver Endpoints
| Method | URL | Description |
|---|---|---|
| POST | `/api/drivers/register` | Register a new driver |
| POST | `/api/drivers/nearest` | Get all nearest available drivers |
| PUT | `/api/drivers/{id}/availability` | Update driver availability and location |
| GET | `/api/drivers` | Get all drivers |
| GET | `/api/drivers/{id}` | Get driver by ID |
| GET | `/api/drivers/available` | Get all available drivers |

### Ride Endpoints
| Method | URL | Description |
|---|---|---|
| POST | `/api/rides/request` | Request a ride |
| GET | `/api/rides` | Get all rides |
| GET | `/api/rides/{id}` | Get ride by ID |
| PATCH | `/api/rides/{id}/complete` | Complete a ride |

---

## Testing the API
### Swagger UI
Once the application starts you can access Swagger Ui Console `http://localhost:8080/swagger-ui/index.html` to find the api endpoints and schemas available.

### H2 Database Console
The in-memory H2 database console is available at:
`http://localhost:8080/h2-console`

| Field    | Value                  |
|----------|------------------------|
| JDBC URL | `jdbc:h2:mem:RideMatchingDb`|
| Username | `sa`                   |
| Password | *(leave blank)*        |


### Sample Curl Commands

### 1. Register Drivers
```bash
curl -X POST http://localhost:8080/api/drivers/register -H "Content-Type: application/json" -d "{\"name\": \"John Smith\", \"latitude\": 35.9042, \"longitude\": 14.5189, \"available\": true}"
```
```bash
curl -X POST http://localhost:8080/api/drivers/register -H "Content-Type: application/json" -d "{\"name\": \"Jane Doe\", \"latitude\": 35.9100, \"longitude\": 14.5300, \"available\": true}"
```
```bash
curl -X POST http://localhost:8080/api/drivers/register -H "Content-Type: application/json" -d "{\"name\": \"Bob Martin\", \"latitude\": 35.8900, \"longitude\": 14.5100, \"available\": true}"
```

### 2. Get All Drivers
```bash
curl -X GET http://localhost:8080/api/drivers
```

### 3. Get Available Drivers
```bash
curl -X GET http://localhost:8080/api/drivers/available
```

### 4. Get Driver By ID
```bash
curl -X GET http://localhost:8080/api/drivers/1
```

### 5. Get Nearest Drivers
```bash
curl -X POST http://localhost:8080/api/drivers/nearest -H "Content-Type: application/json" -d "{\"latitude\": 35.9042, \"longitude\": 14.5189}"
```

### 6. Update Driver
```bash
curl -X PUT http://localhost:8080/api/drivers/1 -H "Content-Type: application/json" -d "{ \"latitude\": 35.9042, \"longitude\": 14.5189, \"available\": false}
```

### 7. Request a Ride
```bash
curl -X POST http://localhost:8080/api/rides/request -H "Content-Type: application/json" -d "{\"riderName\": \"Alice\", \"pickupLatitude\": 35.9042, \"pickupLongitude\": 14.5189}"
```

### 8. Get All Rides
```bash
curl -X GET http://localhost:8080/api/rides
```

### 9. Get Ride by ID
```bash
curl -X GET http://localhost:8080/api/rides/1
```

### 10. Complete a Ride
```bash
curl -X PATCH http://localhost:8080/api/rides/1/complete
```

---

## Project Structure
```
src/main/java/com/example/RideMatchingService/
├── controller/
│   ├── DriverController.java
│   └── RideController.java
├── dto/
│   ├── driver/
│   │   └── DriverCreateDTO.java
│   │   └── DriverDTO.java
│   │   └── DriverRequestDTO.java
│   ├── ride/
│   │   ├── RideRequestDTO.java
│   │   └── RideResponseDTO.java
│   └── ErrorResponseDTO.java
├── exception/
│   └── GlobalExceptionHandler.java
├── model/
│   ├── Driver.java
│   ├── DriverLocationRequest.java
│   ├── Ride.java
│   └── RideStatus.java
├── repository/
│   ├── DriverRepository.java
│   └── RideRepository.java
├── service/
│   ├── DistanceService.java
│   ├── DriverService.java
│   └── RideService.java
└── RideMatchingServiceApplication.java

src/test/java/com/example/RideMatchingService/
├── DriverControllerTest.java
└── RideControllerTest.java
```
