package com.implementit.task.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class SubscriberDto {
    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private LocalDateTime createdDate;
    private Set<Long> productIds;
}
