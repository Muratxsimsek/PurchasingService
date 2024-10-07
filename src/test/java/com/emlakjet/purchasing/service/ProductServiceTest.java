package com.emlakjet.purchasing.service;

import com.emlakjet.purchasing.persistence.entity.ProductEntity;
import com.emlakjet.purchasing.persistence.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:truncate_tables.sql")
@EmbeddedKafka(partitions = 1, topics = {"invoice_rejected_topic"})
@SpringBootTest
@EnableCaching
@AutoConfigureCache
public class ProductServiceTest {

    @SpyBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() {
        if (cacheManager.getCache("products") != null) {
            cacheManager.getCache("products").clear();
        }
    }

    @Test
    public void should_getProductByName_AddProduct_CheckCache() {
        String productName = "Laptop";
        ProductEntity product = new ProductEntity();
        product.setName(productName);

        productRepository.save(product);

        ProductEntity firstCallResult = productService.getProductByName(productName);

        assertThat(firstCallResult).isEqualTo(product);

        clearInvocations(productRepository);

        ProductEntity secondCallResult = productService.getProductByName(productName);

        assertThat(secondCallResult).isEqualTo(product);
        verify(productRepository, times(0)).findByName(productName);
    }

    @Test
    public void should_updateProduct_UpdateProduct_CheckCache() {

        ProductEntity product = new ProductEntity();
        product.setName("Laptop New");
        when(productRepository.save(product)).thenReturn(product);

        ProductEntity updatedProduct = productService.updateProduct(product);

        assertThat(updatedProduct).isEqualTo(product);
        verify(productRepository, times(1)).save(product);

        ProductEntity cachedProduct = cacheManager.getCache("products").get("Laptop New", ProductEntity.class);
        assertThat(cachedProduct).isEqualTo(product);
    }

    @Test
    public void should_deleteProduct_DeleteProduct_CheckCacheIsEmpty() {

        ProductEntity product = new ProductEntity();
        product.setName("Laptop");

        productRepository.save(product);
        productService.getProductByName("Laptop");

        assertThat(cacheManager.getCache("products").get("Laptop")).isNotNull();

        boolean deleteResult = productService.deleteProduct(product.getName());

        assertThat(deleteResult).isTrue();

        assertThat(cacheManager.getCache("products").get("Laptop")).isNull();
    }
}
