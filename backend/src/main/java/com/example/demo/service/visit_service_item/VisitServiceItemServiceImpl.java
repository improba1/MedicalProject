package com.example.demo.service.visit_service_item;

import com.example.demo.model.MedicalService;
import com.example.demo.model.Visit;
import com.example.demo.model.VisitServiceItem;
import com.example.demo.repository.MedicalServiceRepository;
import com.example.demo.repository.VisitRepository;
import com.example.demo.repository.VisitServiceItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));

        MedicalService service = medicalServiceRepository.findById(medicalServiceId)
                .orElseThrow(() -> new EntityNotFoundException("Medical service not found"));

        Optional<VisitServiceItem> existing = itemRepository.findByVisitIdAndServiceId(visitId, medicalServiceId);
        if (existing.isPresent()) {
            VisitServiceItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            VisitServiceItem saved = itemRepository.save(item);
            if (visit.getServices() != null && !visit.getServices().contains(saved)) {
                visit.getServices().removeIf(i -> i.getId().equals(saved.getId()));
                visit.getServices().add(saved);
            }
            return;
        }

        VisitServiceItem item = VisitServiceItem.builder()
                .visit(visit)
                .service(service)
                .serviceName(service.getName())
                .priceAtMomentOfPurchase(service.getPrice())
                .quantity(quantity)
                .build();

        VisitServiceItem saved = itemRepository.save(item);

        if (visit.getServices() != null) {
            visit.getServices().add(saved);
        }
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