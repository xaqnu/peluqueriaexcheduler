CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    rol_id INT NOT NULL REFERENCES roles(id)
);

CREATE TABLE tipos_servicio (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    duracion_minutos INT NOT NULL,
    precio DECIMAL(10,2) NOT NULL
);

CREATE TABLE citas (
    id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL REFERENCES usuarios(id),
    peluquero_id INT NOT NULL REFERENCES usuarios(id),
    tipo_servicio_id INT NOT NULL REFERENCES tipos_servicio(id),
    timestamp_inicio TIMESTAMP NOT NULL,
    timestamp_fin TIMESTAMP NOT NULL,
    estado VARCHAR(20) NOT NULL
);
CREATE TABLE horarios_laborales (
    peluquero_id INT NOT NULL REFERENCES usuarios(id),
    dia_semana INT NOT NULL CHECK (dia_semana BETWEEN 1 AND 7),
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    PRIMARY KEY (peluquero_id, dia_semana)
);
CREATE TABLE peluquero_configuracion (
    peluquero_id INT PRIMARY KEY REFERENCES usuarios(id),
    semanas_agenda_abierta INT NOT NULL DEFAULT 4 check (semanas_agenda_abierta > 0 AND semanas_agenda_abierta <= 10)
);
CREATE TABLE autenticacion(
    user_id INT PRIMARY KEY REFERENCES usuarios(id),
    pass_hash VARCHAR(256),
    salting VARCHAR(256)
);
CREATE INDEX idx_usuarios_email ON usuarios(email);  -- login
CREATE INDEX idx_citas_cliente ON citas(cliente_id); -- mis citas
CREATE INDEX idx_citas_peluquero ON citas(peluquero_id); -- agenda peluquero
CREATE INDEX idx_citas_timestamp_inicio ON citas(peluquero_id,timestamp_inicio);   
CREATE INDEX idx_citas_timestamp_fin ON citas(peluquero_id,timestamp_fin);
