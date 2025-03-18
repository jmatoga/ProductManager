package jm.productmanager.service;

import jm.productmanager.dto.CategoryDTO;
import jm.productmanager.exception.CategoryNotFoundException;
import jm.productmanager.exception.MessageResponse;
import jm.productmanager.mapper.CategoryMapper;
import jm.productmanager.model.Category;
import jm.productmanager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategory(UUID id) {
        return categoryRepository.findById(id)
                       .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        validateNotNullCategoryDTOFields(categoryDTO);
        validateCategoryDTO(categoryDTO, null);
        return categoryRepository.save(categoryMapper.mapToEntity(categoryDTO));
    }

    private void validateNotNullCategoryDTOFields(CategoryDTO categoryDTO) {
        if (categoryDTO.getName() == null) {
            throw new IllegalArgumentException("Category name cannot be null");
        }
        if (categoryDTO.getMinPrice() == null) {
            throw new IllegalArgumentException("Category minimum price cannot be null");
        }
        if (categoryDTO.getMaxPrice() == null) {
            throw new IllegalArgumentException("Category maximum price cannot be null");
        }
    }

    @Override
    public Category updateCategory(UUID id, CategoryDTO categoryDTO) {
        Category category = getCategory(id);
        validateCategoryDTO(categoryDTO, category);
        categoryMapper.updateCategory(category, categoryDTO);
        return categoryRepository.save(category);
    }

    @Override
    public MessageResponse deleteCategory(UUID id) {
        getCategory(id);
        categoryRepository.deleteById(id);
        return new MessageResponse("Category with id: " + id + " has been deleted");
    }

    private void validateCategoryDTO(CategoryDTO categoryDTO, Category oldCategory) {
        if (categoryDTO.getName() != null && !categoryDTO.getName().matches("^[a-zA-Z0-9]{3,20}$")) {
            throw new IllegalArgumentException("Category name should be between 3 and 20 characters and contain only letters and numbers");
        }
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category with name " + categoryDTO.getName() + " already exists");
        }
        if (categoryDTO.getMinPrice() != null && categoryDTO.getMinPrice() <= 0) {
            throw new IllegalArgumentException("Category minimum price must be greater than 0. Provided value: " + categoryDTO.getMinPrice());
        }
        if (categoryDTO.getMaxPrice() != null && categoryDTO.getMaxPrice() <= 0) {
            throw new IllegalArgumentException("Category maximum price must be greater than 0. Provided value: " + categoryDTO.getMaxPrice());
        }

        Double categoryMinPrice = categoryDTO.getMinPrice() == null ? oldCategory.getMinPrice() : categoryDTO.getMinPrice();
        Double categoryMaxPrice = categoryDTO.getMaxPrice() == null ? oldCategory.getMaxPrice() : categoryDTO.getMaxPrice();
        if(categoryMinPrice > categoryMaxPrice) {
            throw new IllegalArgumentException("Category minimum price must be less than maximum price. " +
                                                       "Provided values: minPrice= " + categoryDTO.getMinPrice() + " maxPrice= " + categoryDTO.getMaxPrice());
        }
    }
}
