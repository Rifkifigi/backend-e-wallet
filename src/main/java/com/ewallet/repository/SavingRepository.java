package com.ewallet.repository;

import com.ewallet.model.Saving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingRepository extends JpaRepository<Saving, Long> {
    
    // Method untuk mencari saving berdasarkan account number
    Optional<Saving> findByAccountNumber(String accountNumber);
    
    // Method untuk mencari semua saving milik customer tertentu
    List<Saving> findByCustomerId(Long customerId);
    
    // Method untuk mencari saving berdasarkan status
    List<Saving> findByStatus(String status);
    
    // Method untuk cek apakah account number sudah ada
    boolean existsByAccountNumber(String accountNumber);
}
