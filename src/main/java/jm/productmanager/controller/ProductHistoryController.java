package jm.productmanager.controller;

import jm.productmanager.exception.MessageResponse;
import jm.productmanager.model.ProductHistory;
import jm.productmanager.service.ProductHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products-history")
@RequiredArgsConstructor
public class ProductHistoryController {
    private final ProductHistoryService productHistoryService;

    @GetMapping
    public ResponseEntity<List<ProductHistory>> getProductsHistory() {
        return ResponseEntity.ok(productHistoryService.getProductsHistory());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductHistory> getProductHistory(@PathVariable UUID productId) {
        return ResponseEntity.ok(productHistoryService.getProductHistory(productId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteProductHistory(@PathVariable UUID id) {
        return ResponseEntity.ok(productHistoryService.deleteProductHistory(id));
    }
}
