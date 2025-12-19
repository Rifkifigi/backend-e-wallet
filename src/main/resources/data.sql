-- Initial data untuk testing E-Wallet Application
-- File ini akan otomatis dijalankan saat aplikasi start

-- Insert sample customers
INSERT INTO customers (name, government_id, email, phone_number, address, created_at, updated_at) 
VALUES 
  ('Budi Santoso', '3327012345678901', 'budi@example.com', '081234567890', 'Jl. Sudirman No. 123, Jakarta', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Siti Nurhaliza', '3328012345678902', 'siti@example.com', '081234567891', 'Jl. Thamrin No. 456, Jakarta', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Ahmad Dahlan', '3329012345678903', 'ahmad@example.com', '081234567892', 'Jl. Asia Afrika No. 789, Bandung', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample savings for Customer 1 (Budi Santoso) - Multiple accounts
INSERT INTO savings (account_number, account_name, balance, account_type, status, customer_id, created_at, updated_at)
VALUES
  ('1234567890', 'Tabungan Utama', 5000000.00, 'REGULAR', 'ACTIVE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('1234567891', 'Tabungan Haji', 10000000.00, 'GOLD', 'ACTIVE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('1234567892', 'Tabungan Pendidikan', 15000000.00, 'PLATINUM', 'ACTIVE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('1234567893', 'Tabungan Investasi', 20000000.00, 'PLATINUM', 'ACTIVE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample savings for Customer 2 (Siti Nurhaliza) - Multiple accounts
INSERT INTO savings (account_number, account_name, balance, account_type, status, customer_id, created_at, updated_at)
VALUES
  ('2234567890', 'Tabungan Primer', 8000000.00, 'GOLD', 'ACTIVE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('2234567891', 'Tabungan Sekunder', 12000000.00, 'PLATINUM', 'ACTIVE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample savings for Customer 3 (Ahmad Dahlan)
INSERT INTO savings (account_number, account_name, balance, account_type, status, customer_id, created_at, updated_at)
VALUES
  ('3234567890', 'Tabungan Bisnis', 25000000.00, 'PLATINUM', 'ACTIVE', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Output info
SELECT 'Sample data inserted successfully!' AS message;
SELECT COUNT(*) AS total_customers FROM customers;
SELECT COUNT(*) AS total_savings FROM savings;
