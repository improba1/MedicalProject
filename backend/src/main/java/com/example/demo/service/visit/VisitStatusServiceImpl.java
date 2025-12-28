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

        // 🔥 Перевірки ролей
        if (role == Role.PATIENT && visit.getAppointmentTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Patient cannot cancel past visits");
        }

        // 🔥 Перевірки переходів
        if (visit.getStatus() == VisitStatus.COMPLETED || visit.getStatus() == VisitStatus.PAID) {
            throw new IllegalStateException("Cannot cancel completed or paid visits");
        }

        // 🔥 Побічні ефекти
        slotService.free(visit.getDoctor().getId(), visit.getAppointmentTime());

        visit.setStatus(VisitStatus.CANCELED);
        return visit;
    }

    @Override
    public Visit complete(Visit visit, Role role) {
        if (role != Role.DOCTOR && role != Role.ADMIN) {
            throw new IllegalStateException("Only doctor or admin can complete visits");
        }

        if (visit.getStatus() != VisitStatus.SCHEDULED &&
                visit.getStatus() != VisitStatus.RESCHEDULED) {
            throw new IllegalStateException("Only scheduled visits can be completed");
        }

        visit.setStatus(VisitStatus.COMPLETED);
        return visit;
    }

    @Override
    public Visit pay(Visit visit, Role role) {
        if (role != Role.ADMIN) {
            throw new IllegalStateException("Only admin can mark visit as paid");
        }

        if (visit.getStatus() != VisitStatus.COMPLETED) {
            throw new IllegalStateException("Only completed visits can be paid");
        }

        visit.setStatus(VisitStatus.PAID);
        return visit;
    }

    @Override
    public Visit reschedule(Visit visit, LocalDateTime newTime, Role role) {

        if (role == Role.PATIENT && visit.getAppointmentTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Patient cannot reschedule past visits");
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