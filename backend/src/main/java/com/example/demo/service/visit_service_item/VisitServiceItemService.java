package com.example.demo.service.visit_service_item;

import com.example.demo.model.VisitServiceItem;

import java.util.List;
import java.util.UUID;

public interface VisitServiceItemService {

    void addItemToVisit(UUID visitId, UUID medicalServiceId, Integer quantity);

    void updateItemQuantity(UUID itemId, Integer quantity);

    void removeItem(UUID itemId);

    List<VisitServiceItem> getItemsForVisit(UUID visitId);

    void clearItemsForVisit(UUID visitId);
}