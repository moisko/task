package com.implementit.task.repository;

import com.implementit.task.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

    @Query(value = "select s from Subscriber s join s.products p where p.id = :productId")
    Set<Subscriber> findAllSubscribersByProductId(Long productId);
}
