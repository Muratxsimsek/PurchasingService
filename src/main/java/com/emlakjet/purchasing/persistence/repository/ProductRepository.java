package com.emlakjet.purchasing.persistence.repository;

import com.emlakjet.purchasing.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("SELECT i FROM Product i WHERE  i.name = :name")
    ProductEntity findByName(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("DELETE FROM Product i WHERE  i.name = :name")
    void deleteByName(@Param("name") String name);

}