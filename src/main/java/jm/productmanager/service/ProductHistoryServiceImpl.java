package jm.productmanager.service;

import jm.productmanager.dto.ProductDTO;
import jm.productmanager.exception.MessageResponse;
import jm.productmanager.exception.ProductNotFoundException;
import jm.productmanager.model.EFieldName;
import jm.productmanager.model.Product;
import jm.productmanager.model.ProductHistory;
import jm.productmanager.repository.ProductHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductHistoryServiceImpl implements ProductHistoryService {
    private final ProductHistoryRepository productHistoryRepository;
    private final CategoryService categoryService;

    @Override
    public List<ProductHistory> getProductsHistory() {
        return productHistoryRepository.findAll();
    }

    @Override
    public ProductHistory getProductHistory(UUID productId) {
        return productHistoryRepository.findByProductId(productId)
                       .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Override
    public void createProductHistory(ProductDTO newProduct, Product oldProduct, LocalDateTime now) {
        if (newProduct.getName() != null && !newProduct.getName().equals(oldProduct.getName())) {
            saveProductHistory(oldProduct, EFieldName.NAME, newProduct.getName(), oldProduct.getName(), now);
        }
        if (newProduct.getDescription() != null && !newProduct.getDescription().equals(oldProduct.getDescription())) {
            saveProductHistory(oldProduct, EFieldName.DESCRIPTION, newProduct.getDescription(), oldProduct.getDescription(), now);
        }
        if (newProduct.getPrice() != null && !newProduct.getPrice().equals(oldProduct.getPrice())) {
            saveProductHistory(oldProduct, EFieldName.PRICE, newProduct.getPrice().toString(), oldProduct.getPrice().toString(), now);
        }
        if (newProduct.getCategoryId() != null && !newProduct.getCategoryId().equals(oldProduct.getCategory().getId())) {
            saveProductHistory(oldProduct, EFieldName.CATEGORY, categoryService.getCategory(newProduct.getCategoryId()).getName(), oldProduct.getCategory().getName(), now);
        }
        if (newProduct.getQuantity() != null && !newProduct.getQuantity().equals(oldProduct.getQuantity())) {
            saveProductHistory(oldProduct, EFieldName.QUANTITY, newProduct.getQuantity().toString(), oldProduct.getQuantity().toString(), now);
        }
    }

    private void saveProductHistory(Product product, EFieldName fieldName, String newValue, String oldValue, LocalDateTime now) {
        productHistoryRepository.save(ProductHistory.builder()
                                         .product(product)
                                         .fieldName(fieldName)
                                         .newValue(newValue)
                                         .oldValue(oldValue)
                                         .createdAt(now)
                                         .build());
    }

    @Override
    public MessageResponse deleteProductHistory(UUID id) {
        productHistoryRepository.deleteById(id);
        return new MessageResponse("Product history with id: " + id + " has been deleted");
    }
}
