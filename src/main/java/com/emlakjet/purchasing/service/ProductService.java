package com.emlakjet.purchasing.service;

import com.emlakjet.purchasing.exception.ProductNotFoundException;
import com.emlakjet.purchasing.persistence.entity.ProductEntity;
import com.emlakjet.purchasing.persistence.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@CacheConfig(cacheNames = {"products"})
public class ProductService {

    private ProductRepository productRepository;

    @Cacheable(key = "#name")
    public ProductEntity getProductByName(String name) {
        return productRepository.findByName(name);
    }

    public ProductEntity createProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    @CachePut(key = "#name")
    public ProductEntity updateProduct(String name, ProductEntity product) throws ProductNotFoundException {
        ProductEntity currentProduct = productRepository.findByName(name);
        if (currentProduct != null) {
            currentProduct.setDescription(product.getDescription());
            return productRepository.save(currentProduct);
        } else {
            throw new ProductNotFoundException();
        }
    }

    @CacheEvict(key = "#name")
    public boolean deleteProduct(String name) {
        ProductEntity product = productRepository.findByName(name);
        if (product != null) {
            productRepository.deleteByName(product.getName());
            return true;
        }
        return false;
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }
}

