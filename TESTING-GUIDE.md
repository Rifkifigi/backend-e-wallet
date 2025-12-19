# 🧪 Step-by-Step Testing Guide

Tutorial langkah demi langkah untuk testing aplikasi E-Wallet

---

## 📋 Prerequisites

1. Aplikasi sudah running di `http://localhost:8080`
2. Install cURL atau Postman
3. (Optional) jq untuk format JSON di terminal: `sudo apt install jq`

---

## 🎯 Skenario Testing: Customer dengan Multiple Savings

### Tujuan:
Membuat 1 customer dan menambahkan 3 savings, lalu mengambil data customer tersebut berdasarkan Government ID.

---

## 📝 Step 1: Create Customer

### Request:
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Andi Wijaya",
    "governmentId": "3301234567890123",
    "email": "andi.wijaya@example.com",
    "phoneNumber": "081234567890",
    "address": "Jl. Merdeka No. 45, Semarang"
  }'
```

### Expected Response:
```json
{
  "success": true,
  "message": "Customer created successfully",
  "data": {
    "id": 1,
    "name": "Andi Wijaya",
    "governmentId": "3301234567890123",
    "email": "andi.wijaya@example.com",
    "phoneNumber": "081234567890",
    "address": "Jl. Merdeka No. 45, Semarang",
    "createdAt": "2024-12-19T10:00:00.123456",
    "updatedAt": "2024-12-19T10:00:00.123456",
    "savings": []
  }
}
```

### ✅ Verification:
- Status code: 201 Created
- Response berisi customer ID (simpan untuk step berikutnya)
- Field `savings` masih kosong (array kosong)

---

## 📝 Step 2: Create Saving Account 1 (Tabungan Reguler)

### Request:
```bash
curl -X POST http://localhost:8080/api/savings \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "SAV001234567890",
    "accountName": "Tabungan Reguler Andi",
    "balance": 5000000,
    "accountType": "REGULAR",
    "customerId": 1
  }'
```

### Expected Response:
```json
{
  "success": true,
  "message": "Saving account created successfully",
  "data": {
    "id": 1,
    "accountNumber": "SAV001234567890",
    "accountName": "Tabungan Reguler Andi",
    "balance": 5000000.00,
    "accountType": "REGULAR",
    "status": "ACTIVE",
    "createdAt": "2024-12-19T10:05:00.123456",
    "updatedAt": "2024-12-19T10:05:00.123456"
  }
}
```

### ✅ Verification:
- Status code: 201 Created
- Balance: 5,000,000
- Status: ACTIVE (otomatis)
- Account Type: REGULAR

---

## 📝 Step 3: Create Saving Account 2 (Tabungan Haji)

### Request:
```bash
curl -X POST http://localhost:8080/api/savings \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "SAV001234567891",
    "accountName": "Tabungan Haji Andi",
    "balance": 15000000,
    "accountType": "GOLD",
    "customerId": 1
  }'
```

### Expected Response:
```json
{
  "success": true,
  "message": "Saving account created successfully",
  "data": {
    "id": 2,
    "accountNumber": "SAV001234567891",
    "accountName": "Tabungan Haji Andi",
    "balance": 15000000.00,
    "accountType": "GOLD",
    "status": "ACTIVE",
    "createdAt": "2024-12-19T10:10:00.123456",
    "updatedAt": "2024-12-19T10:10:00.123456"
  }
}
```

### ✅ Verification:
- Status code: 201 Created
- Balance: 15,000,000
- Account Type: GOLD
- Customer ID tetap: 1

---

## 📝 Step 4: Create Saving Account 3 (Tabungan Investasi)

### Request:
```bash
curl -X POST http://localhost:8080/api/savings \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "SAV001234567892",
    "accountName": "Tabungan Investasi Andi",
    "balance": 25000000,
    "accountType": "PLATINUM",
    "customerId": 1
  }'
