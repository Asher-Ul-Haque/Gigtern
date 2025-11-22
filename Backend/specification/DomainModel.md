# Domain Entities and Attributes

## User 
This entity stores all required verification and reputation data 
| Attribute | Data Type | Purpose |
| --------------- | --------------- | --------------- |
| ID | Int | Primary Key: The unique IIITD Roll no |
| Name | String | Full name for verification |
| Phone | Int | Phone number for driver passenger contact |
| Email | String | Email for auditing if needed |
| Gender | Enumeration (MALE, FEMALE) | crucial for safety prioritization algorithm |
| Role | Enumeration (DRIVER, PASSENGER, BOTH) | Defines the user's primary function |
| Reputation score | Float | Aggregate from 1.0 to 5.0 for easy display |
| Cancellation rate | Float | Metric for reliability: Both driver and passenger |
| On time rate | Float | Specific metric for punctuality: Both driver and passenger |


## Ride 
This has a one-to-many relationship with stop and represents a driver's full journey commitment
| Attribute | Data Type | Purpose |
| --------------- | --------------- | --------------- |
| Ride ID | Long | Primary Key: unique ID for a ride listing |
| Driver ID | Int | Driver's roll no |
| Departure Time | Local Date Time | Specific time the journey starts | 
| Origin Location | String | The fixed starting point |
| Max Seats | Byte | Total no. of seats available on the car |
| Booked Seats | Byte | Count of seats currently reserved across all requests |
| Status | Enumeration (ACTIVE, COMPLETED, CANCELLED) | Manages the ride lifecycle | 


## Stop 
This defines the driver's planned route, including the final destination. A single Ride can have many Stops
| Attribute | Data Type | Purpose |
| --------------- | --------------- | --------------- |
| Stop ID | Long | Primary Key: unique ID for a stop listing |
| Ride ID | Long | Foreign key to Ride, linking a stop to a ride | 
| Location | String | The specific location of the stop / waypoint | 
| Sequence | Byte | Order of the stop on the route (1st, 2nd, 3rd) |
| Estimated Arrival Time | Local Date Time | crucial for passenger scheduling |


## Ride Request 
Passengers booking 

| Attribute | Data Type | Purpose |
| --------------- | --------------- | --------------- |
| Request ID | Long | Primary Key: unique ID for a booking listing |
| Ride ID | Long | Foreign Key to ride, linking the book to a ride |
| Pickup Stop ID | Long | Foreign key to stop, where the passenger is picked up |
| Pickup Drop ID | Long | Foreign key to stop, where the passenger is dropped of |
| Quoted Contribution | Float | Monetization |
| Status | Enumeration (PENDING, ACCEPTED, REJECTED) | Driver Acceptance Status |
| Payment Status | Enumeration | Escrow | 


