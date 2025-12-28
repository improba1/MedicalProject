package com.example.demo.service.slot;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SlotService {
    void occupy(UUID doctorId, LocalDateTime time);
    void free(UUID doctorId, LocalDateTime time);
    void reschedule(UUID doctorId, LocalDateTime oldTime, LocalDateTime newTime);
}