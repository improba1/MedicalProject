package com.example.demo.service.visit;

import com.example.demo.enums.Role;
import com.example.demo.model.Visit;

import java.time.LocalDateTime;

public interface VisitStatusService {
    Visit cancel(Visit visit, Role role);
    Visit complete(Visit visit, Role role);
    Visit pay(Visit visit, Role role);
    Visit reschedule(Visit visit, LocalDateTime newTime, Role role);
}