package jm.productmanager.integration;

import jm.productmanager.dto.CategoryDTO;
import jm.productmanager.dto.ProductDTO;
import jm.productmanager.exception.CategoryNotFoundException;
import jm.productmanager.exception.MessageResponse;
import jm.productmanager.model.Category;
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
public class CategoryControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    private final String API_CATEGORIES_URL = "/api/v1/categories";
    private static final Category testCategory = new Category(UUID.fromString("34b1101f-835b-4057-af7a-0256200ae22c"), "Electronics", 50.0, 50000.0);
    private static final UUID testCategoryId = testCategory.getId();

    @Test
    void shouldReturnCategoriesList() {
        ResponseEntity<List<Category>> response = restTemplate.exchange(API_CATEGORIES_URL, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void shouldThrowCategoryNotFoundExceptionWhenGet() {
        UUID nonExistentCategoryId = UUID.randomUUID();

        ResponseEntity<Category> response = restTemplate.getForEntity(API_CATEGORIES_URL + "/" + nonExistentCategoryId, Category.class);

        assertCategoryNotFound(nonExistentCategoryId, response);
    }

    @Test
    void shouldThrowCategoryNotFoundExceptionWhenPatch() {
        UUID nonExistentCategoryId = UUID.randomUUID();

        ResponseEntity<Category> response = restTemplate.exchange(API_CATEGORIES_URL + "/" + nonExistentCategoryId, HttpMethod.PATCH, new HttpEntity<>(new ProductDTO(), null), Category.class);

        assertCategoryNotFound(nonExistentCategoryId, response);
    }

    @Test
    void shouldThrowCategoryNotFoundExceptionWhenDelete() {
        UUID nonExistentCategoryId = UUID.randomUUID();

        ResponseEntity<MessageResponse> response = restTemplate.exchange(API_CATEGORIES_URL + "/" + nonExistentCategoryId, HttpMethod.DELETE, null, MessageResponse.class);

        assertCategoryNotFound(nonExistentCategoryId, response);
    }

    private void assertCategoryNotFound(UUID nonExistentCategoryId, ResponseEntity<?> response) {
        assertNotEquals(testCategoryId, nonExistentCategoryId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThrows(CategoryNotFoundException.class, () -> {
            throw new CategoryNotFoundException(nonExistentCategoryId);
        });
    }

    @Test
    void shouldReturnCategoryById() {
        ResponseEntity<Category> response = restTemplate.getForEntity(API_CATEGORIES_URL + "/" + testCategoryId, Category.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testCategoryId, response.getBody().getId());
    }

    static Stream<CategoryDTO> categoryDTOProvider() {
        return Stream.of(
                new CategoryDTO("validName", 60.0, 500.0),

                new CategoryDTO("a", 60.0, 500.0),
                new CategoryDTO("CategoryNameTooLongForValidation123", 60.0, 500.0),
                new CategoryDTO("*^&", 60.0, 500.0),
                new CategoryDTO("Electronics", 60.0, 500.0),

                new CategoryDTO("validName", -1.0, 500.0),
                new CategoryDTO("validName", 60000.0, -500.0),

                new CategoryDTO("validName", 60.0, 1.0)
        );
    }

    @ParameterizedTest
    @MethodSource("categoryDTOProvider")
    void shouldValidProductWhenPost(CategoryDTO categoryDTO) {
        ResponseEntity<Category> response = restTemplate.postForEntity(API_CATEGORIES_URL, categoryDTO, Category.class);

        validateProductDTO(categoryDTO, response);
    }

    @ParameterizedTest
    @MethodSource("categoryDTOProvider")
    void shouldValidProductWhenPatch(CategoryDTO categoryDTO) {
        ResponseEntity<Category> response = restTemplate.exchange(API_CATEGORIES_URL + "/" + testCategoryId, HttpMethod.PATCH, new HttpEntity<>(categoryDTO, null), Category.class);

        validateProductDTO(categoryDTO, response);
    }

    private static void validateProductDTO(CategoryDTO categoryDTO, ResponseEntity<Category> response) {
        if (categoryDTO.getName() != null && !categoryDTO.getName().matches("^[a-zA-Z0-9]{3,20}$")) {
            assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("Category name should be between 3 and 20 characters and contain only letters and numbers");
            });
        } else if (categoryDTO.getName() != null && categoryDTO.getName().contains("Electronics")) {
            assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("Category with name " + categoryDTO.getName() + " already exists");
            });
        } else if (categoryDTO.getMinPrice() != null && categoryDTO.getMinPrice() < 0 ) {
            assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("Category minimum price must be greater than 0. Provided value: " + categoryDTO.getMinPrice());
            });
        } else if (categoryDTO.getMaxPrice() != null && categoryDTO.getMaxPrice() < 0 ) {
            assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("Category maximum price must be greater than 0. Provided value: " + categoryDTO.getMaxPrice());
            });
        } else if (categoryDTO.getMinPrice() != null && categoryDTO.getMaxPrice() != null && categoryDTO.getMinPrice() > categoryDTO.getMaxPrice()) {
            assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("Category minimum price must be less than maximum price. " +
                        "Provided values: minPrice= " + categoryDTO.getMinPrice() + " maxPrice= " + categoryDTO.getMaxPrice());
            });
        } else {
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("validName", response.getBody().getName());
            assertEquals(60.0, response.getBody().getMinPrice());
            assertEquals(500.0, response.getBody().getMaxPrice());
            return;
        }
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldDeleteProduct() {
        restTemplate.delete( "/api/v1/products/123e4567-e89b-12d3-a456-426614174000");
        ResponseEntity<MessageResponse> response = restTemplate.exchange(API_CATEGORIES_URL + "/" + testCategoryId, HttpMethod.DELETE, null, MessageResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Category with id: " + testCategoryId + " has been deleted", response.getBody().getMessage());
    }

}
