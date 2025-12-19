package com.ewallet.service;

import com.ewallet.dto.CreateSavingDTO;
import com.ewallet.model.Customer;
import com.ewallet.model.Saving;
import com.ewallet.repository.CustomerRepository;
import com.ewallet.repository.SavingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SavingService {
    
    private final SavingRepository savingRepository;
    private final CustomerRepository customerRepository;
    
    /**
     * Membuat saving baru untuk customer
     */
    @Transactional
    public Saving createSaving(CreateSavingDTO dto) {
        // Validasi apakah customer ada
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer with ID " + dto.getCustomerId() + " not found"));
        
        // Validasi apakah account number sudah ada
        if (savingRepository.existsByAccountNumber(dto.getAccountNumber())) {
            throw new RuntimeException("Account number " + dto.getAccountNumber() + " already exists");
        }
        
        Saving saving = new Saving();
        saving.setAccountNumber(dto.getAccountNumber());
        saving.setAccountName(dto.getAccountName());
        saving.setBalance(dto.getBalance());
        saving.setAccountType(dto.getAccountType() != null ? dto.getAccountType() : "REGULAR");
        saving.setStatus("ACTIVE");
        saving.setCustomer(customer);
        
        return savingRepository.save(saving);
    }
    
    /**
     * Mendapatkan semua saving
     */
    @Transactional(readOnly = true)
    public List<Saving> getAllSavings() {
        return savingRepository.findAll();
    }
    
    /**
     * Mendapatkan saving berdasarkan ID
     */
    @Transactional(readOnly = true)
    public Saving getSavingById(Long id) {
        return savingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Saving with ID " + id + " not found"));
    }
    
    /**
     * Mendapatkan saving berdasarkan account number
     */
    @Transactional(readOnly = true)
    public Saving getSavingByAccountNumber(String accountNumber) {
        return savingRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Saving with account number " + accountNumber + " not found"));
    }
    
    /**
     * Mendapatkan semua saving milik customer tertentu
     */
    @Transactional(readOnly = true)
    public List<Saving> getSavingsByCustomerId(Long customerId) {
        return savingRepository.findByCustomerId(customerId);
    }
    
    /**
     * Deposit (menambah saldo)
     */
    @Transactional
    public Saving deposit(Long savingId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Deposit amount must be positive");
        }
        
        Saving saving = getSavingById(savingId);
        saving.setBalance(saving.getBalance().add(amount));
        
        return savingRepository.save(saving);
    }
    
    /**
     * Withdraw (mengurangi saldo)
     */
    @Transactional
    public Saving withdraw(Long savingId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Withdraw amount must be positive");
        }
        
        Saving saving = getSavingById(savingId);
        
        if (saving.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        
        saving.setBalance(saving.getBalance().subtract(amount));
        
        return savingRepository.save(saving);
    }
    
    /**
     * Transfer antar saving
     */
    @Transactional
    public void transfer(Long fromSavingId, Long toSavingId, BigDecimal amount) {
        withdraw(fromSavingId, amount);
        deposit(toSavingId, amount);
    }
}
