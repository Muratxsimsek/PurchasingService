package com.emlakjet.purchasing.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity(name = "Product")
public class ProductEntity {

    @Id
    @Column(unique = true, nullable = false)
    private String name;

    private String description;


}