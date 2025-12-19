package com.ewallet.controller;

import com.ewallet.dto.CreateCustomerDTO;
import com.ewallet.dto.CustomerWithSavingsDTO;
import com.ewallet.model.Customer;
import com.ewallet.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    
    private final CustomerService customerService;
    
    /**
     * API untuk mendapatkan customer berdasarkan Government ID (KTP/NIK)
     * Menampilkan customer beserta semua saving yang dimiliki
     * 
     * GET /api/customers/government-id/{governmentId}
     */
    @GetMapping("/government-id/{governmentId}")
    public ResponseEntity<Map<String, Object>> getCustomerByGovernmentId(
            @PathVariable String governmentId) {
        try {
            CustomerWithSavingsDTO customer = customerService.getCustomerByGovernmentId(governmentId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Customer found successfully");
            response.put("data", customer);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    /**
     * API untuk membuat customer baru
     * 
     * POST /api/customers
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCustomer(
            @Valid @RequestBody CreateCustomerDTO dto) {
        try {
            Customer customer = customerService.createCustomer(dto);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Customer created successfully");
            response.put("data", customer);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * API untuk mendapatkan semua customer
     * 
     * GET /api/customers
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Customers retrieved successfully");
        response.put("data", customers);
        response.put("total", customers.size());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * API untuk mendapatkan customer berdasarkan ID
     * 
     * GET /api/customers/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCustomerById(@PathVariable Long id) {
        try {
            Customer customer = customerService.getCustomerById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Customer found successfully");
            response.put("data", customer);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
