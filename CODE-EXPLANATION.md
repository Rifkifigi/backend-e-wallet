# 📖 Penjelasan Kode - Relasi Customer & Saving

## 🎯 Konsep Relasi One-to-Many

Relasi One-to-Many berarti:
- **Satu Customer** dapat memiliki **banyak Saving Account**
- **Satu Saving Account** hanya dimiliki oleh **satu Customer**

## 🔗 Implementasi di Entity

### 1. Entity Customer (Sisi "One")

```java
@Entity
@Table(name = "customers")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String governmentId;
    
    // 🔥 INI YANG PENTING: Relasi One-to-Many
    // Satu Customer memiliki banyak Saving
    @OneToMany(
        mappedBy = "customer",        // Merujuk ke field 'customer' di entity Saving
        cascade = CascadeType.ALL,     // Ketika Customer dihapus, semua Saving juga terhapus
        fetch = FetchType.LAZY         // Lazy loading untuk performance
    )
    private List<Saving> savings = new ArrayList<>();
    
    // Getter, Setter, Constructor...
}
```

**Penjelasan:**
- `@OneToMany` = Menandakan relasi One-to-Many
- `mappedBy = "customer"` = Field ini tidak menyimpan foreign key, tapi merujuk ke field `customer` di entity Saving
- `cascade = CascadeType.ALL` = Operasi pada Customer akan cascade ke Saving (misal: delete Customer → delete semua Saving)
- `fetch = FetchType.LAZY` = Data Saving tidak langsung di-load, hanya saat dibutuhkan
- `List<Saving>` = Satu Customer bisa punya banyak Saving, jadi pakai List

### 2. Entity Saving (Sisi "Many")

```java
@Entity
@Table(name = "savings")
public class Saving {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String accountNumber;
    private String accountName;
    private BigDecimal balance;
    
    // 🔥 INI YANG PENTING: Relasi Many-to-One
    // Banyak Saving dimiliki oleh satu Customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "customer_id",          // Nama kolom foreign key di database
        nullable = false               // Wajib diisi
    )
    @JsonIgnore                        // Mencegah infinite loop saat serialize JSON
    private Customer customer;
    
    // Getter, Setter, Constructor...
}
```

**Penjelasan:**
- `@ManyToOne` = Menandakan relasi Many-to-One
- `@JoinColumn(name = "customer_id")` = Kolom `customer_id` di table `savings` adalah foreign key
- `nullable = false` = Setiap Saving HARUS memiliki Customer
- `@JsonIgnore` = Saat convert ke JSON, field `customer` tidak disertakan (mencegah infinite loop)
- `Customer customer` = Reference ke Customer yang memiliki Saving ini

## 🎨 Diagram Relasi

```
CUSTOMER (id: 1, name: "Budi")
    ↓ ONE
    ↓
    ↓ HAS MANY
    ↓
SAVING (id: 1, customer_id: 1, accountName: "Tabungan Utama")
SAVING (id: 2, customer_id: 1, accountName: "Tabungan Haji")
SAVING (id: 3, customer_id: 1, accountName: "Tabungan Pendidikan")
```

## 🔍 Repository dengan JOIN FETCH

### CustomerRepository

```java
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // Query biasa - akan trigger N+1 query problem
    Optional<Customer> findByGovernmentId(String governmentId);
    
    // 🔥 Query dengan JOIN FETCH - Lebih efisien!
    // Mengambil Customer dan Savings dalam SATU query
    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.savings WHERE c.governmentId = :governmentId")
    Optional<Customer> findByGovernmentIdWithSavings(@Param("governmentId") String governmentId);
}
```

**Penjelasan:**
- `LEFT JOIN FETCH c.savings` = Ambil Customer beserta Savings-nya dalam satu query
- Menghindari **N+1 Query Problem**:
  - Tanpa FETCH: 1 query untuk Customer + N query untuk Savings (1+N = buruk!)
  - Dengan FETCH: 1 query untuk semuanya (1 = bagus!)

## 🛠️ Service Layer

### CustomerService

```java
@Service
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    // 🔥 Method untuk get Customer by Government ID dengan semua Savings
    public CustomerWithSavingsDTO getCustomerByGovernmentId(String governmentId) {
        
        // 1. Cari Customer dengan JOIN FETCH (dapat Customer + Savings sekaligus)
        Customer customer = customerRepository.findByGovernmentIdWithSavings(governmentId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        // 2. Convert ke DTO
        return convertToCustomerWithSavingsDTO(customer);
    }
    
    private CustomerWithSavingsDTO convertToCustomerWithSavingsDTO(Customer customer) {
        // Convert list Savings ke list SavingDTO
        List<SavingDTO> savingDTOs = customer.getSavings().stream()
                .map(this::convertToSavingDTO)
                .collect(Collectors.toList());
        
        // Buat DTO Customer dengan Savings
        CustomerWithSavingsDTO dto = new CustomerWithSavingsDTO();
        dto.setCustomerId(customer.getId());
        dto.setName(customer.getName());
        dto.setGovernmentId(customer.getGovernmentId());
        dto.setSavings(savingDTOs);
        dto.setTotalSavings(savingDTOs.size()); // Jumlah savings
        
        return dto;
    }
}
```

