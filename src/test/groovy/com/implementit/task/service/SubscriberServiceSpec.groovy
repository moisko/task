package com.implementit.task.service

import com.implementit.task.dto.SubscriberDto
import com.implementit.task.dto.UpdateSubscriberDto
import com.implementit.task.model.Product
import com.implementit.task.model.Subscriber
import com.implementit.task.repository.ProductRepository
import com.implementit.task.repository.SubscriberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import javax.persistence.EntityNotFoundException
import javax.validation.ConstraintViolationException
import java.time.LocalDateTime

@SpringBootTest
class SubscriberServiceSpec extends Specification {
    @Autowired
    private SubscriberRepository subscriberRepository
    @Autowired
    private ProductRepository productRepository
    @Autowired
    private SubscriberService subscriberService

    def cleanup() {
        subscriberRepository.deleteAll()
        productRepository.deleteAll()
    }

    def "should fetch a subscriber who already made a purchase"() {
        given: "an already existing subscriber who already made a purchase"
        Subscriber subscriber = subscriberRepository.save(new Subscriber(
                firstName: 'John',
                lastName: 'Doe',
                products: [
                        new Product(
                                name: "Lie Nielsen plane 164",
                                available: Boolean.TRUE,
                                subscribers: [] as Set
                        )
                ] as Set
        ))

        when: "a request to fetch the subscriber is made"
        SubscriberDto subscriberDto = subscriberService.fetchSubscriber(subscriber.id)

        then: "the subscriber is fetched and mapped to dto"
        subscriberDto
        and: "it's first name is John"
        subscriberDto.firstName == "John"
        and: "it's last name is Doe"
        subscriberDto.lastName == "Doe"
        and: "the subscriber has purchased one product so far"
        subscriberDto.productIds.size() == 1
        and: "this product is actually saved in the data source"
        subscriberDto.productIds[0]
    }

    def "should throw an exception when an attempt to fetch a non existing entity is made"() {
        when: "an attempt to fetch a non existing entity is made"
        subscriberService.fetchSubscriber(-1L)

        then: "an entity not found exception should be thrown"
        Exception ex = thrown()
        ex instanceof EntityNotFoundException
    }

    def "should fetch a subscriber who is about to make a purchase"() {
        given: "an already existing subscriber with no purchases so far"
        Subscriber subscriber = subscriberRepository.save(new Subscriber(
                firstName: 'John',
                lastName: 'Doe',
        ))
        and: "a product that the subscriber wants to buy"
        Product woodPlane = new Product(
                name: "Lie Nielsen plane 164",
                createdDate: LocalDateTime.now(),
        )
        and: "the product was finally purchased by the subscriber"
        subscriber.addProduct(woodPlane)
        subscriberRepository.save(subscriber)

        when: "a request to fetch the subscriber is made"
        SubscriberDto subscriberDto = subscriberService.fetchSubscriber(subscriber.id)

        then: "the subscriber is fetched and mapped to dto"
        subscriberDto
        and: "it's first name is John"
        subscriberDto.firstName == "John"
        and: "it's last name is Doe"
        subscriberDto.lastName == "Doe"
        and: "the subscriber has purchased one product so far"
        subscriberDto.productIds.size() == 1
        and: "it has an id"
        subscriberDto.productIds
    }

    def "should register a subscriber"() {
        when: "this subscriber is registered into the system"
        SubscriberDto subscriber = subscriberService.registerSubscriber(new SubscriberDto(
                firstName: 'John',
                lastName: 'Doe',
        ))

        then: "it is successfully registered"
        subscriber.id
        subscriber.firstName == "John"
        subscriber.lastName == "Doe"
        subscriber.createdDate
        subscriber.productIds.size() == 0
    }

    def "should throw an exception when an attempt to register an invalid subscriber is made"() {
        when: "an attempt to register a subscriber with missing first name"
        subscriberService.registerSubscriber(new SubscriberDto(
                lastName: 'Doe',
        ))
        then: "a validation exception should be thrown"
        Exception ex = thrown()
        and: "the exception is of type ConstraintViolationException"
        ex instanceof ConstraintViolationException
    }

    def "should remove an already existing subscriber"() {
        given: "an existing subscriber"
        Subscriber subscriber = subscriberRepository.save(new Subscriber(
                firstName: 'John',
                lastName: 'Doe',
                products: [
                        new Product(
                                name: "Lie Nielsen plane 164",
                                createdDate: LocalDateTime.now(),
                                available: Boolean.TRUE,
                        )
                ] as Set
        ))
        and: "the product id of the wood plane that he has purchased"
        Long productId = subscriber.products[0].id

        when: "a request to delete that subscriber is made"
        subscriberService.removeSubscriber(subscriber.id)

        then: "the removed subscriber is no longer present in the data source"
        subscriberRepository.findById(subscriber.id).isEmpty()
        and: "the product associated to this subscriber is still present in the data source"
        productRepository.findById(productId).isPresent()
    }

    def "should fetch all subscribers for a given product"() {
        given: "a Lie Nielsen wood plane"
        Product handPlane = productRepository.save(new Product(
                name: "Lie Nielsen plane 164",
                available: Boolean.TRUE,
        ))

        and: "some subscribers"
        List<Subscriber> subscribers = subscriberRepository.saveAll([
                new Subscriber(
                        firstName: 'John',
                        lastName: 'Doe',
                ),
                new Subscriber(
                        firstName: 'Chuck',
                        lastName: 'Norris',
                ),
                new Subscriber(
                        firstName: 'Jamy',
                        lastName: 'Raegen',
                )
        ])

        and: "John and Chucky decide they will become carpenters"
        Subscriber johnDoe = subscribers.find { it.firstName == 'John' && it.lastName == 'Doe' }
        johnDoe.addProduct(handPlane)

        Subscriber chuckNorris = subscribers.find { it.firstName == 'Chuck' && it.lastName == 'Norris' }
        chuckNorris.addProduct(handPlane)

        subscriberRepository.saveAll([johnDoe, chuckNorris])

        when: "a request to get all the subscribers who has bought Lie Nielsen hand plane"
        Set<SubscriberDto> wonnaBeCarpenters = subscriberService.fetchAllByProductId(handPlane.id)

        then: "the returned collection contains only John and Chucky"
        wonnaBeCarpenters.size() == 2
        wonnaBeCarpenters.collect { it.firstName }.containsAll(['John', 'Chuck'])
        wonnaBeCarpenters.find { it.firstName == 'Jamy' } == null
    }

    def "should update the subscriber"() {
        given: "an existing subscriber"
        Subscriber subscriber = subscriberRepository.save(new Subscriber(
                firstName: 'John',
                lastName: 'Doe',
        ))

        when: "a request to update it's first and last name is made"
        SubscriberDto updateSubscriber = subscriberService.updateSubscriber([
                id       : subscriber.id,
                firstName: "Chuck",
                lastName : "Norris"
        ] as UpdateSubscriberDto)

        then: "it's actually updated"
        updateSubscriber.firstName == "Chuck"
        updateSubscriber.lastName == "Norris"
    }

    def "should throw an exception when subscriber id is not present"() {
        when: "a request to update it's first and last name is made"
        subscriberService.updateSubscriber([
                id       : null,
                firstName: "Chuck",
                lastName : "Norris"
        ] as UpdateSubscriberDto)

        then: "a validation exception is thrown"
        Exception ex = thrown()
        ex instanceof ConstraintViolationException
    }
}
