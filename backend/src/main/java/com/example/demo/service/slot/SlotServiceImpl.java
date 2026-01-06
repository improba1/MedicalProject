package com.example.demo.service.slot;

import com.example.demo.exceptions.IllegalStateFoundException;
import com.example.demo.model.DoctorAvailability;
import com.example.demo.repository.DoctorAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class SlotServiceImpl implements SlotService {

    private final DoctorAvailabilityRepository availabilityRepository;

    private void ensureFuture(LocalDateTime time) {
        if (time.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot operate on past slots");
        }
    }

    // ==========================
    // 🔥 OCCUPY SLOT
    // ==========================

    @Override
    @Transactional
    public void occupy(UUID doctorId, LocalDateTime time) {

        ensureFuture(time);

        DoctorAvailability slot = availabilityRepository
                .findByDoctorIdAndAvailableTimeAndIsActiveTrue(doctorId, time)
                .orElseThrow(() -> new IllegalStateException("Slot is not available"));

        slot.setActive(false);
        availabilityRepository.save(slot);
    }

    // ==========================
    // 🔥 FREE SLOT
    // ==========================

    @Override
    @Transactional
    public void free(UUID doctorId, LocalDateTime time) {

        DoctorAvailability slot = availabilityRepository
                .findByDoctorIdAndAvailableTime(doctorId, time)
                .orElse(null);

        if (slot == null) {
            return;
        }

        // Якщо час у минулому → не активуємо
        if (slot.getAvailableTime().isBefore(LocalDateTime.now())) {
            return;
        }

        slot.setActive(true);
        availabilityRepository.save(slot);
    }

    // ==========================
    // 🔥 RESCHEDULE SLOT
    // ==========================

    @Override
    @Transactional
    public void reschedule(UUID doctorId, LocalDateTime oldTime, LocalDateTime newTime) {

        ensureFuture(newTime);

        // 1️⃣ Звільняємо старий слот
        free(doctorId, oldTime);

        // 2️⃣ Перевіряємо, що новий слот існує
        DoctorAvailability newSlot = availabilityRepository
                .findByDoctorIdAndAvailableTimeAndIsActiveTrue(doctorId, newTime)
                .orElseThrow(() -> new IllegalStateFoundException(
                        "Cannot reschedule: new slot is not available"
                ));

        // 3️⃣ Займаємо новий слот
        newSlot.setActive(false);
        availabilityRepository.save(newSlot);
    }
}