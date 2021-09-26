package com.implementit.task.service;

import com.implementit.task.dto.SubscriberDto;
import com.implementit.task.dto.UpdateSubscriberDto;
import com.implementit.task.mapper.SubscriberMapper;
import com.implementit.task.repository.SubscriberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final Validator validator;

    public SubscriberService(SubscriberRepository subscriberRepository, Validator validator) {
        this.subscriberRepository = subscriberRepository;
        this.validator = validator;
    }

    @Transactional(readOnly = true)
    public SubscriberDto fetchSubscriber(Long id) {
        return subscriberRepository.findById(id)
                .map(SubscriberMapper.INSTANCE::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Set<SubscriberDto> fetchAllByProductId(Long productId) {
        return subscriberRepository.findAllSubscribersByProductId(productId)
                .stream()
                .map(SubscriberMapper.INSTANCE::toDto)
                .collect(toSet());
    }

    @Transactional
    public SubscriberDto registerSubscriber(SubscriberDto subscriberDto) {
        Set<ConstraintViolation<SubscriberDto>> violations = validator.validate(subscriberDto);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<SubscriberDto> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }
        return SubscriberMapper.INSTANCE.toDto(
                subscriberRepository.save(SubscriberMapper.INSTANCE.toEntity(subscriberDto, Set.of()))
        );
    }

    @Transactional
    public void removeSubscriber(Long id) {
        subscriberRepository.findById(id).ifPresent(subscriberRepository::delete);
    }

    @Transactional
    public SubscriberDto updateSubscriber(UpdateSubscriberDto updateSubscriberDto) {
        Set<ConstraintViolation<UpdateSubscriberDto>> violations = validator.validate(updateSubscriberDto);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<UpdateSubscriberDto> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }
        return subscriberRepository.findById(updateSubscriberDto.getId())
                .map(subscriber -> {
                    boolean shouldUpdate = false;
                    if (Objects.nonNull(updateSubscriberDto.getFirstName()) && !subscriber.getFirstName().equals(updateSubscriberDto.getFirstName())) {
                        subscriber.setFirstName(updateSubscriberDto.getFirstName());
                        shouldUpdate = true;
                    }
                    if (Objects.nonNull(updateSubscriberDto.getLastName()) && !subscriber.getLastName().equals(updateSubscriberDto.getLastName())) {
                        subscriber.setLastName(updateSubscriberDto.getLastName());
                        shouldUpdate = true;
                    }
                    return shouldUpdate
                            ? SubscriberMapper.INSTANCE.toDto(subscriberRepository.save(subscriber))
                            : SubscriberMapper.INSTANCE.toDto(subscriber);
                })
                .orElseThrow(EntityNotFoundException::new);
    }
}
