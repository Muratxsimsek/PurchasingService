package com.emlakjet.purchasing.persistence.repository;

import com.emlakjet.purchasing.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    ProductEntity findByName(String name);

    void deleteByName(String name);
}