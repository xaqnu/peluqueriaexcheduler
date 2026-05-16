-- roles
INSERT INTO roles (nombre) VALUES ('cliente'), ('peluquero'), ('admin');

-- usuarios peluqueros
INSERT INTO usuarios (nombre, email, rol_id) VALUES
('María García', 'maria@test.com', 2),
('Carlos López', 'carlos@test.com', 2);

-- usuario cliente
INSERT INTO usuarios (nombre, email, rol_id) VALUES
('Juan Pérez', 'juan@test.com', 1);

-- config peluqueros
INSERT INTO peluquero_configuracion (peluquero_id, semanas_agenda_abierta) VALUES 
(1, 4), 
(2, 6);

-- horarios laborales maria lunes a viernes
INSERT INTO horarios_laborales (peluquero_id, dia_semana, hora_inicio, hora_fin) VALUES
(1, 1, '09:00', '18:00'),
(1, 2, '09:00', '18:00'),
(1, 3, '09:00', '18:00'),
(1, 4, '09:00', '18:00'),
(1, 5, '09:00', '18:00');

-- horarios laborales carlos lunes a sabado
INSERT INTO horarios_laborales (peluquero_id, dia_semana, hora_inicio, hora_fin) VALUES
(2, 1, '10:00', '19:00'),
(2, 2, '10:00', '19:00'),
(2, 3, '10:00', '19:00'),
(2, 4, '10:00', '19:00'),
(2, 5, '10:00', '19:00'),
(2, 6, '10:00', '15:00');

-- tipos de servicio
INSERT INTO tipos_servicio (nombre, duracion_minutos, precio) VALUES
('Corte', 30, 15.00),
('Tinte', 90, 45.00),
('Barba', 20, 10.00);

-- citas maria manana (corte 10:00-10:30, tinte 12:00-13:30)
INSERT INTO citas (cliente_id, peluquero_id, tipo_servicio_id, timestamp_inicio, estado) VALUES
(3, 1, 1, '2026-05-12 10:00:00', 'CONFIRMADA'),
(3, 1, 2, '2026-05-12 12:00:00', 'CANCELADA'),
(3, 1, 3, '2026-05-14 09:30:00', 'CONFIRMADA'),
(3, 1, 1, '2026-05-14 15:00:00', 'PENDIENTE');
INSERT INTO citas (cliente_id, peluquero_id, tipo_servicio_id, timestamp_inicio, estado) VALUES
(3, 2, 2, '2026-05-12 10:00:00', 'CONFIRMADA'),
(3, 2, 1, '2026-05-13 14:00:00', 'CONFIRMADA'),
(3, 2, 3, '2026-05-15 11:00:00', 'PENDIENTE');
-- caso 1: cita justo al inicio de jornada (no debe haber hueco de inicio)
INSERT INTO citas (cliente_id, peluquero_id, tipo_servicio_id, timestamp_inicio, estado) VALUES
(3, 1, 1, '2026-05-18 09:00:00', 'CONFIRMADA');

-- caso 2: cita justo al final de jornada (no debe haber hueco de fin)
INSERT INTO citas (cliente_id, peluquero_id, tipo_servicio_id, timestamp_inicio, estado) VALUES
(3, 1, 1, '2026-05-19 17:30:00', 'CONFIRMADA');

-- caso 3: citas consecutivas sin hueco entre ellas
INSERT INTO citas (cliente_id, peluquero_id, tipo_servicio_id, timestamp_inicio, estado) VALUES
(3, 1, 1, '2026-05-20 10:00:00', 'CONFIRMADA'),
(3, 1, 1, '2026-05-20 10:30:00', 'CONFIRMADA');

-- caso 4: peluquero 2 sin citas (carlos) - ya cubierto por dias_libres
-- caso 5: cita unica en el dia para verificar dos limites de jornada
INSERT INTO citas (cliente_id, peluquero_id, tipo_servicio_id, timestamp_inicio, estado) VALUES
(3, 1, 2, '2026-05-21 11:00:00', 'CONFIRMADA');