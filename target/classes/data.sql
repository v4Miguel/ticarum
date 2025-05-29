-- Hospital (si no quieres definir id manualmente)
INSERT INTO hospital (nombre) VALUES ('Hospital Central');
INSERT INTO hospital (nombre) VALUES ('Hospital Norte');
INSERT INTO hospital (nombre) VALUES ('Hospital Sur');

-- Dependencia
INSERT INTO dependencia (nombre, hospital_id) VALUES ('Urgencias', 1);
INSERT INTO dependencia (nombre, hospital_id) VALUES ('Pediatría', 1);
INSERT INTO dependencia (nombre, hospital_id) VALUES ('Cirugía', 2);

-- Cama
INSERT INTO cama (estado, hospital_id, dependencia_id) VALUES ('LIBRE', 1, 1);
INSERT INTO cama (estado, hospital_id, dependencia_id) VALUES ('OCUPADA', 1, 2);

