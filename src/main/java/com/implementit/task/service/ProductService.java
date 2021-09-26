package com.implementit.task.service;

import com.implementit.task.dto.ProductDto;
import com.implementit.task.dto.UpdateProductDto;
import com.implementit.task.mapper.ProductMapper;
import com.implementit.task.model.Subscriber;
import com.implementit.task.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final Validator validator;

    public ProductService(ProductRepository productRepository, Validator validator) {
        this.productRepository = productRepository;
        this.validator = validator;
    }

    @Transactional(readOnly = true)
    public ProductDto fetchProduct(Long id) {
        return productRepository.findById(id)
                .map(ProductMapper.INSTANCE::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Set<ProductDto> fetchAllBySubscriberId(Long subscriberId) {
        return productRepository.fetchAllProductsBySubscriberId(subscriberId)
                .stream()
                .map(ProductMapper.INSTANCE::toDto)
                .collect(toSet());
    }

    @Transactional
    public ProductDto addProduct(ProductDto productDto) {
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(productDto);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<ProductDto> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }
        return ProductMapper.INSTANCE.toDto(
                productRepository.save(ProductMapper.INSTANCE.toEntity(productDto))
        );
    }

    @Transactional
    public void removeProduct(Long id) {
        productRepository.findById(id).ifPresent(product -> {
            for (Subscriber subscriber : product.getSubscribers()) {
                subscriber.removeProduct(product);
            }
            productRepository.deleteById(id);
        });
    }

    @Transactional
    public ProductDto updateProduct(UpdateProductDto updateProductDto) {
        Set<ConstraintViolation<UpdateProductDto>> violations = validator.validate(updateProductDto);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<UpdateProductDto> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }
        return productRepository.findById(updateProductDto.getId())
                .map(product -> {
                    boolean shouldUpdate = false;
                    if (!product.getName().equals(updateProductDto.getName())) {
                        product.setName(updateProductDto.getName());
                        shouldUpdate = true;
                    }
                    return shouldUpdate
                            ? ProductMapper.INSTANCE.toDto(productRepository.save(product))
                            : ProductMapper.INSTANCE.toDto(product);
                })
                .orElseThrow(EntityNotFoundException::new);
    }
}
