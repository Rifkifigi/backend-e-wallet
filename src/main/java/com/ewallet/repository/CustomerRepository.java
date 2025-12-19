package com.ewallet.repository;

import com.ewallet.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // Method untuk mencari customer berdasarkan Government ID (KTP/NIK)
    Optional<Customer> findByGovernmentId(String governmentId);
    
    // Method untuk mencari customer berdasarkan Government ID dengan fetch savings
    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.savings WHERE c.governmentId = :governmentId")
    Optional<Customer> findByGovernmentIdWithSavings(@Param("governmentId") String governmentId);
    
    // Method untuk cek apakah Government ID sudah ada
    boolean existsByGovernmentId(String governmentId);
    
    // Method untuk mencari customer berdasarkan email
    Optional<Customer> findByEmail(String email);
}
