package com.emlakjet.purchasing.persistence.repository;

import com.emlakjet.purchasing.persistence.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    @Query("SELECT SUM(i.amount) FROM Invoice i WHERE upper(i.firstName) = upper(:firstName) AND upper(i.lastName) = upper(:lastName) AND upper(i.email) = upper(:email) AND i.approved = true")
    Optional<Double> findApprovedTotalAmountPerUser(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("email") String email);

    @Query("SELECT i FROM Invoice i WHERE upper(i.firstName) = upper(:firstName) AND upper(i.lastName) = upper(:lastName) AND upper(i.email) = upper(:email) AND i.approved = :approved")
    List<InvoiceEntity> findByApproved(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("email") String email, @Param("approved") boolean approved);
}