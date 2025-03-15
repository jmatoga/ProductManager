package jm.productmanager.mapper;

import jm.productmanager.dto.CategoryDTO;
import jm.productmanager.model.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category mapToEntity(CategoryDTO categoryDTO);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCategory(@MappingTarget Category category, CategoryDTO categoryDTO);
}
