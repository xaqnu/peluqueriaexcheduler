CREATE OR REPLACE FUNCTION calcular_timestamp_fin() RETURNS TRIGGER AS $$

DECLARE
    duracion INTEGER;
BEGIN
    SELECT duracion_minutos INTO duracion FROM tipos_servicio WHERE id = NEW.tipo_servicio_id;
    NEW.timestamp_fin := NEW.timestamp_inicio + (duracion || ' minutes')::interval;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;    
CREATE TRIGGER trigger_calcular_timestamp_fin
BEFORE INSERT OR UPDATE ON citas
FOR EACH ROW
EXECUTE FUNCTION calcular_timestamp_fin();