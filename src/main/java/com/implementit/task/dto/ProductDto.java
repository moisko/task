package com.implementit.task.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class ProductDto {
    private Long id;
    @NotBlank
    private String name;
    private LocalDateTime createdDate;
    private Boolean available;
}
