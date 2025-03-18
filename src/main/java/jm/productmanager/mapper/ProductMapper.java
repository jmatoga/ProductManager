package jm.productmanager.mapper;

import jm.productmanager.dto.ProductDTO;
import jm.productmanager.model.Category;
import jm.productmanager.model.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "productDTO.name")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Product mapToEntity(ProductDTO productDTO, Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "productDTO.name")
    @Mapping(target = "category", source = "category")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProduct(@MappingTarget Product product, ProductDTO productDTO, Category category);
}
