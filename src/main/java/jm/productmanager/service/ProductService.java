package jm.productmanager.service;

import jm.productmanager.dto.ProductDTO;
import jm.productmanager.exception.MessageResponse;
import jm.productmanager.model.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> getProducts();

    Product getProduct(UUID id);

    Product createProduct(ProductDTO product);

    Product updateProduct(UUID id, ProductDTO productDTO);

    MessageResponse deleteProduct(UUID id);
}
