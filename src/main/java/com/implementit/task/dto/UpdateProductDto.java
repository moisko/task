package com.implementit.task.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateProductDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
}
