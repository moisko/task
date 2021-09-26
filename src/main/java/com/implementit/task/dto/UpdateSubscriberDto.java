package com.implementit.task.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateSubscriberDto {
    @NotNull
    private Long id;
    private String firstName;
    private String lastName;
}
