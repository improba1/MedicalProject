package com.example.demo.service.slot;

import com.example.demo.model.DoctorAvailability;
import com.example.demo.repository.DoctorAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SlotServiceImpl implements SlotService {

    private final DoctorAvailabilityRepository availabilityRepository;

    @Override
    public void occupy(UUID doctorId, LocalDateTime time) {
        DoctorAvailability slot = availabilityRepository
                .findByDoctorIdAndAvailableTimeAndIsActiveTrue(doctorId, time)
                .orElseThrow(() -> new IllegalStateException("Slot is not available"));

        slot.setActive(false);
        availabilityRepository.save(slot);
    }

    @Override
    public void free(UUID doctorId, LocalDateTime time) {
        availabilityRepository.findByDoctorIdAndAvailableTime(doctorId, time)
                .ifPresent(slot -> {
                    slot.setActive(true);
                    availabilityRepository.save(slot);
                });
    }

    @Override
    public void reschedule(UUID doctorId, LocalDateTime oldTime, LocalDateTime newTime) {
        free(doctorId, oldTime);
        occupy(doctorId, newTime);
    }
}