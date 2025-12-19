# E-Wallet / Tabungan Mini Application

Aplikasi E-Wallet dan Tabungan mini berbasis Java Spring Boot dengan relasi Customer dan Saving.

## 📋 Fitur

- ✅ Manajemen Customer (Nasabah)
- ✅ Manajemen Saving Account (Rekening Tabungan)
- ✅ Relasi One-to-Many: 1 Customer dapat memiliki banyak Saving
- ✅ API untuk mendapatkan Customer berdasarkan Government ID (KTP/NIK)
- ✅ Menampilkan lebih dari 1 saving per customer
- ✅ Deposit, Withdraw, dan Transfer
- ✅ Validasi dan Error Handling

## 🏗️ Struktur Database

### Table: customers
```sql
- id (Long, Primary Key, Auto Increment)
- name (String, Not Null)
- government_id (String, Unique, Not Null) -- KTP/NIK
- email (String, Unique)
- phone_number (String)
- address (String, Not Null)
- created_at (DateTime)
- updated_at (DateTime)
```

### Table: savings
```sql
- id (Long, Primary Key, Auto Increment)
- account_number (String, Unique, Not Null)
- account_name (String, Not Null)
- balance (BigDecimal, Not Null)
- account_type (String) -- REGULAR, GOLD, PLATINUM
- status (String) -- ACTIVE, INACTIVE, BLOCKED
- customer_id (Long, Foreign Key -> customers.id)
- created_at (DateTime)
- updated_at (DateTime)
```

### Relasi
- **One-to-Many**: 1 Customer dapat memiliki banyak Saving
- **Many-to-One**: Banyak Saving dimiliki oleh 1 Customer

## 🚀 Cara Menjalankan

### Prerequisites
- Java 17 atau lebih tinggi
- Maven 3.6+

### Langkah-langkah

1. **Clone atau extract project**
```bash
cd ewallet-app
```

2. **Build project**
```bash
mvn clean install
```

3. **Run application**
```bash
mvn spring-boot:run
```

4. **Aplikasi akan berjalan di:**
   - API: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:ewalletdb`
     - Username: `sa`
     - Password: (kosongkan)

## 📡 API Endpoints

### Customer APIs

#### 1. **Get Customer by Government ID** ⭐ (API BARU)
Mendapatkan customer berdasarkan Government ID (KTP/NIK) dan menampilkan semua saving yang dimiliki.

**Endpoint:** `GET /api/customers/government-id/{governmentId}`

**Example Request:**
```bash
curl http://localhost:8080/api/customers/government-id/3327012345678901
```

**Example Response:**
```json
{
  "success": true,
  "message": "Customer found successfully",
  "data": {
    "customerId": 1,
    "name": "Budi Santoso",
    "governmentId": "3327012345678901",
    "email": "budi@example.com",
    "phoneNumber": "081234567890",
    "address": "Jl. Sudirman No. 123, Jakarta",
    "createdAt": "2024-12-19T10:00:00",
    "updatedAt": "2024-12-19T10:00:00",
    "totalSavings": 3,
    "savings": [
      {
        "savingId": 1,
        "accountNumber": "1234567890",
        "accountName": "Tabungan Utama",
        "balance": 5000000.00,
        "accountType": "REGULAR",
        "status": "ACTIVE",
        "createdAt": "2024-12-19T10:05:00",
        "updatedAt": "2024-12-19T10:05:00"
      },
      {
        "savingId": 2,
        "accountNumber": "1234567891",
        "accountName": "Tabungan Haji",
        "balance": 10000000.00,
        "accountType": "GOLD",
        "status": "ACTIVE",
        "createdAt": "2024-12-19T10:10:00",
        "updatedAt": "2024-12-19T10:10:00"
      },
      {
        "savingId": 3,
        "accountNumber": "1234567892",
        "accountName": "Tabungan Pendidikan",
        "balance": 15000000.00,
        "accountType": "PLATINUM",
        "status": "ACTIVE",
        "createdAt": "2024-12-19T10:15:00",
        "updatedAt": "2024-12-19T10:15:00"
      }
    ]
  }
}
```

#### 2. Create Customer
**Endpoint:** `POST /api/customers`

**Request Body:**
```json
{
  "name": "Budi Santoso",
  "governmentId": "3327012345678901",
  "email": "budi@example.com",
  "phoneNumber": "081234567890",
  "address": "Jl. Sudirman No. 123, Jakarta"
}
```

#### 3. Get All Customers
**Endpoint:** `GET /api/customers`

#### 4. Get Customer by ID
**Endpoint:** `GET /api/customers/{id}`

### Saving APIs

#### 1. Create Saving
**Endpoint:** `POST /api/savings`

**Request Body:**
```json
{
  "accountNumber": "1234567890",
  "accountName": "Tabungan Utama",
  "balance": 5000000,
  "accountType": "REGULAR",
  "customerId": 1
}
```

#### 2. Get All Savings
**Endpoint:** `GET /api/savings`

#### 3. Get Saving by ID
**Endpoint:** `GET /api/savings/{id}`

#### 4. Get Saving by Account Number
**Endpoint:** `GET /api/savings/account/{accountNumber}`

#### 5. Get Savings by Customer ID
**Endpoint:** `GET /api/savings/customer/{customerId}`

#### 6. Deposit
**Endpoint:** `POST /api/savings/{id}/deposit?amount=1000000`

#### 7. Withdraw
**Endpoint:** `POST /api/savings/{id}/withdraw?amount=500000`

#### 8. Transfer
**Endpoint:** `POST /api/savings/transfer?fromSavingId=1&toSavingId=2&amount=1000000`

## 🧪 Testing dengan cURL

### Test 1: Create Customer
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Budi Santoso",
    "governmentId": "3327012345678901",
    "email": "budi@example.com",
    "phoneNumber": "081234567890",
    "address": "Jl. Sudirman No. 123, Jakarta"
  }'
```

