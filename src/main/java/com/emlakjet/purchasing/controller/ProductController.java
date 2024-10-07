package com.emlakjet.purchasing.controller;

import com.emlakjet.purchasing.controller.Dto.ProductDto;
import com.emlakjet.purchasing.controller.advice.Dto.CommonResponseDTO;
import com.emlakjet.purchasing.exception.ProductCantRemovedException;
import com.emlakjet.purchasing.exception.ProductNotFoundException;
import com.emlakjet.purchasing.persistence.entity.ProductEntity;
import com.emlakjet.purchasing.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;

    @Operation(summary = "Find Product", description = "you can find any product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{name}")
    public ProductDto getProductByName(@PathVariable String name) throws ProductNotFoundException {
        ProductEntity productEntity = productService.getProductByName(name);
        if (productEntity != null) {
            return toProductDto(productEntity);
        } else {
            throw new ProductNotFoundException();
        }
    }

    @Operation(summary = "Add Product", description = "you can add any product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto) {

        ProductEntity newProductEntity = productService.createProduct(toProductEntity(productDto));

        return toProductDto(newProductEntity);
    }

    @Operation(summary = "Update Product", description = "you can update any product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{name}")
    public ProductDto updateProduct(@PathVariable String name, @RequestBody ProductDto updatedProduct) throws ProductNotFoundException {
        ProductEntity productEntity = toProductEntity(updatedProduct);
        ProductEntity updatedProductEntity = productService.updateProduct(name, productEntity);
        return toProductDto(updatedProductEntity);
    }

    @Operation(summary = "Remove Product", description = "you can remove any product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product removed successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{name}")
    public CommonResponseDTO deleteProduct(@PathVariable String name) throws ProductCantRemovedException {
        boolean deleted = productService.deleteProduct(name);
        if (!deleted) {
            throw new ProductCantRemovedException();
        } else {
            return CommonResponseDTO.builder()
                    .data(null)
                    .message("Product Deleted").build();
        }
    }

    @Operation(summary = "Find All Products", description = "you can find all product in the db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Products fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<ProductDto> getAllProducts() {
        return toProductDtoList(productService.getAllProducts());
    }

    private ProductEntity toProductEntity(ProductDto productDto) {
        return ProductEntity.builder().name(productDto.getName()).description(productDto.getDescription()).build();
    }

    private ProductDto toProductDto(ProductEntity productEntity) {
        return ProductDto.builder().name(productEntity.getName()).description(productEntity.getDescription()).build();
    }

    private List<ProductDto> toProductDtoList(List<ProductEntity> productEntities) {
        return productEntities.stream().map(productEntity -> toProductDto(productEntity)).collect(Collectors.toList());
    }
}
