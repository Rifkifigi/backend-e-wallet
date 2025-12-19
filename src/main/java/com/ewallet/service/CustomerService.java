package com.ewallet.service;

import com.ewallet.dto.CreateCustomerDTO;
import com.ewallet.dto.CustomerWithSavingsDTO;
import com.ewallet.dto.SavingDTO;
import com.ewallet.model.Customer;
import com.ewallet.model.Saving;
import com.ewallet.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    /**
     * Mencari customer berdasarkan Government ID dan menampilkan semua saving yang dimiliki
     */
    @Transactional(readOnly = true)
    public CustomerWithSavingsDTO getCustomerByGovernmentId(String governmentId) {
        Customer customer = customerRepository.findByGovernmentIdWithSavings(governmentId)
                .orElseThrow(() -> new RuntimeException("Customer with Government ID " + governmentId + " not found"));
        
        return convertToCustomerWithSavingsDTO(customer);
    }
    
    /**
     * Membuat customer baru
     */
    @Transactional
    public Customer createCustomer(CreateCustomerDTO dto) {
        // Validasi apakah Government ID sudah terdaftar
        if (customerRepository.existsByGovernmentId(dto.getGovernmentId())) {
            throw new RuntimeException("Customer with Government ID " + dto.getGovernmentId() + " already exists");
        }
        
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setGovernmentId(dto.getGovernmentId());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setAddress(dto.getAddress());
        
        return customerRepository.save(customer);
    }
    
    /**
     * Mendapatkan semua customer
     */
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    /**
     * Mendapatkan customer berdasarkan ID
     */
    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer with ID " + id + " not found"));
    }
    
    /**
     * Converter dari Customer entity ke CustomerWithSavingsDTO
     */
    private CustomerWithSavingsDTO convertToCustomerWithSavingsDTO(Customer customer) {
        List<SavingDTO> savingDTOs = customer.getSavings().stream()
                .map(this::convertToSavingDTO)
                .collect(Collectors.toList());
        
        CustomerWithSavingsDTO dto = new CustomerWithSavingsDTO();
        dto.setCustomerId(customer.getId());
        dto.setName(customer.getName());
        dto.setGovernmentId(customer.getGovernmentId());
        dto.setEmail(customer.getEmail());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setAddress(customer.getAddress());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());
        dto.setSavings(savingDTOs);
        dto.setTotalSavings(savingDTOs.size());
        
        return dto;
    }
    
    /**
     * Converter dari Saving entity ke SavingDTO
     */
    private SavingDTO convertToSavingDTO(Saving saving) {
        SavingDTO dto = new SavingDTO();
        dto.setSavingId(saving.getId());
        dto.setAccountNumber(saving.getAccountNumber());
        dto.setAccountName(saving.getAccountName());
        dto.setBalance(saving.getBalance());
        dto.setAccountType(saving.getAccountType());
        dto.setStatus(saving.getStatus());
        dto.setCreatedAt(saving.getCreatedAt());
        dto.setUpdatedAt(saving.getUpdatedAt());
        
        return dto;
    }
}