## 📡 Controller

### CustomerController

```java
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    
    private final CustomerService customerService;
    
    // 🔥 API Endpoint untuk get Customer by Government ID
    @GetMapping("/government-id/{governmentId}")
    public ResponseEntity<Map<String, Object>> getCustomerByGovernmentId(
            @PathVariable String governmentId) {
        
        try {
            // Panggil service
            CustomerWithSavingsDTO customer = customerService.getCustomerByGovernmentId(governmentId);
            
            // Buat response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Customer found successfully");
            response.put("data", customer); // DTO yang berisi Customer + List<Saving>
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
```

## 🎯 Flow Request ke Response

### 1. User Request
```bash
GET /api/customers/government-id/3327012345678901
```

### 2. Controller Terima Request
```java
@GetMapping("/government-id/{governmentId}")
public ResponseEntity getCustomerByGovernmentId(@PathVariable String governmentId)
```

### 3. Controller Panggil Service
```java
CustomerWithSavingsDTO customer = customerService.getCustomerByGovernmentId(governmentId);
```

### 4. Service Query Database (dengan JOIN FETCH)
```sql
SELECT c.*, s.* 
FROM customers c 
LEFT JOIN savings s ON c.id = s.customer_id 
WHERE c.government_id = '3327012345678901'
```

### 5. Service Convert ke DTO
```java
// Entity Customer + List<Saving> → CustomerWithSavingsDTO
dto.setSavings(savingDTOs);
dto.setTotalSavings(savingDTOs.size());
```

### 6. Controller Return Response
```json
{
  "success": true,
  "data": {
    "customerId": 1,
    "name": "Budi Santoso",
    "totalSavings": 3,
    "savings": [...]
  }
}
```

## 🚫 Menghindari Infinite Loop JSON

### Masalah:
```
Customer → Saving → Customer → Saving → ... (infinite loop!)
```

### Solusi 1: @JsonIgnore di Entity
```java
@Entity
public class Saving {
    @ManyToOne
    @JsonIgnore  // ← Ini mencegah serialize field 'customer'
    private Customer customer;
}
```

### Solusi 2: Menggunakan DTO (Recommended)
```java
// Tidak langsung return Entity, tapi DTO yang sudah di-design
public class CustomerWithSavingsDTO {
    private Long customerId;
    private String name;
    private List<SavingDTO> savings; // ← Tidak ada reference balik ke Customer
}

public class SavingDTO {
    private Long savingId;
    private String accountNumber;
    private BigDecimal balance;
    // ← Tidak ada field Customer
}
```

## 💾 SQL yang Dihasilkan

### Tanpa JOIN FETCH (N+1 Problem)
```sql
-- Query 1: Ambil Customer
SELECT * FROM customers WHERE government_id = '3327012345678901';

-- Query 2: Ambil Saving 1
SELECT * FROM savings WHERE customer_id = 1;

-- Query 3: Ambil Saving 2
SELECT * FROM savings WHERE customer_id = 1;

-- Query 4: Ambil Saving 3
SELECT * FROM savings WHERE customer_id = 1;

-- Total: 1 + 3 = 4 queries (BURUK!)
```

### Dengan JOIN FETCH (Optimal)
```sql
-- Query 1: Ambil Customer + semua Savings sekaligus
SELECT c.*, s.* 
FROM customers c 
LEFT JOIN savings s ON c.id = s.customer_id 
WHERE c.government_id = '3327012345678901';

-- Total: 1 query saja (BAGUS!)
```

## 🎓 Key Takeaways

1. **@OneToMany** di Customer = Satu Customer punya banyak Saving
2. **@ManyToOne** di Saving = Banyak Saving punya satu Customer
3. **mappedBy** = Menunjukkan field mana yang jadi "owner" relasi
4. **JOIN FETCH** = Optimasi query untuk ambil data relasi sekaligus
5. **DTO Pattern** = Mencegah infinite loop dan kontrol response JSON
6. **@JsonIgnore** = Alternatif untuk break infinite loop

## 📚 Referensi

- JPA Documentation: https://docs.oracle.com/javaee/7/api/javax/persistence/OneToMany.html
- Spring Data JPA: https://spring.io/projects/spring-data-jpa
- Hibernate Best Practices: https://vladmihalcea.com/

---

**Semoga membantu pemahaman tentang relasi One-to-Many! 🚀**