```

### Expected Response:
```json
{
  "success": true,
  "message": "Saving account created successfully",
  "data": {
    "id": 3,
    "accountNumber": "SAV001234567892",
    "accountName": "Tabungan Investasi Andi",
    "balance": 25000000.00,
    "accountType": "PLATINUM",
    "status": "ACTIVE",
    "createdAt": "2024-12-19T10:15:00.123456",
    "updatedAt": "2024-12-19T10:15:00.123456"
  }
}
```

### ✅ Verification:
- Status code: 201 Created
- Balance: 25,000,000
- Account Type: PLATINUM
- Total saving accounts untuk customer 1: 3 buah

---

## 🌟 Step 5: Get Customer by Government ID (FITUR UTAMA)

### Request:
```bash
curl http://localhost:8080/api/customers/government-id/3301234567890123
```

### Expected Response:
```json
{
  "success": true,
  "message": "Customer found successfully",
  "data": {
    "customerId": 1,
    "name": "Andi Wijaya",
    "governmentId": "3301234567890123",
    "email": "andi.wijaya@example.com",
    "phoneNumber": "081234567890",
    "address": "Jl. Merdeka No. 45, Semarang",
    "createdAt": "2024-12-19T10:00:00.123456",
    "updatedAt": "2024-12-19T10:00:00.123456",
    "totalSavings": 3,
    "savings": [
      {
        "savingId": 1,
        "accountNumber": "SAV001234567890",
        "accountName": "Tabungan Reguler Andi",
        "balance": 5000000.00,
        "accountType": "REGULAR",
        "status": "ACTIVE",
        "createdAt": "2024-12-19T10:05:00.123456",
        "updatedAt": "2024-12-19T10:05:00.123456"
      },
      {
        "savingId": 2,
        "accountNumber": "SAV001234567891",
        "accountName": "Tabungan Haji Andi",
        "balance": 15000000.00,
        "accountType": "GOLD",
        "status": "ACTIVE",
        "createdAt": "2024-12-19T10:10:00.123456",
        "updatedAt": "2024-12-19T10:10:00.123456"
      },
      {
        "savingId": 3,
        "accountNumber": "SAV001234567892",
        "accountName": "Tabungan Investasi Andi",
        "balance": 25000000.00,
        "accountType": "PLATINUM",
        "status": "ACTIVE",
        "createdAt": "2024-12-19T10:15:00.123456",
        "updatedAt": "2024-12-19T10:15:00.123456"
      }
    ]
  }
}
```

### ✅ Verification:
- Status code: 200 OK
- **totalSavings: 3** ← Menampilkan jumlah savings
- **savings array berisi 3 objects** ← Menampilkan semua savings
- Total balance: 5M + 15M + 25M = **45 juta**
- Semua savings memiliki **customer_id = 1**

---

## 💰 Step 6: Deposit ke Saving Account

### Request:
```bash
curl -X POST "http://localhost:8080/api/savings/1/deposit?amount=2000000"
```

### Expected Response:
```json
{
  "success": true,
  "message": "Deposit successful",
  "data": {
    "id": 1,
    "accountNumber": "SAV001234567890",
    "accountName": "Tabungan Reguler Andi",
    "balance": 7000000.00,  // ← Dari 5M jadi 7M
    "accountType": "REGULAR",
    "status": "ACTIVE",
    "updatedAt": "2024-12-19T10:20:00.123456"
  }
}
```

### ✅ Verification:
- Balance berubah dari 5,000,000 → 7,000,000
- updatedAt berubah

---

## 💸 Step 7: Withdraw dari Saving Account

### Request:
```bash
curl -X POST "http://localhost:8080/api/savings/2/withdraw?amount=1000000"
```

### Expected Response:
```json
{
  "success": true,
  "message": "Withdrawal successful",
  "data": {
    "id": 2,
    "accountNumber": "SAV001234567891",
    "accountName": "Tabungan Haji Andi",
    "balance": 14000000.00,  // ← Dari 15M jadi 14M
    "accountType": "GOLD",
    "status": "ACTIVE",
    "updatedAt": "2024-12-19T10:25:00.123456"
  }
}
```

### ✅ Verification:
- Balance berubah dari 15,000,000 → 14,000,000

---

## 🔄 Step 8: Transfer Antar Saving Accounts

### Request:
```bash
curl -X POST "http://localhost:8080/api/savings/transfer?fromSavingId=3&toSavingId=1&amount=5000000"
```

### Expected Response:
```json
{
  "success": true,
  "message": "Transfer successful"
}
```

### ✅ Verification:
- Saving 3: 25M - 5M = 20M
- Saving 1: 7M + 5M = 12M

### Verify dengan Get Savings:
```bash
curl http://localhost:8080/api/savings/1
curl http://localhost:8080/api/savings/3
```

---

## 🔍 Step 9: Verify Final State

### Request:
```bash
curl http://localhost:8080/api/customers/government-id/3301234567890123
```

### Expected Response:
```json
{
  "success": true,
  "data": {
    "customerId": 1,
    "name": "Andi Wijaya",
    "governmentId": "3301234567890123",
    "totalSavings": 3,
    "savings": [
      {
        "savingId": 1,
        "accountName": "Tabungan Reguler Andi",
        "balance": 12000000.00  // ← Updated: 5M → 7M → 12M
      },
      {
        "savingId": 2,
        "accountName": "Tabungan Haji Andi",
        "balance": 14000000.00  // ← Updated: 15M → 14M
      },
      {
        "savingId": 3,
        "accountName": "Tabungan Investasi Andi",
        "balance": 20000000.00  // ← Updated: 25M → 20M
      }
    ]
  }
}
```

### ✅ Final Verification:
- Total balance: 12M + 14M + 20M = **46 juta**
- Semua transaksi tercatat dengan benar
- Customer tetap memiliki 3 savings

---

## 🧪 Additional Test Cases

### Test Case 1: Get Customer yang Tidak Ada
```bash
curl http://localhost:8080/api/customers/government-id/9999999999999999
```

**Expected:**
```json
{
  "success": false,
  "message": "Customer with Government ID 9999999999999999 not found"
}
```

### Test Case 2: Create Saving dengan Customer ID Invalid
```bash
curl -X POST http://localhost:8080/api/savings \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "TEST123",
    "accountName": "Test",
    "balance": 1000000,
    "customerId": 999
  }'
