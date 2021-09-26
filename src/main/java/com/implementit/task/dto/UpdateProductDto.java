package com.implementit.task.dto;

import com.implementit.task.validation.Validatable;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateProductDto implements Validatable {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
}
