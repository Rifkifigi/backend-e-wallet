package com.ewallet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerDTO {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Government ID is required")
    private String governmentId;
    
    @Email(message = "Email should be valid")
    private String email;
    
    private String phoneNumber;
    
    @NotBlank(message = "Address is required")
    private String address;
}
