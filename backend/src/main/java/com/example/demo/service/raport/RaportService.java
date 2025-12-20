package com.example.demo.service.raport;

import com.example.demo.model.Raport;

import java.util.List;
import java.util.UUID;

public interface RaportService {

    Raport getById(UUID id);
    List<Raport> getAll();
    Raport create(Raport raport);
    Raport update(UUID id, Raport raport);
    void delete(UUID id);
    List<Raport> getByDoctorId(UUID doctorId);
    List<Raport> getByPatientId(UUID patientId);
    Raport getByVisitId(UUID visitId);
    Raport getRaportByVisitForUser(UUID visitId);
    List<Raport> getUserRaports();
    Raport getOwnRaportByVisit(UUID visitId);
    List<Raport> getDoctorRaports();
    List<Raport> getOwnRaportsByPatient(UUID patientId);
}