package com.example.demo.service.visit;

import com.example.demo.enums.Role;
import com.example.demo.enums.VisitStatus;
import com.example.demo.model.Visit;
import com.example.demo.service.slot.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VisitStatusServiceImpl implements VisitStatusService {

    private final SlotService slotService;

    @Override
    public Visit cancel(Visit visit, Role role) {

        // Пацієнт не може скасувати минулий візит
        if (role == Role.PATIENT && visit.getAppointmentTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Patient cannot cancel past visits");
        }

        // Не можна скасувати оплачений або завершений візит
        if (visit.getStatus() == VisitStatus.PAID || visit.getStatus() == VisitStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel paid or completed visits");
        }

        slotService.free(visit.getDoctor().getId(), visit.getAppointmentTime());
        visit.setStatus(VisitStatus.CANCELED);
        return visit;
    }

    @Override
    public Visit complete(Visit visit, Role role) {

        // Пацієнт не може завершити візит
        if (role == Role.PATIENT) {
            throw new IllegalStateException("Patient cannot complete visits");
        }

        // Завершити можна тільки оплачений візит
        if (visit.getStatus() != VisitStatus.PAID) {
            throw new IllegalStateException("Only paid visits can be completed");
        }

        visit.setStatus(VisitStatus.COMPLETED);
        return visit;
    }

    @Override
    public Visit pay(Visit visit, Role role) {

        // Пацієнт може платити сам
        if (role != Role.PATIENT && role != Role.ADMIN) {
            throw new IllegalStateException("Only patient or admin can pay for a visit");
        }

        // Платити можна тільки за SCHEDULED візит
        if (visit.getStatus() != VisitStatus.SCHEDULED) {
            throw new IllegalStateException("Only scheduled visits can be paid");
        }

        // Не можна платити після дати візиту
        if (visit.getAppointmentTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot pay for past visits");
        }

        visit.setStatus(VisitStatus.PAID);
        return visit;
    }

    @Override
    public Visit reschedule(Visit visit, LocalDateTime newTime, Role role) {

        // Пацієнт не може переносити минулий візит
        if (role == Role.PATIENT && visit.getAppointmentTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Patient cannot reschedule past visits");
        }

        // Не можна переносити оплачений візит
        if (visit.getStatus() == VisitStatus.PAID) {
            throw new IllegalStateException("Cannot reschedule a paid visit");
        }

        slotService.reschedule(
                visit.getDoctor().getId(),
                visit.getAppointmentTime(),
                newTime
        );

        visit.setAppointmentTime(newTime);
        visit.setStatus(VisitStatus.RESCHEDULED);

        return visit;
    }
}