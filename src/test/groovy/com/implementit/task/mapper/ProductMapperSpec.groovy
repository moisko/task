package com.implementit.task.mapper

import com.implementit.task.dto.ProductDto
import com.implementit.task.model.Product
import com.implementit.task.model.Subscriber
import spock.lang.Specification

import java.time.LocalDateTime

class ProductMapperSpec extends Specification {

    def "should map product to product dto"() {
        given: "a product entity"
        Product product = new Product(
                id: 1L,
                name: "Lie Nielsen plane 164",
                createdDate: LocalDateTime.now(),
                available: Boolean.TRUE,
                subscribers: [
                        new Subscriber(id: 2L),
                        new Subscriber(id: 3L),
                        new Subscriber(id: 4L),
                        new Subscriber(id: 5L)
                ] as Set
        )

        when: "a mapping to dto is made"
        ProductDto productDto = ProductMapper.INSTANCE.toDto(product)

        then: "the entity is correctly mapped to dto"
        productDto.id == 1L
        productDto.name == "Lie Nielsen plane 164"
        productDto.createdDate
        productDto.available == Boolean.TRUE
    }
}
