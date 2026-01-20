package com.example.demo.controller.admin;

import com.example.demo.dto.request.visit_service_item.VisitServiceItemCreateRequest;
import com.example.demo.dto.request.visit_service_item.VisitServiceItemUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.VisitResponse;
import com.example.demo.dto.response.VisitServiceItemResponse;
import com.example.demo.mapper.VisitMapper;
import com.example.demo.mapper.VisitServiceItemMapper;
import com.example.demo.model.Visit;
import com.example.demo.service.visit.VisitService;
import com.example.demo.service.visit_service_item.VisitServiceItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/admin/visits")
@RequiredArgsConstructor
public class AdminCartController {

    private final VisitService visitService;
    private final VisitServiceItemService visitServiceItemService;
    private final VisitMapper visitMapper;
    private final VisitServiceItemMapper itemMapper;

    @PostMapping("/{visitId}/cart/add/items")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<VisitResponse>> addItem(
            @PathVariable UUID visitId,
            @RequestBody @Valid VisitServiceItemCreateRequest req
    ) {
        Visit updated = visitService.addItemToVisit(
                visitId,
                req.getMedicalServiceId(),
                req.getQuantity()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Item added",
                        visitMapper.toResponse(updated)
                ));
    }

    @PutMapping("/{visitId}/cart/update-quantity/items/{itemId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<VisitResponse>> updateItemQuantity(
            @PathVariable UUID visitId,
            @PathVariable UUID itemId,
            @RequestBody @Valid VisitServiceItemUpdateRequest req
    ) {
        Visit updated = visitService.updateItemQuantity(
                visitId,
                itemId,
                req.getQuantity()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Item updated",
                        visitMapper.toResponse(updated)
                ));
    }

    @DeleteMapping("/{visitId}/cart/remove/items/{itemId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<VisitResponse>> removeItem(
            @PathVariable UUID visitId,
            @PathVariable UUID itemId
    ) {
        Visit updated = visitService.removeItemFromVisit(visitId, itemId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Item removed",
                        visitMapper.toResponse(updated)
                ));
    }

    @PostMapping("/{visitId}/cart/items/clear")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<VisitResponse>> clearCart(
            @PathVariable UUID visitId
    ) {
        Visit updated = visitService.clearCart(visitId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Cart cleared",
                        visitMapper.toResponse(updated)
                ));
    }

    @GetMapping("/{visitId}/cart/get/items")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<VisitServiceItemResponse>>> getItems(
            @PathVariable UUID visitId
    ) {
        var items = visitServiceItemService.getItemsForVisit(visitId);
        var response = itemMapper.toResponseList(items);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Items fetched",
                        response
                ));
    }
}