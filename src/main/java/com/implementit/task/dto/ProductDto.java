package com.implementit.task.dto;

import com.implementit.task.validation.Validatable;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class ProductDto implements Validatable {
    private Long id;
    @NotBlank
    private String name;
    private LocalDateTime createdDate;
    private Boolean available;
}
