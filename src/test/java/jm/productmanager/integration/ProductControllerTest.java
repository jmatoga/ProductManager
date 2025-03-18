package jm.productmanager.integration;

import jm.productmanager.dto.ProductDTO;
import jm.productmanager.exception.MessageResponse;
import jm.productmanager.exception.ProductNotFoundException;
import jm.productmanager.model.Category;
import jm.productmanager.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles({"test"})
public class ProductControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    private final String API_PRODUCTS_URL = "/api/v1/products";
    private final UUID testProductId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final Category testCategory = new Category(UUID.fromString("34b1101f-835b-4057-af7a-0256200ae22c"), "Electronics", 50.0, 50000.0);
    private static final UUID testCategoryId = testCategory.getId();

    @Test
    void shouldReturnProductsList() {
        ResponseEntity<List<Product>> response = restTemplate.exchange(API_PRODUCTS_URL, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenGet() {
        UUID nonExistentProductId = UUID.randomUUID();

        ResponseEntity<Product> response = restTemplate.getForEntity(API_PRODUCTS_URL + "/" + nonExistentProductId, Product.class);

        assertProductNotFound(nonExistentProductId, response);
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenPatch() {
        UUID nonExistentProductId = UUID.randomUUID();

        ResponseEntity<Product> response = restTemplate.exchange(API_PRODUCTS_URL + "/" + nonExistentProductId, HttpMethod.PATCH, new HttpEntity<>(new ProductDTO(), null), Product.class);

        assertProductNotFound(nonExistentProductId, response);
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenDelete() {
        UUID nonExistentProductId = UUID.randomUUID();

        ResponseEntity<MessageResponse> response = restTemplate.exchange(API_PRODUCTS_URL + "/" + nonExistentProductId, HttpMethod.DELETE, null, MessageResponse.class);

        assertProductNotFound(nonExistentProductId, response);
    }

    private void assertProductNotFound(UUID nonExistentProductId, ResponseEntity<?> response) {
        assertNotEquals(testProductId, nonExistentProductId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThrows(ProductNotFoundException.class, () -> {
            throw new ProductNotFoundException(nonExistentProductId);
        });
    }

    @Test
    void shouldReturnProductById() {
        ResponseEntity<Product> response = restTemplate.getForEntity(API_PRODUCTS_URL + "/" + testProductId, Product.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testProductId, response.getBody().getId());
    }

    static Stream<ProductDTO> productDTOProvider() {
        return Stream.of(
                new ProductDTO("validName", "Valid Example", 60.0, 5, testCategoryId),

                new ProductDTO("a", "Too small name", 60.0, 5, testCategoryId),
                new ProductDTO("ProductNameTooLongForValidation123", "Too long name", 60.0, 5, testCategoryId),
                new ProductDTO("*^&", "Too long name", 60.0, 5, testCategoryId),
                new ProductDTO("blockedWord", "Blocked word in name", 60.0, 5, testCategoryId),
                new ProductDTO("1blockedWord", "Blocked word after name", 60.0, 5, testCategoryId),
                new ProductDTO("blockedWordE", "Blocked word before name", 60.0, 5, testCategoryId),
                new ProductDTO("BLOCKEDword", "Blocked word case insensitive", 60.0, 5, testCategoryId),
                new ProductDTO("existingName", "Name already exists", 60.0, 5, testCategoryId),

                new ProductDTO("validName", "Too low price", 1.0, 5, testCategoryId),
                new ProductDTO("validName", "Too big price", 60000.0, 5, testCategoryId),

                new ProductDTO("validName", "Negative quantity", 60.0, -1, testCategoryId)
        );
    }

    @ParameterizedTest
    @MethodSource("productDTOProvider")
    void shouldValidProductWhenPost(ProductDTO productDTO) {
        ResponseEntity<Product> response = restTemplate.postForEntity(API_PRODUCTS_URL, productDTO, Product.class);

        validateProductDTO(productDTO, response);
    }

    @ParameterizedTest
    @MethodSource("productDTOProvider")
    void shouldValidProductWhenPatch(ProductDTO productDTO) {
        ResponseEntity<Product> response = restTemplate.exchange(API_PRODUCTS_URL + "/" + testProductId, HttpMethod.PATCH, new HttpEntity<>(productDTO, null), Product.class);

        validateProductDTO(productDTO, response);
    }

    private static void validateProductDTO(ProductDTO productDTO, ResponseEntity<Product> response) {
        if (productDTO.getName() != null && !productDTO.getName().matches("^[a-zA-Z0-9]{3,20}$")) {
            assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("Product name should be between 3 and 20 characters and contain only letters and numbers");
            });
        } else if (productDTO.getName() != null && productDTO.getName().contains("existingName")) {
            assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("Product with name " + productDTO.getName() + " already exists");
            });
        } else if (productDTO.getName() != null && productDTO.getName().toLowerCase().contains("blockedword")) {
            assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("Product name contains blocked words");
            });
        } else if (productDTO.getPrice() != null && (productDTO.getPrice() < testCategory.getMinPrice() || productDTO.getPrice() > testCategory.getMaxPrice())) {
            assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("Product price should be between " + testCategory.getMinPrice() + " and " + testCategory.getMaxPrice());
            });
        } else if (productDTO.getQuantity() != null && productDTO.getQuantity() < 0) {
            assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("Product quantity cannot be negative");
            });
        } else {
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("validName", response.getBody().getName());
            assertEquals("Valid Example", response.getBody().getDescription());
            assertEquals(60.0, response.getBody().getPrice());
            assertEquals(5, response.getBody().getQuantity());
            assertEquals(testCategoryId, response.getBody().getCategory().getId());
            return;
        }
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldDeleteProduct() {
        ResponseEntity<MessageResponse> response = restTemplate.exchange(API_PRODUCTS_URL + "/" + testProductId, HttpMethod.DELETE, null, MessageResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product with id: " + testProductId + " has been deleted", response.getBody().getMessage());
    }

}
