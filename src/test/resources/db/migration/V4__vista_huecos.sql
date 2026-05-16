CREATE VIEW vista_huecos AS
WITH citas_rango AS (
    SELECT c.id AS id,
           c.peluquero_id AS peluquero_id,
           c.timestamp_inicio AS timestamp_inicio,
           c.timestamp_fin AS timestamp_fin
    FROM citas c
    JOIN peluquero_configuracion pc ON pc.peluquero_id = c.peluquero_id
    WHERE c.estado IN ('CONFIRMADA', 'PENDIENTE') 
    AND c.timestamp_inicio >= NOW() AND c.timestamp_inicio < NOW() + pc.semanas_agenda_abierta * INTERVAL '1 week'
),
 huecos_entre_citas AS (
    SELECT peluquero_id,
           timestamp_fin AS desde,
           LEAD(timestamp_inicio) OVER (PARTITION BY peluquero_id, DATE(timestamp_inicio) ORDER BY timestamp_inicio) AS hasta
    FROM citas_rango
),
limites_jornada AS (
    SELECT peluquero_id,
           DATE(timestamp_inicio) AS fecha,
           MIN(timestamp_inicio) AS inicio_primera_cita,
           MAX(timestamp_fin) AS fin_ultima_cita
    FROM citas_rango
    GROUP BY peluquero_id, DATE(timestamp_inicio)
),
huecos_limite_inferior_jornada AS (
    SELECT hl.peluquero_id,
           lj.fecha + hl.hora_inicio AS desde,
           lj.inicio_primera_cita AS hasta
    FROM limites_jornada lj
    JOIN horarios_laborales hl ON hl.peluquero_id = lj.peluquero_id
                               AND hl.dia_semana = EXTRACT(ISODOW FROM lj.fecha)
),
huecos_limite_superior_jornada AS (
    SELECT hl.peluquero_id,
           lj.fin_ultima_cita AS desde,
           lj.fecha + hl.hora_fin AS hasta
    FROM limites_jornada lj
    JOIN horarios_laborales hl ON hl.peluquero_id = lj.peluquero_id
                               AND hl.dia_semana = EXTRACT(ISODOW FROM lj.fecha)
),
dias_libres AS (
    SELECT 
        hl.peluquero_id,
        -- combinamos fecha del calendario con hora de inicio de jornada
        fecha + hl.hora_inicio AS desde,
        -- combinamos fecha del calendario con hora de fin de jornada
        fecha + hl.hora_fin AS hasta
    FROM horarios_laborales hl
    -- generamos una fila por cada día del calendario para cada peluquero
    CROSS JOIN generate_series(
        CURRENT_DATE,
        CURRENT_DATE + interval '10 weeks',
        interval '1 day'
    ) AS fecha
    -- solo los días que ese peluquero trabaja
    WHERE EXTRACT(ISODOW FROM fecha) = hl.dia_semana
    -- solo los días sin ninguna cita
    AND NOT EXISTS (
        SELECT 1 
        FROM citas_rango cr
        WHERE cr.peluquero_id = hl.peluquero_id
        AND DATE(cr.timestamp_inicio) = fecha
    )
)
SELECT peluquero_id, desde, hasta FROM huecos_entre_citas
WHERE hasta IS NOT NULL AND hasta > desde

UNION ALL

SELECT peluquero_id, desde, hasta FROM huecos_limite_inferior_jornada
WHERE hasta > desde

UNION ALL

SELECT peluquero_id, desde, hasta FROM huecos_limite_superior_jornada
WHERE hasta > desde

UNION ALL

SELECT peluquero_id, desde, hasta FROM dias_libres;