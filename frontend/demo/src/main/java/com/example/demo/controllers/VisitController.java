package com.example.demo.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.dto.BookRequest;
import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.VisitByDate;
import com.example.demo.dto.VisitByDoctorDetailsRequest;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;
import com.example.demo.model.Payment;
import com.example.demo.model.Raport;
import com.example.demo.model.Specialization;
import com.example.demo.model.Visit;
import com.example.demo.repos.PatientRepository;
import com.example.demo.repos.RaportRepository;
import com.example.demo.repos.VisitRepository;
import com.example.demo.repos.VisitStatusRepository;
import com.example.demo.services.impl.PaymentService;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/patient/visit")
public class VisitController {

    private final PatientRepository patientRepository;
    private final RaportRepository raportRepository;
    private final VisitStatusRepository visitStatusRepository;
    private final VisitRepository visitRepository;
    private final PaymentService paymentService;
    
    @GetMapping("/dates")
    private ResponseEntity<List<LocalDateTime>> visit_by_doctor_name(@RequestBody VisitByDoctorDetailsRequest request){

        //find by specialization, last_name or date

        //choose last_name
        //show terms
        //choose term
        //pay
        //success
        short years = 5;
        String doc_last_name = request.getDoctor_last_name();
        Doctor doctor = Doctor.builder()
                .name("Emma")
                .last_name("Davis")
                .email("emma.davis@example.com")
                .login("emma.davis")
                .password("123")
                .phone("+230631234005")
                .specialization(new Specialization(UUID.fromString("531ad003-6ede-49b4-bbea-b3cdcee6a3cc"), "Pediatrics", true))
                .experience_years(years)
                .qualification("MD")
                .role("DOCTOR")
                .active(true)
                .date_of_registration(LocalDateTime.of(2025, 10, 10, 11, 43))
                .build();
        // List<LocalDateTime> slots = scheduleRepo.findByDoctorIdAndIsAvailableTrue(doctor.getId());
        List<LocalDateTime> slots = List.of(
            LocalDateTime.of(2025, 10, 21, 10, 0),
            LocalDateTime.of(2025, 10, 21, 10, 30),
            LocalDateTime.of(2025, 10, 21, 11, 0),
            LocalDateTime.of(2025, 10, 21, 11, 30)
        );
        return ResponseEntity.ok(slots);

        //choose date / specialization
        //show doctors
        //choose doctor
        //pay
        //success

    }

    @GetMapping("/doctors")
    private ResponseEntity<List<Doctor>> visit_by_doctor_date(@RequestBody VisitByDate request){
        LocalDateTime date = request.getDate();
        short years = 5;
        // List<Doctor> doctors = schedulesRepo.findByDateAndIsAvailable(date);
        Doctor doctor1 = Doctor.builder()
            .name("Emma")
            .last_name("Davis")
            .email("emma.davis@example.com")
            .login("emma.davis")
            .password("123")
            .phone("+230631234005")
            .specialization(new Specialization(UUID.fromString("531ad003-6ede-49b4-bbea-b3cdcee6a3cc"), "Pediatrics", true))
            .experience_years(years)
            .qualification("MD")
            .role("DOCTOR")
            .active(true)
            .date_of_registration(LocalDateTime.of(2025, 10, 10, 11, 43))
            .build();

        Doctor doctor2 = Doctor.builder()
            .name("Liam")
            .last_name("Johnson")
            .email("liam.johnson@example.com")
            .login("liam.johnson")
            .password("123")
            .phone("+230631234006")
            .specialization(new Specialization(UUID.fromString("641ad113-7fde-42b1-acba-b4cdcee7f4dd"), "Cardiology", true))
            .experience_years(years)
            .qualification("PhD")
            .role("DOCTOR")
            .active(true)
            .date_of_registration(LocalDateTime.of(2024, 5, 15, 9, 20))
            .build();

        Doctor doctor3 = Doctor.builder()
            .name("Olivia")
            .last_name("Martinez")
            .email("olivia.martinez@example.com")
            .login("olivia.martinez")
            .password("123")
            .phone("+230631234007")
            .specialization(new Specialization(UUID.fromString("751ad223-9ede-12c4-babc-c5cdcee8b5ee"), "Neurology", true))
            .experience_years(years)
            .qualification("MD")
            .role("DOCTOR")
            .active(true)
            .date_of_registration(LocalDateTime.of(2023, 8, 2, 14, 35))
            .build();

        Doctor doctor4 = Doctor.builder()
            .name("Noah")
            .last_name("Brown")
            .email("noah.brown@example.com")
            .login("noah.brown")
            .password("123")
            .phone("+230631234008")
            .specialization(new Specialization(UUID.fromString("861ad333-3fde-33d4-dcba-d6cdcee9c6ff"), "Dermatology", true))
            .experience_years(years)
            .qualification("MD")
            .role("DOCTOR")
            .active(true)
            .date_of_registration(LocalDateTime.of(2024, 11, 22, 10, 15))
            .build();

        Doctor doctor5 = Doctor.builder()
            .name("Ava")
            .last_name("Wilson")
            .email("ava.wilson@example.com")
            .login("ava.wilson")
            .password("123")
            .phone("+230631234009")
            .specialization(new Specialization(UUID.fromString("971ad443-4fde-44e4-ecba-e7cdceea78aa"), "Orthopedics", true))
            .experience_years(years)
            .qualification("Professor, MD")
            .role("DOCTOR")
            .active(true)
            .date_of_registration(LocalDateTime.of(2022, 3, 30, 16, 50))
            .build();
        List<Doctor> doctors = List.of(
            doctor1, doctor2, doctor3, doctor4, doctor5
        );
        return ResponseEntity.ok(doctors);
    }

