package com.emlakjet.purchasing.controller;

import com.emlakjet.purchasing.controller.Dto.InvoiceDto;
import com.emlakjet.purchasing.controller.Dto.ProductDto;
import com.emlakjet.purchasing.controller.advice.Dto.CommonResponseDTO;
import com.emlakjet.purchasing.exception.ProductNotFoundException;
import com.emlakjet.purchasing.persistence.entity.InvoiceEntity;
import com.emlakjet.purchasing.persistence.entity.ProductEntity;
import com.emlakjet.purchasing.persistence.entity.UserEntity;
import com.emlakjet.purchasing.service.InvoiceService;
import com.emlakjet.purchasing.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final ProductService productService;

    @Operation(summary = "Submit Invoice", description = "you can submit invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice Submitted"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public CommonResponseDTO submitInvoice(@RequestBody InvoiceDto invoiceDto) throws ProductNotFoundException {

        ProductEntity product = productService.getProductByName(invoiceDto.getProductName());
        if(product==null || !invoiceDto.getProductName().equals(product.getName())) {
            throw new ProductNotFoundException();
        }
        InvoiceEntity invoiceEntity = toInvoiceEntity(invoiceDto);
        InvoiceEntity newInvoiceEntity = invoiceService.submitInvoice(invoiceEntity);
        String message = "Invoice rejected";
        if (newInvoiceEntity.getApproved().equals(true)) {
            message = "Invoice accepted";
        }

        return CommonResponseDTO.builder()
                .data(newInvoiceEntity)
                .message(message).build();
    }

    @Operation(summary = "Find All Invoices", description = "you can fetch all invoices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Invoices Fetched"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<InvoiceDto> findAllInvoices() {
        return toProductDtoList(invoiceService.findAllInvoices());
    }

    @Operation(summary = "Find All Approved Invoices", description = "you can fetch approved invoices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Approved Invoices Fetched"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/approved")
    public List<InvoiceDto> getApprovedInvoices(HttpSession session) {
        UserEntity userEntity = getUserEntity(session);
        List<InvoiceEntity> approvedInvoices = invoiceService.findByApproved(userEntity.getFirstName(), userEntity.getLastName(), userEntity.getEmail(), true);
        return toProductDtoList(approvedInvoices);
    }

    @Operation(summary = "Find All Rejected Invoices", description = "you can fetch rejected invoices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Rejected Invoices Fetched"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/rejected")
    public List<InvoiceDto> getRejectedInvoices(HttpSession session) {
        UserEntity userEntity = getUserEntity(session);
        List<InvoiceEntity> rejectedInvoices = invoiceService.findByApproved(userEntity.getFirstName(), userEntity.getLastName(), userEntity.getEmail(), false);
        return toProductDtoList(rejectedInvoices);
    }

    private UserEntity getUserEntity(HttpSession session) {
        return (UserEntity) session.getAttribute("userEntity");
    }

    private InvoiceEntity toInvoiceEntity(InvoiceDto invoiceDto) {
        return InvoiceEntity.builder()
                .firstName(invoiceDto.getFirstName())
                .lastName(invoiceDto.getLastName())
                .email(invoiceDto.getEmail())
                .amount(invoiceDto.getAmount())
                .billNo(invoiceDto.getBillNo())
                .productName(invoiceDto.getProductName()).build();
    }

    private InvoiceDto toInvoiceDto(InvoiceEntity invoiceEntity) {
        return InvoiceDto.builder()
                .firstName(invoiceEntity.getFirstName())
                .lastName(invoiceEntity.getLastName())
                .email(invoiceEntity.getEmail())
                .amount(invoiceEntity.getAmount())
                .billNo(invoiceEntity.getBillNo())
                .productName(invoiceEntity.getProductName()).build();
    }

    private List<InvoiceDto> toProductDtoList(List<InvoiceEntity>  invoiceEntities) {
        return invoiceEntities.stream().map(invoiceEntity -> toInvoiceDto(invoiceEntity)).collect(Collectors.toList());
    }
}