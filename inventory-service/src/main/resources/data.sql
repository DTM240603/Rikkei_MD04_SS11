INSERT INTO medicines (medicine_id, medicine_name, stock, price)
VALUES
    ('MED-001', 'Paracetamol', 100, 12000.00),
    ('MED-002', 'Vitamin C', 80, 25000.00),
    ('MED-003', 'Cough Syrup', 50, 45000.00)
ON CONFLICT (medicine_id) DO NOTHING;

UPDATE medicines SET price = 0 WHERE price IS NULL;
