package com.emlakjet.purchasing.controller.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Product Data Transfer Object")
@Builder
@Data
public class ProductDto {

    @Schema(description = "Name of the Product", example = "Laptop")
    private String name;

    @Schema(description = "Description of the Product", example = "I will use it on the project of company")
    private String description;


}