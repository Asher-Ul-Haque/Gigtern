# Test Run

## Register : 
POST /api/v1/auth/register
INPUT:
{
    "email" : "devaj23190@iiitd.ac.in",
    "password" : "devajNirala1234",
    "name" : "DevajNirala",
    "role" : "STUDENT"
}
OUTPUT:
{
    "jwt": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlNUVURFTlQiLCJpYXQiOjE3NjM4NjIxOTUsImV4cCI6MTc2Mzk0ODU5NX0.ZntlwxBERzcUEOYSIlV29SL_JDzNi30oj1UN9VIAurAxFgp8I7HCJd3mksB8oZKfV_APu2dg5J6fRReGzw6diQ",
    "userId": 1,
    "role": "STUDENT",
    "message": "Registration successful"
}

## GET /api/v1/users/me 
INPUT: (JUST ATTACH JWT)
OUTPUT:
{
    "id": 1,
    "name": "DevajNirala",
    "email": "devaj23190@iiitd.ac.in",
    "oneLiner": null,
    "role": "STUDENT",
    "verificationStatus": "PENDING",
    "createdAt": "2025-11-23T07:13:15.162604",
    "universityName": null,
    "skills": [],
    "companyContact": null
}

## POST /api/v1/gigs
INPUT: (ATTACH JWT)
{
    "title" : "Ek JOb hai",
    "description" : "Description is required",
    "paymentType" : "FIXED",
    "payRate" : 3.5,
    "skillsRequired" : ["Java", "C"]
}

OUTPUT: 
{
    "id": 1,
    "employerId": 1,
    "title": "Ek JOb hai",
    "description": "Description is required",
    "paymentType": "FIXED",
    "payRate": 3.5,
    "status": "OPEN",
    "skillsRequired": [
        "Java",
        "C"
    ],
    "postedDate": "2025-11-23T07:30:53.490589325",
    "employerCompanyName": "DevajNirala",
    "durationEstimateHours": null
}

## GET /api/v1/gigs
INPUT (JWT ATTACHED ONLY)

OUTPUT:
[
    {
        "id": 1,
        "employerId": 1,
        "title": "Ek JOb hai",
        "description": "Description is required",
        "paymentType": "FIXED",
        "payRate": 3.5,
        "status": "OPEN",
        "skillsRequired": [
            "Java",
            "C"
        ],
        "postedDate": "2025-11-23T07:30:53.490589",
        "employerCompanyName": "DevajNirala",
        "durationEstimateHours": null
    }
]

## GET /api/v1/gigs/my
INPUT (JWT ATTACHED ONLY)

OUTPUT:
{
    "id": 1,
    "employerId": 1,
    "title": "Ek JOb hai",
    "description": "Description is required",
    "paymentType": "FIXED",
    "payRate": 3.5,
    "status": "OPEN",
    "skillsRequired": [
        "Java",
        "C"
    ],
    "postedDate": "2025-11-23T07:30:53.490589",
    "employerCompanyName": "DevajNirala",
    "durationEstimateHours": null
}
