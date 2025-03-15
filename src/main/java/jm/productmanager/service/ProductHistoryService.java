package jm.productmanager.service;

import jm.productmanager.dto.ProductDTO;
import jm.productmanager.exception.MessageResponse;
import jm.productmanager.model.Product;
import jm.productmanager.model.ProductHistory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ProductHistoryService {
    List<ProductHistory> getProductsHistory();

    ProductHistory getProductHistory(UUID productId);

    void createProductHistory(ProductDTO newProduct, Product oldProduct, LocalDateTime now);

    MessageResponse deleteProductHistory(UUID id);
}
