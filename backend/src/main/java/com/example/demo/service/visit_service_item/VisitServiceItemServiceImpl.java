package com.example.demo.service.visit_service_item;

import com.example.demo.model.MedicalService;
import com.example.demo.model.Visit;
import com.example.demo.model.VisitServiceItem;
import com.example.demo.repository.MedicalServiceRepository;
import com.example.demo.repository.VisitRepository;
import com.example.demo.repository.VisitServiceItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VisitServiceItemServiceImpl implements VisitServiceItemService {

    private final VisitRepository visitRepository;
    private final MedicalServiceRepository medicalServiceRepository;
    private final VisitServiceItemRepository itemRepository;

    @Override
    @Transactional
    public void addItemToVisit(UUID visitId, UUID medicalServiceId, Integer quantity) {

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));

        MedicalService service = medicalServiceRepository.findById(medicalServiceId)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));

        if (!service.getDoctor().getId().equals(visit.getDoctor().getId())) {
            throw new IllegalArgumentException("Service does not belong to visit doctor");
        }

        VisitServiceItem item = VisitServiceItem.builder()
                .visit(visit)
                .service(service)
                .serviceName(service.getName())
                .priceAtMomentOfPurchase(service.getPrice())
                .quantity(quantity)
                .build();

        VisitServiceItem saved = itemRepository.save(item);
        if (visit.getServices() == null) {
            visit.setServices(new ArrayList<>());
        }
        visit.getServices().add(saved);
    }

    @Override
    @Transactional
    public void updateItemQuantity(UUID itemId, Integer quantity) {
        VisitServiceItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Visit service item not found"));

        if (quantity == null || quantity <= 0) {
            itemRepository.delete(item);
            return;
        }
        item.setQuantity(quantity);
        itemRepository.save(item);
    }

    @Override
    @Transactional
    public void removeItem(UUID itemId) {
        VisitServiceItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Visit service item not found"));

        itemRepository.delete(item);
    }

    @Override
    public List<VisitServiceItem> getItemsForVisit(UUID visitId) {
        return itemRepository.findByVisitId(visitId);
    }

    @Override
    @Transactional
    public void clearItemsForVisit(UUID visitId) {
        itemRepository.deleteByVisitId(visitId);
    }
}