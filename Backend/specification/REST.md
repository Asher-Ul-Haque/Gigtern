# Gigtern REST API Endpoints (v1)

This list outlines the primary RESTful endpoints for the Gigtern backend, categorized by the main domain resource they manage.


## Authorization / User 
| Endpoint               | HTTP Method | Description | Roles Allowed |
|------------------------| --------------- | --------------- | --------------- | 
| /api/v1/auth/register  | POST | Register a new user (Student or Employer) | Public | 
| /api/v1/auth/login     | POST | Authenticate and return an access token. | Public | 
| /api/v1/users/{userId} | GET | Retrieve user profile (general info) | Authenticated | 
| /api/v1/users/me       | GET | Retrieve the authenticated user's full profile | Authenticated | 

## Student Profile
| Endpoint | HTTP Method | Description | Roles Allowed |
| --------------- | --------------- | --------------- | --------------- | 
| /api/v1/students/me | PUT | Update student details (skills, university, preferences). | Student |
| /api/v1/students/me/verification | POST | Upload student ID for verification. | Student |
| /api/v1/students/{studentId} | GET | Get a student's public profile and reviews. | Authenticated |

## Employer Profile
| Endpoint | HTTP Method | Description | Roles Allowed |
| --------------- | --------------- | --------------- | --------------- | 
| /api/v1/employers/me | PUT | Update employer details (company name, contact). | Employer |
| /api/v1/employers/me/verification | POST | Upload Aadhar/KYC for verification. | Employer |
| /api/v1/employers/{employerId} | GET | Get an employer's public profile and reviews. | Authenticated |


## Gigs
| Endpoint | HTTP Method | Description | Roles Allowed |
| -------- | ----------- | ----------- | ------------- |
| /api/v1/gigs | POST | Create a new gig posting. | Employer |
| /api/v1/gigs | GET | List all available gigs (for students). Supports filtering and sorting. | Student |
| /api/v1/gigs/my | GET | List gigs posted by the authenticated employer. | Employer |
| /api/v1/gigs/{gigId} | GET | Retrieve a specific gig by ID. | Authenticated |
| /api/v1/gigs/{gigId} | PUT | Update an existing gig posting. | Employer |
| /api/v1/gigs/{gigId}/status | PATCH | Update the gig status (e.g., from OPEN to CLOSED). | Employer |
| /api/v1/gigs/search | GET | Specialized search endpoint (for personalized matchmaking). | Student |


## Application Matches
| Endpoint | HTTP Method | Description | Roles Allowed |
| -------- | ----------- | ----------- | ------------- |
| /api/v1/gigs/{gigId}/apply | POST | Student applies to a specific gig. | Student |
| /api/v1/applications/my | GET | List applications made by the authenticated student. | Student |
| /api/v1/gigs/{gigId}/applications | GET | List all applications for a specific gig (includes QuickBook scores). | Employer |
| /api/v1/applications/{applicationId}/accept | PATCH | Employer accepts an application, setting the gig status to MATCHED/BOOKED. | Employer |
| /api/v1/applications/{applicationId}/reject | PATCH | Employer rejects an application. | Employer |


## Rehire
| Endpoint | HTTP Method | Description | Roles Allowed |
| -------- | ----------- | ----------- | ------------- |
| /api/v1/employers/me/favorites/{studentId} | POST | Employer adds a student to a 'rehire' list. | Employer |
| /api/v1/employers/me/favorites | GET | List of all favorite students for easy rehire. | Employer |


## Reviews / Ratings
| Endpoint | HTTP Method | Description | Roles Allowed |
| -------- | ----------- | ----------- | ------------- |
| /api/v1/gigs/{gigId}/review | POST | Submit a two-way review (both student and employer use this). | Authenticated |
| /api/v1/users/{userId}/reviews | GET | Get all reviews received by a user. | Authenticated |


## Transactions
| Endpoint | HTTP Method | Description | Roles Allowed |
| -------- | ----------- | ----------- | ------------- |
| /api/v1/transactions/gig/{gigId} | POST | Initiate payment and process commission fees upon gig completion. | Employer |
| /api/v1/transactions/my | GET | Get the authenticated user's transaction history. | Authenticated |


## Messaging (Secure Chats)
| Endpoint | HTTP Method | Description | Roles Allowed |
| -------- | ----------- | ----------- | ------------- |
| /api/v1/chats | GET | List all chat threads for the user. | Authenticated |
| /api/v1/chats/{chatId}/messages | GET | Retrieve message history for a thread. | Authenticated |
| /api/v1/chats/{chatId}/messages | POST | Send a new message. (Note: A dedicated WebSocket service is better for real-time chat, but REST can initiate the chat.) | Authenticated |
