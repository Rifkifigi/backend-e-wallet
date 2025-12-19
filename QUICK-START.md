# 🚀 Quick Start Guide - E-Wallet Application

## Cara Cepat Menjalankan Aplikasi

### 1️⃣ Prasyarat
```bash
# Pastikan sudah install:
- Java 17+
- Maven 3.6+
```

### 2️⃣ Jalankan Aplikasi
```bash
cd ewallet-app
mvn spring-boot:run
```

### 3️⃣ Akses Aplikasi
- **API**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:ewalletdb`
  - Username: `sa`
  - Password: (kosongkan)

---

## 🎯 API Utama (Yang Diminta)

### Get Customer by Government ID + Semua Savings ⭐

**Endpoint:**
```
GET /api/customers/government-id/{governmentId}
```

**Contoh Request:**
```bash
curl http://localhost:8080/api/customers/government-id/3327012345678901
```

**Response:**
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
    "totalSavings": 4,
    "savings": [
      {
        "savingId": 1,
        "accountNumber": "1234567890",
        "accountName": "Tabungan Utama",
        "balance": 5000000.00,
        "accountType": "REGULAR",
        "status": "ACTIVE"
      },
      {
        "savingId": 2,
        "accountNumber": "1234567891",
        "accountName": "Tabungan Haji",
        "balance": 10000000.00,
        "accountType": "GOLD",
        "status": "ACTIVE"
      },
      ...
    ]
  }
}
```

---

## 🧪 Testing Cepat

### Opsi 1: Menggunakan Script (Recommended)
```bash
chmod +x test-api.sh
./test-api.sh
```

### Opsi 2: Manual dengan cURL

#### Step 1: Buat Customer
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "governmentId": "1234567890123456",
    "email": "john@example.com",
    "phoneNumber": "081234567890",
    "address": "Jl. Example No. 1"
  }'
```

#### Step 2: Buat Saving 1
```bash
curl -X POST http://localhost:8080/api/savings \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "ACC001",
    "accountName": "Tabungan 1",
    "balance": 1000000,
    "accountType": "REGULAR",
    "customerId": 1
  }'
```

#### Step 3: Buat Saving 2
```bash
curl -X POST http://localhost:8080/api/savings \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "ACC002",
    "accountName": "Tabungan 2",
    "balance": 2000000,
    "accountType": "GOLD",
    "customerId": 1
  }'
```

#### Step 4: Get Customer dengan Semua Savings
```bash
curl http://localhost:8080/api/customers/government-id/1234567890123456
```

### Opsi 3: Menggunakan Postman
1. Import file `E-Wallet-API.postman_collection.json` ke Postman
2. Test semua API yang tersedia

---

## 📊 Initial Data

Aplikasi sudah include sample data di `data.sql`:
- **3 Customers** dengan Government ID berbeda
- **7 Saving Accounts** total
- Customer 1 memiliki **4 savings**
- Customer 2 memiliki **2 savings**
- Customer 3 memiliki **1 saving**

Government IDs yang bisa langsung dicoba:
- `3327012345678901` (Budi Santoso - 4 savings)
- `3328012345678902` (Siti Nurhaliza - 2 savings)
- `3329012345678903` (Ahmad Dahlan - 1 saving)

---

## 🔍 Lihat Data di Database

1. Buka: http://localhost:8080/h2-console
2. Login dengan:
   - JDBC URL: `jdbc:h2:mem:ewalletdb`
   - Username: `sa`
   - Password: (kosongkan)
3. Run query:
   ```sql
   SELECT * FROM customers;
   SELECT * FROM savings;
   
   -- Lihat customer dengan savings
   SELECT c.name, c.government_id, s.account_number, s.account_name, s.balance
   FROM customers c
   LEFT JOIN savings s ON c.id = s.customer_id
   ORDER BY c.id, s.id;
   ```

---

## 📝 API Endpoints Lainnya

### Customer APIs
- `POST /api/customers` - Create customer
- `GET /api/customers` - Get all customers
- `GET /api/customers/{id}` - Get customer by ID
- `GET /api/customers/government-id/{governmentId}` - Get by Gov ID ⭐

### Saving APIs
- `POST /api/savings` - Create saving
- `GET /api/savings` - Get all savings
- `GET /api/savings/{id}` - Get saving by ID
- `GET /api/savings/account/{accountNumber}` - Get by account number
- `GET /api/savings/customer/{customerId}` - Get savings by customer ID
- `POST /api/savings/{id}/deposit?amount=X` - Deposit
- `POST /api/savings/{id}/withdraw?amount=X` - Withdraw
- `POST /api/savings/transfer?fromSavingId=X&toSavingId=Y&amount=Z` - Transfer

---

## 🎓 Fitur yang Sudah Diimplementasikan

✅ **Relasi Database One-to-Many**
  - 1 Customer → Many Savings
  - Menggunakan JPA Annotations (`@OneToMany`, `@ManyToOne`)

✅ **API Get Customer by Government ID**
  - Menampilkan customer lengkap
  - Menampilkan **SEMUA** saving account yang dimiliki
  - Optimasi dengan JOIN FETCH (menghindari N+1 query problem)

✅ **DTO Pattern**
  - Request DTO untuk input
  - Response DTO untuk output
  - Menghindari infinite loop JSON

✅ **CRUD Operations**
  - Create, Read untuk Customer dan Saving
  - Deposit, Withdraw, Transfer untuk Saving

✅ **Validasi**
  - Input validation dengan `@Valid`
  - Business logic validation
  - Error handling dengan response yang jelas

✅ **Sample Data**
  - Data awal untuk testing
  - Multiple savings per customer

---

## 📚 Dokumentasi Lengkap

- **README.md** - Dokumentasi lengkap
- **DATABASE-SCHEMA.md** - Diagram dan penjelasan database
- **test-api.sh** - Script testing otomatis
- **E-Wallet-API.postman_collection.json** - Postman collection

---

## 💡 Tips

1. **Cek Log Aplikasi** untuk melihat SQL query yang dijalankan
2. **Gunakan H2 Console** untuk inspect data di database
3. **Test dengan Postman** untuk UI yang lebih user-friendly
4. **Jalankan test-api.sh** untuk automated testing

---

## 🐛 Troubleshooting

### Port 8080 sudah digunakan?
Edit `application.properties` dan ubah:
```properties
server.port=8081
```

### Maven build error?
```bash
mvn clean install -U
```

### Data tidak muncul?
Pastikan file `data.sql` ada di `src/main/resources/`

---

**Happy Coding! 🚀**
