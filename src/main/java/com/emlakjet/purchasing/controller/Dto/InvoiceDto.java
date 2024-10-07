package com.emlakjet.purchasing.controller.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Invoice Data Transfer Object")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class InvoiceDto {

    @Schema(description = "First Name", example = "Murat")
    private String firstName;
    @Schema(description = "Last Name", example = "Simsek")
    private String lastName;
    @Schema(description = "Email", example = "muratxsimsek@gmail.com")
    private String email;
    @Schema(description = "Amount", example = "150.78")
    private Double amount;
    @Schema(description = "Product Name", example = "Laptop")
    private String productName;
    @Schema(description = "Bill No", example = "AP12377")
    private String billNo;

}