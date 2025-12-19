package com.ewallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "savings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Saving {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Account number is required")
    @Column(nullable = false, unique = true, name = "account_number")
    private String accountNumber;
    
    @NotBlank(message = "Account name is required")
    @Column(nullable = false, name = "account_name")
    private String accountName;
    
    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be positive")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;
    
    @Column(name = "account_type")
    private String accountType; // REGULAR, GOLD, PLATINUM
    
    @Column(nullable = false)
    private String status; // ACTIVE, INACTIVE, BLOCKED
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relasi Many-to-One dengan Customer
    // Banyak saving bisa dimiliki oleh satu customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore 
    private Customer customer;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "ACTIVE";
        }
        if (accountType == null) {
            accountType = "REGULAR";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