### Test 2: Create Multiple Savings for Customer
```bash
# Saving 1
curl -X POST http://localhost:8080/api/savings \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "1234567890",
    "accountName": "Tabungan Utama",
    "balance": 5000000,
    "accountType": "REGULAR",
    "customerId": 1
  }'

# Saving 2
curl -X POST http://localhost:8080/api/savings \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "1234567891",
    "accountName": "Tabungan Haji",
    "balance": 10000000,
    "accountType": "GOLD",
    "customerId": 1
  }'

# Saving 3
curl -X POST http://localhost:8080/api/savings \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "1234567892",
    "accountName": "Tabungan Pendidikan",
    "balance": 15000000,
    "accountType": "PLATINUM",
    "customerId": 1
  }'
```

### Test 3: Get Customer by Government ID (dengan semua savings)
```bash
curl http://localhost:8080/api/customers/government-id/3327012345678901
```

### Test 4: Deposit
```bash
curl -X POST "http://localhost:8080/api/savings/1/deposit?amount=1000000"
```

### Test 5: Withdraw
```bash
curl -X POST "http://localhost:8080/api/savings/1/withdraw?amount=500000"
```

### Test 6: Transfer
```bash
curl -X POST "http://localhost:8080/api/savings/transfer?fromSavingId=1&toSavingId=2&amount=1000000"
```

## 🗂️ Struktur Project

```
ewallet-app/
├── src/
│   └── main/
│       ├── java/com/ewallet/
│       │   ├── model/
│       │   │   ├── Customer.java          # Entity Customer
│       │   │   └── Saving.java            # Entity Saving
│       │   ├── repository/
│       │   │   ├── CustomerRepository.java
│       │   │   └── SavingRepository.java
│       │   ├── service/
│       │   │   ├── CustomerService.java
│       │   │   └── SavingService.java
│       │   ├── controller/
│       │   │   ├── CustomerController.java
│       │   │   └── SavingController.java
│       │   ├── dto/
│       │   │   ├── CustomerWithSavingsDTO.java
│       │   │   ├── SavingDTO.java
│       │   │   ├── CreateCustomerDTO.java
│       │   │   └── CreateSavingDTO.java
│       │   └── EWalletApplication.java    # Main Application
│       └── resources/
│           └── application.properties      # Configuration
└── pom.xml                                 # Maven Dependencies
```

## 🎯 Fitur Utama yang Ditambahkan

### 1. **Relasi One-to-Many**
   - Satu Customer dapat memiliki banyak Saving Account
   - Menggunakan annotation `@OneToMany` di Customer
   - Menggunakan annotation `@ManyToOne` di Saving

### 2. **API Get Customer by Government ID** ⭐
   - Endpoint: `GET /api/customers/government-id/{governmentId}`
   - Menampilkan informasi customer lengkap
   - Menampilkan SEMUA saving account yang dimiliki customer
   - Menampilkan total jumlah saving account
   - Menggunakan JOIN FETCH untuk optimasi query

### 3. **DTO untuk Response**
   - `CustomerWithSavingsDTO`: Response customer dengan list savings
   - `SavingDTO`: Response detail saving
   - Menghindari infinite loop dengan `@JsonIgnore`

## 💡 Tips

1. **Akses H2 Console** untuk melihat data di database:
   - URL: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:ewalletdb`
   - Username: `sa`
   - Password: (kosongkan)

2. **Testing API** bisa menggunakan:
   - cURL (sudah ada contoh di atas)
   - Postman
   - Browser (untuk GET requests)

3. **Lihat Query SQL** yang dijalankan di console/log aplikasi

## 📝 Notes

- Database menggunakan H2 in-memory, data akan hilang ketika aplikasi di-stop
- Untuk production, ganti ke database persistent (PostgreSQL, MySQL, dll)
- Semua endpoint menggunakan JSON format
- Validasi otomatis untuk semua input
- Error handling sudah diimplementasikan

## 🔧 Teknologi yang Digunakan

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database
- Lombok
- Maven

## 📧 Author

Dibuat untuk memenuhi requirement project e-wallet/tabungan mini dengan fitur:
- Relasi 1 Customer ke banyak Saving
- API get customer by Government ID
- Menampilkan lebih dari 1 saving per customer

---

**Happy Coding! 🚀**
