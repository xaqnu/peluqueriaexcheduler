package com.unax.peluqueriascheduler.repositories;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.unax.peluqueriascheduler.domain.Citas.exceptions.TipoServicioNotFoundException;

import static com.unax.peluqueriascheduler.generated.Tables.TIPOS_SERVICIO;
@Repository
public class TiposServicioRepository {
    private final DSLContext dsl;
    
    public TiposServicioRepository(DSLContext dsl) {
        this.dsl = dsl;
    }
    
    public int getDuracion(String tipoServicio) {
        return dsl.selectFrom(TIPOS_SERVICIO)
              .where(TIPOS_SERVICIO.NOMBRE.eq(tipoServicio))
              .fetchOptional(TIPOS_SERVICIO.DURACION_MINUTOS)
              .orElseThrow(() -> new TipoServicioNotFoundException(tipoServicio));

    }
}
