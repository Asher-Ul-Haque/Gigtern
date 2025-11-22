# Gigtern Domain Model

This document outlines the core entities (or domains) required for the Gigtern platform, their key attributes, and the relationships between them.

## Core Entities

### 1. User

Represents anyone who registers on the platform (either a Student or an Employer).
| Attribute | Data Type | Description |
| :--- | :--- | :--- |
| userId | UUID (Primary Key) | Unique identifier for the user. |
| email | String | User's login email. |
| passwordHash | String | Hashed password for security. |
| role | Enum (STUDENT, EMPLOYER) | Defines the user type. |
| isVerified | Boolean | True after successful KYC/Student ID verification. |
| createdAt | DateTime | Timestamp of registration. |

### 2. Student (Extends User)

Specific details for students seeking gigs.
| Attribute | Data Type | Description |
| :--- | :--- | :--- |
| studentId | UUID (Primary Key) | Unique identifier. |
| userId | UUID (Foreign Key) | Link to the base User entity. |
| universityName | String | Name of the student's academic institution. |
| studentIdProofUrl | String | URL to the uploaded student ID document. |
| skills | Array of Strings | List of technical/soft skills (e.g., Java, UX Design). |
| preferences | JSON / Map | Preferences for gig type, location, remote/in-person. |
| kycStatus | Enum (PENDING, VERIFIED) | Status of Aadhar/KYC verification. |

### 3. Employer (Extends User)

Specific details for employers posting gigs.
| Attribute | Data Type | Description |
| :--- | :--- | :--- |
| employerId | UUID (Primary Key) | Unique identifier. |
| userId | UUID (Foreign Key) | Link to the base User entity. |
| companyName | String | Name of the organization. |
| aadharKycUrl | String | URL to the uploaded Aadhar/KYC document. |
| contactPerson | String | Primary contact name. |

### 4. Gig (Job Posting)

Represents a short-term, flexible work opportunity.
| Attribute | Data Type | Description |
| :--- | :--- | :--- |
| gigId | UUID (Primary Key) | Unique identifier for the gig. |
| employerId | UUID (Foreign Key) | Employer who posted the gig. |
| title | String | Short, descriptive title. |
| description | Text | Detailed description of the task and deliverables. |
| skillsRequired | Array of Strings | Skills needed to perform the gig. |
| payRate | Decimal | The agreed-upon payment amount (fixed or hourly rate). |
| paymentType | Enum (FIXED, HOURLY) | How the payment is structured. |
| durationEstimate | Integer | Estimated hours or days for the gig. |
| status | Enum (OPEN, MATCHED, IN_PROGRESS, COMPLETED, CLOSED) | Current state of the gig. |
| postedDate | DateTime | When the gig was posted. |
| quickBookEnabled | Boolean | If quick 15-minute booking algorithm is enabled. |

### 5. Application (or Match)

Tracks a student's interest in a Gig and the matchmaking process.
| Attribute | Data Type | Description |
| :--- | :--- | :--- |
| applicationId | UUID (Primary Key) | Unique identifier. |
| gigId | UUID (Foreign Key) | The gig being applied for. |
| studentId | UUID (Foreign Key) | The student making the application. |
| status | Enum (APPLIED, ACCEPTED, REJECTED, BOOKED) | Current status of the application. |
| appliedDate | DateTime | Timestamp of application. |
| quickBookMatchScore | Decimal | Score assigned by the matchmaking algorithm. |

### 6. Review

Used for the Two-Way Rating system.
| Attribute | Data Type | Description |
| :--- | :--- | :--- |
| reviewId | UUID (Primary Key) | Unique identifier. |
| gigId | UUID (Foreign Key) | The gig the review is related to. |
| reviewerId | UUID (Foreign Key) | The ID of the User who wrote the review (Student or Employer). |
| revieweeId | UUID (Foreign Key) | The ID of the User being reviewed. |
| rating | Integer (1-5) | The star rating. |
| comment | Text | Detailed feedback. |
| reviewDate | DateTime | When the review was submitted. |

### 7. Transaction

Records financial activity, especially commission fees.
| Attribute | Data Type | Description |
| :--- | :--- | :--- |
| transactionId | UUID (Primary Key) | Unique identifier. |
| gigId | UUID (Foreign Key) | Related gig. |
| payerId | UUID (Foreign Key) | User who made the payment. |
| receiverId | UUID (Foreign Key) | User who received the payment (before platform commission). |
| amountTotal | Decimal | Total amount of the transaction. |
| commissionFee | Decimal | The 50% fixed percentage taken by Gigtern. |
| netPayout | Decimal | Amount student receives or employer pays (less fees). |
| status | Enum (PENDING, PAID, FAILED) | State of the payment. |
| transactionDate | DateTime | Timestamp of the transaction. |

---

## Relationships (Simplified)

* User 1:1 Student / Employer (A User is either one or the other)
* Employer 1:M Gig (One employer can post multiple gigs)
* Gig M:M Student (Many students can apply for a gig, a student can apply for many gigs, mediated by the Application entity)
* Gig 1:M Review (A gig completion results in two reviews)
* Gig 1:1 Transaction (A completed gig results in one financial transaction)
