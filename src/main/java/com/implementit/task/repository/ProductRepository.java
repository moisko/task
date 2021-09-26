package com.implementit.task.repository;

import com.implementit.task.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "select p from Product p join p.subscribers s where s.id = :subscriberId")
    Set<Product> fetchAllProductsBySubscriberId(Long subscriberId);
}
