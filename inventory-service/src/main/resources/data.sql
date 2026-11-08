INSERT INTO medicines (medicine_id, medicine_name, stock)
VALUES
    ('MED-001', 'Paracetamol', 100),
    ('MED-002', 'Vitamin C', 80),
    ('MED-003', 'Cough Syrup', 50)
ON CONFLICT (medicine_id) DO NOTHING;
