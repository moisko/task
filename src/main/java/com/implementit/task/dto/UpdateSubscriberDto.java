package com.implementit.task.dto;

import com.implementit.task.validation.Validatable;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateSubscriberDto implements Validatable {
    @NotNull
    private Long id;
    private String firstName;
    private String lastName;
}
