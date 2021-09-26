package com.implementit.task.controller;

import com.implementit.task.dto.ProductDto;
import com.implementit.task.dto.UpdateProductDto;
import com.implementit.task.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(productService.fetchProduct(id));
    }

    @GetMapping("/products/all")
    public ResponseEntity<Set<ProductDto>> getAllBySubscriber(@RequestParam(name = "subscriberId") Long subscriberId) {
        return ResponseEntity.ok(productService.fetchAllBySubscriberId(subscriberId));
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.addProduct(productDto));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> removeProduct(@PathVariable(value = "id") Long id) {
        productService.removeProduct(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody UpdateProductDto updateProductDto) {
        return ResponseEntity.ok(productService.updateProduct(updateProductDto));
    }
}
