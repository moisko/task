package com.implementit.task.service;

import com.implementit.task.dto.SubscriberDto;
import com.implementit.task.dto.UpdateSubscriberDto;
import com.implementit.task.mapper.SubscriberMapper;
import com.implementit.task.repository.SubscriberRepository;
import com.implementit.task.validation.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final ValidationService validationService;

    public SubscriberService(SubscriberRepository subscriberRepository, ValidationService validationService) {
        this.subscriberRepository = subscriberRepository;
        this.validationService = validationService;
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
        validationService.validate(subscriberDto);
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
        validationService.validate(updateSubscriberDto);
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
