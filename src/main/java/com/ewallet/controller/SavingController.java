package com.ewallet.controller;

import com.ewallet.dto.CreateSavingDTO;
import com.ewallet.model.Saving;
import com.ewallet.service.SavingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/savings")
@RequiredArgsConstructor
public class SavingController {
    
    private final SavingService savingService;
    
    /**
     * API untuk membuat saving baru
     * 
     * POST /api/savings
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createSaving(
            @Valid @RequestBody CreateSavingDTO dto) {
        try {
            Saving saving = savingService.createSaving(dto);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Saving account created successfully");
            response.put("data", saving);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * API untuk mendapatkan semua saving
     * 
     * GET /api/savings
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllSavings() {
        List<Saving> savings = savingService.getAllSavings();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Savings retrieved successfully");
        response.put("data", savings);
        response.put("total", savings.size());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * API untuk mendapatkan saving berdasarkan ID
     * 
     * GET /api/savings/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getSavingById(@PathVariable Long id) {
        try {
            Saving saving = savingService.getSavingById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Saving found successfully");
            response.put("data", saving);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    /**
     * API untuk mendapatkan saving berdasarkan account number
     * 
     * GET /api/savings/account/{accountNumber}
     */
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<Map<String, Object>> getSavingByAccountNumber(
            @PathVariable String accountNumber) {
        try {
            Saving saving = savingService.getSavingByAccountNumber(accountNumber);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Saving found successfully");
            response.put("data", saving);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    /**
     * API untuk mendapatkan semua saving milik customer tertentu
     * 
     * GET /api/savings/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Map<String, Object>> getSavingsByCustomerId(
            @PathVariable Long customerId) {
        List<Saving> savings = savingService.getSavingsByCustomerId(customerId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Savings retrieved successfully");
        response.put("data", savings);
        response.put("total", savings.size());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * API untuk deposit (menambah saldo)
     * 
     * POST /api/savings/{id}/deposit
     */
    @PostMapping("/{id}/deposit")
    public ResponseEntity<Map<String, Object>> deposit(
            @PathVariable Long id,
            @RequestParam BigDecimal amount) {
        try {
            Saving saving = savingService.deposit(id, amount);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Deposit successful");
            response.put("data", saving);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * API untuk withdraw (mengurangi saldo)
     * 
     * POST /api/savings/{id}/withdraw
     */
    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Map<String, Object>> withdraw(
            @PathVariable Long id,
            @RequestParam BigDecimal amount) {
        try {
            Saving saving = savingService.withdraw(id, amount);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Withdrawal successful");
            response.put("data", saving);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * API untuk transfer antar saving
     * 
     * POST /api/savings/transfer
     */
    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transfer(
            @RequestParam Long fromSavingId,
            @RequestParam Long toSavingId,
            @RequestParam BigDecimal amount) {
        try {
            savingService.transfer(fromSavingId, toSavingId, amount);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Transfer successful");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
