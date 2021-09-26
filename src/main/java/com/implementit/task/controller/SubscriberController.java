package com.implementit.task.controller;

import com.implementit.task.dto.SubscriberDto;
import com.implementit.task.dto.UpdateSubscriberDto;
import com.implementit.task.service.SubscriberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @GetMapping("/subscribers/{id}")
    public ResponseEntity<SubscriberDto> getSubscriber(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(subscriberService.fetchSubscriber(id));
    }

    @GetMapping("/subscribers/all")
    public ResponseEntity<Set<SubscriberDto>> getAllByProductId(@RequestParam(name = "productId") Long productId) {
        return ResponseEntity.ok(subscriberService.fetchAllByProductId(productId));
    }

    @PostMapping("/subscribers")
    public ResponseEntity<SubscriberDto> addSubscriber(@RequestBody SubscriberDto subscriberDto) {
        return ResponseEntity.ok(subscriberService.registerSubscriber(subscriberDto));
    }

    @DeleteMapping("/subscribers/{id}")
    public ResponseEntity<Void> removeSubscriber(@PathVariable(value = "id") Long id) {
        subscriberService.removeSubscriber(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/subscribers")
    public ResponseEntity<SubscriberDto> updateSubscriber(@RequestBody @Valid UpdateSubscriberDto updateSubscriberDto) {
        return ResponseEntity.ok(subscriberService.updateSubscriber(updateSubscriberDto));
    }
}
