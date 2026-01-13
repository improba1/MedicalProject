package com.example.demo.mapper;

import com.example.demo.dto.request.visit_service_item.VisitServiceItemCreateRequest;
import com.example.demo.dto.request.visit_service_item.VisitServiceItemUpdateRequest;
import com.example.demo.dto.response.VisitServiceItemResponse;
import com.example.demo.model.MedicalService;
import com.example.demo.model.Visit;
import com.example.demo.model.VisitServiceItem;
import org.springframework.stereotype.Component;

@Component
public class VisitServiceItemMapper {

    public VisitServiceItem toEntity(VisitServiceItemCreateRequest req, Visit visit, MedicalService service) {
        return VisitServiceItem.builder()
                .visit(visit)
                .service(service)
                .priceAtMomentOfPurchase(service.getPrice())
                .quantity(req.getQuantity())
                .build();
    }

    public void updateEntity(VisitServiceItem item, VisitServiceItemUpdateRequest req) {
        if (req.getQuantity() != null) {
            item.setQuantity(req.getQuantity());
        }
    }

    public VisitServiceItemResponse toResponse(VisitServiceItem item) {
        VisitServiceItemResponse dto = new VisitServiceItemResponse();
        dto.setId(item.getId());
        dto.setVisitId(item.getVisit().getId());
        dto.setMedicalServiceId(item.getService().getId());
        dto.setServiceName(item.getService().getName());
        dto.setPriceAtMomentOfPurchase(item.getPriceAtMomentOfPurchase());
        dto.setQuantity(item.getQuantity());
        return dto;
    }
}
