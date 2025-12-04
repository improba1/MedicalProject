package com.example.demo.repos;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID>{
    Optional<Payment> findByStripeSessionId(String sessionId);
}
