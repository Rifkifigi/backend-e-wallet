# Database Schema Diagram

## Relasi One-to-Many antara Customer dan Saving

```
┌─────────────────────────────────────┐
│          CUSTOMERS TABLE            │
├─────────────────────────────────────┤
│ 🔑 id (PK)                          │
│ 📝 name                             │
│ 🆔 government_id (UNIQUE)           │
│ 📧 email (UNIQUE)                   │
│ 📱 phone_number                     │
│ 🏠 address                          │
│ 📅 created_at                       │
│ 📅 updated_at                       │
└─────────────────────────────────────┘
         │
         │ 1
         │
         │ Has Many
         │
         │ *
         ▼
┌─────────────────────────────────────┐
│           SAVINGS TABLE             │
├─────────────────────────────────────┤
│ 🔑 id (PK)                          │
│ 🔢 account_number (UNIQUE)          │
│ 📝 account_name                     │
│ 💰 balance                          │
│ 🏷️  account_type                    │
│    (REGULAR/GOLD/PLATINUM)          │
│ 🔄 status                           │
│    (ACTIVE/INACTIVE/BLOCKED)        │
│ 🔗 customer_id (FK) ───────────┐   │
│ 📅 created_at                   │   │
│ 📅 updated_at                   │   │
└────────────────────────────┬────┘   │
                             │        │
                             └────────┘
                              Belongs To
```

## Penjelasan Relasi

### One-to-Many (1:N)
- **Satu Customer** dapat memiliki **banyak Saving Account**
- **Satu Saving Account** hanya dimiliki oleh **satu Customer**

### Foreign Key
- `customer_id` di table `savings` adalah Foreign Key yang merujuk ke `id` di table `customers`

### Cascade
- Ketika Customer dihapus, semua Saving Account miliknya juga akan terhapus (CASCADE)

## Contoh Data

### Customer 1: Budi Santoso (Government ID: 3327012345678901)
```
├── Saving 1: Tabungan Utama      (Rp  5,000,000) [REGULAR]
├── Saving 2: Tabungan Haji       (Rp 10,000,000) [GOLD]
├── Saving 3: Tabungan Pendidikan (Rp 15,000,000) [PLATINUM]
└── Saving 4: Tabungan Investasi  (Rp 20,000,000) [PLATINUM]
    Total Savings: 4 accounts
    Total Balance: Rp 50,000,000
```

### Customer 2: Siti Nurhaliza (Government ID: 3328012345678902)
```
├── Saving 1: Tabungan Primer     (Rp  8,000,000) [GOLD]
└── Saving 2: Tabungan Sekunder   (Rp 12,000,000) [PLATINUM]
    Total Savings: 2 accounts
    Total Balance: Rp 20,000,000
```

### Customer 3: Ahmad Dahlan (Government ID: 3329012345678903)
```
└── Saving 1: Tabungan Bisnis     (Rp 25,000,000) [PLATINUM]
    Total Savings: 1 account
    Total Balance: Rp 25,000,000
```

## API Response Example

### GET /api/customers/government-id/3327012345678901

Response akan menampilkan Customer beserta **SEMUA** Saving Account yang dimiliki:

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
      {
        "savingId": 3,
        "accountNumber": "1234567892",
        "accountName": "Tabungan Pendidikan",
        "balance": 15000000.00,
        "accountType": "PLATINUM",
        "status": "ACTIVE"
      },
      {
        "savingId": 4,
        "accountNumber": "1234567893",
        "accountName": "Tabungan Investasi",
        "balance": 20000000.00,
        "accountType": "PLATINUM",
        "status": "ACTIVE"
      }
    ]
  }
}
```

## SQL Query yang Digunakan

Untuk mendapatkan Customer dengan semua Savings, digunakan JOIN FETCH:

```sql
SELECT c FROM Customer c 
LEFT JOIN FETCH c.savings 
WHERE c.governmentId = :governmentId
```

Query ini mengoptimasi performa dengan mengambil data Customer dan Savings dalam satu query (menghindari N+1 problem).
