package com.implementit.task.mapper;

import com.implementit.task.dto.SubscriberDto;
import com.implementit.task.model.Product;
import com.implementit.task.model.Subscriber;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Mapper
public interface SubscriberMapper {
    SubscriberMapper INSTANCE = Mappers.getMapper(SubscriberMapper.class);

    @Mapping(target = "productIds", source = "products", qualifiedByName = "productsToProductIds")
    SubscriberDto toDto(Subscriber subscriber);

    @Mapping(target = "products", source = "products")
    @Mapping(target = "id", source = "subscriberDto.id")
    @Mapping(target = "firstName", source = "subscriberDto.firstName")
    @Mapping(target = "lastName", source = "subscriberDto.lastName")
    @Mapping(target = "createdDate", ignore = true)
    Subscriber toEntity(SubscriberDto subscriberDto, Set<Product> products);

    @Named("productsToProductIds")
    static Set<Long> productsToProductIds(Set<Product> products) {
        return products.stream().map(Product::getId).collect(toSet());
    }
}
