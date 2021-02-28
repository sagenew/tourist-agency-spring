package com.touristagency.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class DiscountDTO {
    @Min(value = 0, message = "LESS THAN 0")
    @Max(value = 100, message = "GREATER THAN 100")
    private double step;

    @Min(value = 0, message = "LESS THAN 0")
    @Max(value = 100, message = "GREATER THAN 100")
    private double threshold;
}
