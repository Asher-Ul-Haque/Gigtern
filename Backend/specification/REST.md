# REST API Endpoints

This is the first iteration of the endpoints for the MVP, prioritizing Identity / Trust, Core Functionality and Monetization.

## Feature List 
| Priority | Description | Purpose |
| --------------- | --------------- | --------------- |
| 1 | User Authentication and Identity | Trust and Feasibility for a closed network |
| 2 | Ride Listing | Driver can define a ride with multiple ordered stops |
| 3 | Ride Searching | Passenger can search for rides and see relevant information (stops, reputation, gender, price) |
| 4 | Ride Request and Acceptance Flow | Passenger requests a ride segment, Driver accepts, seat is booked |
| 5 | Mock Escrow | Simulate Payment and Settlement |

## Non Priority Features
* Real time GPS tracking
* Dynamic pricing
* Robust rating
* Payment Gateway integration

---

## Endpoint List 

### Authenticity and Identity 
| Method | Endpoint | Payload | Description |
| ---------- | ---------- | ---------- | ------- |
| POST | /api/v1/auth/signup/ | Body: User creation data, Returns JWT/ Auth Token, User object | Register a new user and perform mock IIIT-D verification, for now just note the IP |
| POST | /api/v1/auth/login/ | Body: Roll No, User Type, Returns: JWT/Auth Token/ User object | Log in an existing user |
| GET | /api/v1/users/{roll} | Returns: User object | Get a user's profile and reputation status | 


### Ride Management (Supply)
| Method | Endpoint | Payload | Description |
| ---------- | ---------- | ---------- | ------- |
| POST | /api/v1/rides/ | Body: Ride data Returns: Created Ride object | Driver created a new ride listing |
| GET | /api/v1/rides/{id} | Returns: Ride object with nested stop list | Get a specific ride listing with its associated stops and passengers |
| GET | /api/v1/rides/{driverRoll}/active | Returns: List of Ride objects | Get active ride by driver | 


### Search & Booking (Demand)
| Method | Endpoint | Payload | Description |
| ---------- | ---------- | ---------- | ------- |
| GET | /api/v1/rides/search | Query: departure time, pickup location, drop off location Returns: filtered and safety prioritized list of Ride objects | Passenger searches for available rides |
| POST | /api/v1/rides/{id}/request | Body: Ride request data (passenger id, pickup stop, drop off stop, quoted contribution) | Passenger requests a seat segment on a ride, along with stops |
| POST | /api/v1/rides/requests/{requestId}/accept | Path: request ID, returns updated ride request status (ACCEPTED) | Driver accepts the pending request | 


### Monetization and Settlement 
| Method | Endpoint | Payload | Description |
| ---------- | ---------- | ---------- | ------- |
| POST | /api/v1/rides/requests/{requestID}/pay | Returns: updated ride request status | Passenger pays |
| POST | /api/v1/rides/{rideId}/complete | Returns: updated ride status | Driver marks the entire ride as complete |
| POST | /api/v1/rides/requests/{requestId}/settle | Returns: updated ride request status | Internal API to finalize payment | 

