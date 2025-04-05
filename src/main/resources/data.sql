-- Delete existing admin user if exists
DELETE FROM users WHERE username = 'admin';

-- Insert default admin user (password: admin123)
INSERT INTO users (username, email, password, role)
VALUES ('admin', 'admin@example.com', '$2a$10$3vxMOh8OfGMkEfGcQ162C.2y4CoEI1V5ETVwkdGm4UFOnS6FlT2H2', 'ADMIN');

-- Insert initial tournament
INSERT INTO tournaments (date, location, name, number_of_mats, status)
VALUES ('2025-04-03', 'Stockholm', 'Stockholm Open 2025', 4, 'PLANNED')
ON CONFLICT DO NOTHING; 