package com.implementit.task.dto;

import com.implementit.task.validation.Validatable;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class SubscriberDto implements Validatable {
    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private LocalDateTime createdDate;
    private Set<Long> productIds;
}
