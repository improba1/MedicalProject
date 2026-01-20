# Medical Appointment & Management System

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![React](https://img.shields.io/badge/React-18-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

## ❖ Overview

This full-stack web application digitizes the clinic experience, creating a seamless bridge between patients, doctors, and administrators. It serves as a unified platform for scheduling appointments, managing medical records, and overseeing clinic staff.

The system replaces manual booking with a real-time digital scheduler, ensuring that patients can access healthcare services instantly while doctors can focus on patient care rather than administrative tasks.

## ⚡ Core Features

### For Patients
* **Smart Search:** Filter specialists by name, medical field, or rating.
* **Booking System:** View real-time slot availability and book appointments instantly.
* **Medical History:** Access past visit details, conclusions, and payment history.
* **Secure Payments:** Integrated flow for paying for medical services.

### For Doctors
* **Workspace:** A dedicated dashboard to view daily schedules and upcoming visits.
* **Slot Management:** Configure working hours and available time slots.
* **Reporting:** Create, edit, and save medical conclusions and diagnoses.
* **Profile Customization:** Update professional details and profile photos.

### For Administrators
* **Staff Control:** Onboard new doctors and manage existing profiles.
* **User Oversight:** Monitor patient and doctor activities.
* **System Health:** Logs and status monitoring.

## 🛠 Technology Stack

The project relies on a modern, scalable architecture containerized via Docker.

| Area | Technology |
| :--- | :--- |
| **Backend** | Java, Spring Boot 3, Spring Security (JWT), Hibernate |
| **Frontend** | React.js, React Router v6, Axios, CSS Modules |
| **Database** | PostgreSQL |
| **DevOps** | Docker, Maven, npm |

## ➤ Getting Started

Follow these steps to set up the project locally.

### 1. Clone the repository
```bash
git clone [https://github.com/your-username/medical-project.git](https://github.com/your-username/medical-project.git)
```
2. Backend Configuration
Navigate to the backend folder and configure your database connection in application.properties:

Properties

spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=postgres
spring.datasource.password=password
Run the server:

```bash
mvn spring-boot:run
```

3. Frontend Setup
Navigate to the frontend folder, install dependencies, and start the client:

```bash
npm install
npm start
```

🔐 API Reference
Here are the primary endpoints used in the application:

POST /api/v1/auth/authenticate — User login and token generation.

POST /api/v1/patient/me/visits/create — Booking logic.

GET /api/v1/patient/medical-services/search — Fetch services for price calculation.

POST /api/v1/admin/doctors/create — Register new staff (Multipart support).
