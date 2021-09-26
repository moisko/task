package com.implementit.task.service

import com.implementit.task.dto.ProductDto
import com.implementit.task.dto.UpdateProductDto
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
class ProductServiceSpec extends Specification {
    @Autowired
    private ProductService productService
    @Autowired
    private ProductRepository productRepository
    @Autowired
    private SubscriberRepository subscriberRepository

    def cleanup() {
        subscriberRepository.deleteAll()
        productRepository.deleteAll()
    }

    def "should fetch product"() {
        given: "an already existing product"
        Long productId = productRepository.save(
                new Product(
                        name: "Lie Nielsen plane 164",
                        createdDate: LocalDateTime.now(),
                        available: Boolean.TRUE,
                        subscribers: [] as Set
                )
        ).id

        when: "a request to fetch that product is made"
        ProductDto product = productService.fetchProduct(productId)

        then: "the product is successfully fetched from the data store"
        product.id
        product.name == 'Lie Nielsen plane 164'
        product.available
    }

    def "should throw an exception if the requested product does not exist"() {
        when: "a request to fetch a non existing product"
        productService.fetchProduct(-1L)

        then: "a not found exception is thrown"
        Exception ex = thrown()
        ex instanceof EntityNotFoundException
    }

    def "should add a product"() {
        when: "a request to add a valid product is made"
        Long productId = productService.addProduct(new ProductDto(
                name: "Lie Nielsen plane 164",
                available: Boolean.TRUE
        )).id

        then: "it is successfully added"
        Product savedProduct = productRepository.findById(productId).get()
        savedProduct.id
        savedProduct.createdDate
        savedProduct.name == "Lie Nielsen plane 164"
        savedProduct.available == Boolean.TRUE
    }

    def "should throw an exception if an attempt to add an invalid product is made"() {
        when: "a request to add an invalid product is made"
        productService.addProduct(new ProductDto(
                available: Boolean.TRUE
        ))

        then: "a validation exception is thrown"
        Exception ex = thrown()
        ex instanceof ConstraintViolationException
    }

    def "should remove a product"() {
        given: "a subscriber who has purchased already a plane"
        Subscriber subscriber = subscriberRepository.save(
                new Subscriber(
                        firstName: 'Jamy',
                        lastName: 'Raegen',
                        products: [new Product(name: "Lie Nielsen plane 164", available: Boolean.TRUE)]
                )
        )

        when: "a request to delete that product is made"
        productService.removeProduct(subscriber.products[0].id)

        then: "that product no longer exists in the data source"
        productRepository.findById(subscriber.products[0].id).isEmpty()

        and: "the product is no longer in the subscribers' cart"
        productService.fetchAllBySubscriberId(subscriber.id).findIndexOf { it.name == "Lie Nielsen plane 164" } == -1
    }

    def "should update a product"() {
        given: "an already existing product"
        Long productId = productRepository.save(
                new Product(
                        name: "Lie Nielsen plane 164",
                        createdDate: LocalDateTime.now(),
                        available: Boolean.TRUE,
                        subscribers: [] as Set
                )
        ).id

        when: "a request to update that product is made"
        ProductDto updatedProduct = productService.updateProduct(new UpdateProductDto(
                id: productId,
                name: "Dewalt"
        ))

        then: "the product is successfully updated"
        updatedProduct.name == "Dewalt"
    }

    def "should throw validation exception when an attempt to update a product with missing name"() {
        when: "a request to update a product with missing name is made"
        productService.updateProduct(new UpdateProductDto())

        then: "validation exception is thrown"
        Exception ex = thrown()
        ex instanceof ConstraintViolationException
    }

    def "should fetch all products for a given subscriber"() {
        given: "an existing subscriber"
        Subscriber johnDoe = subscriberRepository.save(new Subscriber(firstName: 'John', lastName: 'Doe'))

        and: "a set of products some of them made by him"
        List<Product> products = productRepository.saveAll([
                new Product(
                        name: "Lie Nielsen plane 164",
                        available: Boolean.TRUE,
                ),
                new Product(
                        name: "Tormek T8",
                        available: Boolean.TRUE,
                ),
                new Product(
                        name: "Tormek T4",
                        available: Boolean.TRUE,
                )
        ])
        johnDoe.addProduct(products.find { it.name == "Lie Nielsen plane 164" })
        johnDoe.addProduct(products.find { it.name == "Tormek T8" })
        subscriberRepository.save(johnDoe)

        when: "a request to get all the products made by John Doe is made"
        Set<ProductDto> johnDoeOrderedProducts = productService.fetchAllBySubscriberId(johnDoe.id)

        then: "the correct set of products is returned"
        johnDoeOrderedProducts.size() == 2
        johnDoeOrderedProducts.collect { it.name }.containsAll(["Lie Nielsen plane 164", "Tormek T8"])
        johnDoeOrderedProducts.findIndexOf { it.name == "Tormek T4" } == -1
    }
}
