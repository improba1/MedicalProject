package com.example.demo.mapper;

import com.example.demo.dto.request.raport.RaportCreateRequest;
import com.example.demo.dto.request.raport.RaportUpdateRequest;
import com.example.demo.dto.response.RaportResponse;
import com.example.demo.model.Raport;
import com.example.demo.model.Visit;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class RaportMapper {

    public RaportResponse toResponse(Raport raport) {
        Visit visit = raport.getVisit();

        return RaportResponse.builder()
                .id(raport.getId())
                .visitId(visit.getId())
                .disease(raport.getDisease())
                .symptoms(raport.getSymptoms())
                .treatmentPlan(raport.getTreatmentPlan())
                .doctorNotes(raport.getDoctorNotes())
                .totalPrice(raport.getTotalPrice())
                .servicesSnapshot(raport.getServicesSnapshot())
                .paymentReceipt(raport.getPaymentReceipt())
                .createdAt(raport.getCreatedAt())
                .build();
    }

    public List<RaportResponse> toResponseList(List<Raport> raports) {
        return raports.stream()
                .map(this::toResponse)
                .toList();
    }

    public Raport toEntity(RaportCreateRequest request, Visit visit, String servicesSnapshot, String paymentReceipt) {

        return Raport.builder()
                .visit(visit)
                .disease(request.getDisease())
                .symptoms(visit.getPatientSymptoms())
                .treatmentPlan(request.getTreatmentPlan())
                .doctorNotes(request.getDoctorNotes())
                .totalPrice(visit.getTotalPrice())
                .servicesSnapshot(servicesSnapshot)
                .paymentReceipt(paymentReceipt)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public Raport updateEntity(Raport raport, RaportUpdateRequest request) {

        if (request.getDisease() != null)
            raport.setDisease(request.getDisease());

        if (request.getTreatmentPlan() != null)
            raport.setTreatmentPlan(request.getTreatmentPlan());

        if (request.getDoctorNotes() != null)
            raport.setDoctorNotes(request.getDoctorNotes());

        return raport;
    }
}