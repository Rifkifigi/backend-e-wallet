#!/bin/bash

# Script untuk testing E-Wallet API
# Author: E-Wallet Team

BASE_URL="http://localhost:8080"

echo "======================================"
echo "🧪 E-Wallet API Testing Script"
echo "======================================"
echo ""

# Warna untuk output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test 1: Create Customer
echo -e "${BLUE}Test 1: Create Customer${NC}"
echo "--------------------------------------"
curl -X POST $BASE_URL/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Budi Santoso",
    "governmentId": "3327012345678901",
    "email": "budi@example.com",
    "phoneNumber": "081234567890",
    "address": "Jl. Sudirman No. 123, Jakarta"
  }' | jq '.'
echo -e "\n${GREEN}✓ Customer created${NC}\n"
sleep 2

# Test 2: Create Another Customer
echo -e "${BLUE}Test 2: Create Another Customer${NC}"
echo "--------------------------------------"
curl -X POST $BASE_URL/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Siti Nurhaliza",
    "governmentId": "3328012345678902",
    "email": "siti@example.com",
    "phoneNumber": "081234567891",
    "address": "Jl. Thamrin No. 456, Jakarta"
  }' | jq '.'
echo -e "\n${GREEN}✓ Customer created${NC}\n"
sleep 2

# Test 3: Create Saving Account 1 for Customer 1
echo -e "${BLUE}Test 3: Create Saving Account 1 (Tabungan Utama)${NC}"
echo "--------------------------------------"
curl -X POST $BASE_URL/api/savings \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "1234567890",
    "accountName": "Tabungan Utama",
    "balance": 5000000,
    "accountType": "REGULAR",
    "customerId": 1
  }' | jq '.'
echo -e "\n${GREEN}✓ Saving account created${NC}\n"
sleep 2

# Test 4: Create Saving Account 2 for Customer 1
echo -e "${BLUE}Test 4: Create Saving Account 2 (Tabungan Haji)${NC}"
echo "--------------------------------------"
curl -X POST $BASE_URL/api/savings \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "1234567891",
    "accountName": "Tabungan Haji",
    "balance": 10000000,
    "accountType": "GOLD",
    "customerId": 1
  }' | jq '.'
echo -e "\n${GREEN}✓ Saving account created${NC}\n"
sleep 2

# Test 5: Create Saving Account 3 for Customer 1
echo -e "${BLUE}Test 5: Create Saving Account 3 (Tabungan Pendidikan)${NC}"
echo "--------------------------------------"
curl -X POST $BASE_URL/api/savings \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "1234567892",
    "accountName": "Tabungan Pendidikan",
    "balance": 15000000,
    "accountType": "PLATINUM",
    "customerId": 1
  }' | jq '.'
echo -e "\n${GREEN}✓ Saving account created${NC}\n"
sleep 2

# Test 6: Create Saving Account for Customer 2
echo -e "${BLUE}Test 6: Create Saving Account for Customer 2${NC}"
echo "--------------------------------------"
curl -X POST $BASE_URL/api/savings \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "2234567890",
    "accountName": "Tabungan Siti",
    "balance": 8000000,
    "accountType": "GOLD",
    "customerId": 2
  }' | jq '.'
echo -e "\n${GREEN}✓ Saving account created${NC}\n"
sleep 2

# Test 7: Get Customer by Government ID (MAIN FEATURE) ⭐
echo -e "${YELLOW}========================================${NC}"
echo -e "${YELLOW}⭐ Test 7: Get Customer by Government ID${NC}"
echo -e "${YELLOW}========================================${NC}"
curl $BASE_URL/api/customers/government-id/3327012345678901 | jq '.'
echo -e "\n${GREEN}✓ Customer with all savings retrieved${NC}\n"
sleep 2

# Test 8: Get All Customers
echo -e "${BLUE}Test 8: Get All Customers${NC}"
echo "--------------------------------------"
curl $BASE_URL/api/customers | jq '.'
echo -e "\n${GREEN}✓ All customers retrieved${NC}\n"
sleep 2

# Test 9: Get All Savings
echo -e "${BLUE}Test 9: Get All Savings${NC}"
echo "--------------------------------------"
curl $BASE_URL/api/savings | jq '.'
echo -e "\n${GREEN}✓ All savings retrieved${NC}\n"
sleep 2

# Test 10: Deposit to Saving Account
echo -e "${BLUE}Test 10: Deposit 1,000,000 to Saving ID 1${NC}"
echo "--------------------------------------"
curl -X POST "$BASE_URL/api/savings/1/deposit?amount=1000000" | jq '.'
echo -e "\n${GREEN}✓ Deposit successful${NC}\n"
sleep 2

# Test 11: Withdraw from Saving Account
echo -e "${BLUE}Test 11: Withdraw 500,000 from Saving ID 2${NC}"
echo "--------------------------------------"
curl -X POST "$BASE_URL/api/savings/2/withdraw?amount=500000" | jq '.'
echo -e "\n${GREEN}✓ Withdrawal successful${NC}\n"
sleep 2

# Test 12: Transfer between Saving Accounts
echo -e "${BLUE}Test 12: Transfer 1,000,000 from Saving 1 to Saving 3${NC}"
echo "--------------------------------------"
curl -X POST "$BASE_URL/api/savings/transfer?fromSavingId=1&toSavingId=3&amount=1000000" | jq '.'
echo -e "\n${GREEN}✓ Transfer successful${NC}\n"
sleep 2

# Test 13: Get Customer by Government ID Again (to see updated balances)
echo -e "${YELLOW}========================================${NC}"
echo -e "${YELLOW}⭐ Test 13: Get Customer by Government ID (After Transactions)${NC}"
echo -e "${YELLOW}========================================${NC}"
curl $BASE_URL/api/customers/government-id/3327012345678901 | jq '.'
echo -e "\n${GREEN}✓ Customer with updated savings retrieved${NC}\n"

echo ""
echo "======================================"
echo -e "${GREEN}✅ All Tests Completed!${NC}"
echo "======================================"
echo ""
echo "Tips:"
echo "- Akses H2 Console: http://localhost:8080/h2-console"
echo "- JDBC URL: jdbc:h2:mem:ewalletdb"
echo "- Username: sa"
echo "- Password: (kosongkan)"
echo ""
