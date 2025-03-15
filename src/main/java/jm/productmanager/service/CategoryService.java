package jm.productmanager.service;

import jm.productmanager.dto.CategoryDTO;
import jm.productmanager.exception.MessageResponse;
import jm.productmanager.model.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> getCategories();

    Category getCategory(UUID id);

    Category createCategory(CategoryDTO categoryDTO);

    Category updateCategory(UUID id, CategoryDTO categoryDTO);

    MessageResponse deleteCategory(UUID id);
}