    @PostMapping("/book")
    private ResponseEntity<Visit> book_visit(@RequestBody BookRequest request){
        UUID patient_id = request.getPatient_id();

        //if (userRepo.findById(patient_id).exist())
        //if (scheduleRepo.findById(schedule_id).exist())

        //Doctor doctor = docRepo.findById(doctor_id);

        short years = 5;
        Doctor doctor = Doctor.builder()
            .id(UUID.fromString("02a5fb3c-6f78-4645-b20f-29a577eab32e"))
            .name("Emma")
            .last_name("Davis")
            .email("emma.davis@example.com")
            .login("emma.davis")
            .password("$2a$06$Jr3N3i/qr11wz/PbQkMTUefwSB2EGF4u9qa8N6WeIoIXZgZ6e9uB6")
            .phone("+380631234005")
            .specialization(new Specialization(UUID.fromString("531ad003-6ede-49b4-bbea-b3cdcee6a3cc"), "Pediatrics", true))
            .experience_years(years)
            .qualification("MD")
            .role("DOCTOR")
            .active(true)
            .date_of_registration(LocalDateTime.of(2025, 10, 16, 21, 20, 26))
            .build();

        Optional<Patient> patient = patientRepository.findById(patient_id);

        if (patient.isPresent()){
            Raport raport = Raport.builder()
                .doctor(doctor)
                .patient(patient.get())
                .status("PENDING")
                .build();
            
            raportRepository.save(raport);

            Visit visit = Visit.builder()
                .appointment_time(LocalDateTime.now())
                .doctor(doctor)
                .patient(patient.get())
                .raport(raport)
                .visit_status(visitStatusRepository.findByStatus("PENDING").get())
                .visit_time(LocalDateTime.now())
                .build();
            visitRepository.save(visit);
            pay_for_booking(new PaymentRequest(visit.getId(), patient_id));
            return ResponseEntity.status(HttpStatus.OK).body(visit);
            
        }else{
            return ResponseEntity.badRequest().build();
        }
        
    }

    @PostMapping("/pay")
    private ResponseEntity<String> pay_for_booking(@RequestBody PaymentRequest request){
        UUID visit_id = request.getVisit_id();
        Visit visit = visitRepository.findById(visit_id)
            .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Visit not found"));

        Payment payment = visit.getPayment();
        if(payment != null && payment.getStatus() == PaymentStatus.PAID){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This visit has already been paid and processed.");
        }
        String checkoutUrl = paymentService.createCheckoutSession(visit_id);
        return ResponseEntity.status(HttpStatus.CREATED).body(checkoutUrl);
    }
}
