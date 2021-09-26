package com.implementit.task.mapper

import com.implementit.task.dto.SubscriberDto
import com.implementit.task.model.Product
import com.implementit.task.model.Subscriber
import spock.lang.Specification

import java.time.LocalDateTime

class SubscriberMapperSpec extends Specification {

    def "should map subscriber to subscriber dto"() {
        given: "a subscriber entity"
        Subscriber subscriberEntity = new Subscriber(
                id: 1L,
                firstName: "John",
                lastName: "Doe",
                createdDate: LocalDateTime.now(),
                products: [
                        new Product(id: 2L),
                        new Product(id: 3L),
                        new Product(id: 4L),
                        new Product(id: 5L)
                ] as Set
        )

        when: "a mapping to dto is made"
        SubscriberDto subscriberDto = SubscriberMapper.INSTANCE.toDto(subscriberEntity)

        then: "the entity is correctly mapped to dto"
        subscriberDto.id == 1L
        subscriberDto.firstName == "John"
        subscriberDto.lastName == "Doe"
        subscriberDto.createdDate
        subscriberDto.productIds == [2L, 3L, 4L, 5L] as Set
    }
}
