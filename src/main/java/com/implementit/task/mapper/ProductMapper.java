package com.implementit.task.mapper;

import com.implementit.task.dto.ProductDto;
import com.implementit.task.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toDto(Product product);

    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "subscribers", ignore = true)
    Product toEntity(ProductDto productDto);
}