```

**Expected:**
```json
{
  "success": false,
  "message": "Customer with ID 999 not found"
}
```

### Test Case 3: Create Saving dengan Account Number yang Sudah Ada
```bash
curl -X POST http://localhost:8080/api/savings \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "SAV001234567890",
    "accountName": "Duplicate",
    "balance": 1000000,
    "customerId": 1
  }'
```

**Expected:**
```json
{
  "success": false,
  "message": "Account number SAV001234567890 already exists"
}
```

### Test Case 4: Withdraw dengan Saldo Tidak Cukup
```bash
curl -X POST "http://localhost:8080/api/savings/1/withdraw?amount=100000000"
```

**Expected:**
```json
{
  "success": false,
  "message": "Insufficient balance"
}
```

---

## 📊 Verify di H2 Console

1. Buka: http://localhost:8080/h2-console
2. Login (JDBC URL: `jdbc:h2:mem:ewalletdb`, User: `sa`, Password: kosong)
3. Run query:

```sql
-- Lihat semua customers
SELECT * FROM customers;

-- Lihat semua savings
SELECT * FROM savings;

-- Lihat customer dengan savings (JOIN)
SELECT 
    c.name AS customer_name,
    c.government_id,
    s.account_number,
    s.account_name,
    s.balance,
    s.account_type
FROM customers c
LEFT JOIN savings s ON c.id = s.customer_id
WHERE c.government_id = '3301234567890123'
ORDER BY s.id;

-- Hitung total balance per customer
SELECT 
    c.name,
    COUNT(s.id) AS total_savings,
    SUM(s.balance) AS total_balance
FROM customers c
LEFT JOIN savings s ON c.id = s.customer_id
GROUP BY c.id, c.name;
```

---

## 📋 Testing Checklist

- [ ] Customer dapat dibuat dengan data valid
- [ ] Customer tidak dapat dibuat dengan Government ID duplikat
- [ ] Saving dapat dibuat dengan customer_id yang valid
- [ ] Saving tidak dapat dibuat dengan customer_id yang invalid
- [ ] Saving tidak dapat dibuat dengan account_number duplikat
- [ ] **Get Customer by Government ID menampilkan SEMUA savings** ⭐
- [ ] Deposit berhasil menambah saldo
- [ ] Withdraw berhasil mengurangi saldo
- [ ] Withdraw gagal jika saldo tidak cukup
- [ ] Transfer berhasil memindahkan saldo antar savings
- [ ] Semua timestamp (createdAt, updatedAt) tercatat dengan benar

---

## 💡 Tips Testing

1. **Gunakan jq untuk format JSON**:
   ```bash
   curl http://localhost:8080/api/customers/government-id/3301234567890123 | jq '.'
   ```

2. **Save Response ke File**:
   ```bash
   curl http://localhost:8080/api/customers/government-id/3301234567890123 > customer.json
   ```

3. **Test dengan Postman**:
   - Import file `E-Wallet-API.postman_collection.json`
   - Bisa save response dan environment variables

4. **Automated Testing**:
   ```bash
   ./test-api.sh
   ```

---

**Happy Testing! 🚀**
