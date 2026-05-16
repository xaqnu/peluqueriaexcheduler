package com.unax.peluqueriascheduler.repositories;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.unax.peluqueriascheduler.dbfilters.CitasFilter;
import com.unax.peluqueriascheduler.domain.Citas.Cita;
import com.unax.peluqueriascheduler.domain.Citas.CitaRequest;
import com.unax.peluqueriascheduler.domain.Citas.EstadoCita;
import com.unax.peluqueriascheduler.domain.Usuarios.Cliente;
import com.unax.peluqueriascheduler.domain.Usuarios.Peluquero;
import com.unax.peluqueriascheduler.generated.tables.records.CitasRecord;


import static com.unax.peluqueriascheduler.generated.Tables.CITAS;
import static com.unax.peluqueriascheduler.generated.Tables.TIPOS_SERVICIO;
import java.util.List;

@Repository
public class CitasRepository {
    private final DSLContext dsl;

    public CitasRepository(DSLContext dsl) {
        this.dsl = dsl;
    }
    @Transactional(readOnly=true)
    public Cita getCitaById(int id) {
        return dsl.selectFrom(CITAS)
                  .where(CITAS.ID.eq(id))
                  .fetchOneInto(Cita.class);
    }
    @Transactional(readOnly=true)
    public List<Cita> getCitasByCliente(Cliente usuario){
        return dsl.selectFrom(CITAS)
                  .where(CITAS.CLIENTE_ID.eq(usuario.id()))
                  .fetchInto(Cita.class);

    }
    @Transactional(readOnly=true)
    public List<Cita> getCitasByPeluquero(Peluquero peluquero){
        return dsl.selectFrom(CITAS)
                    .where(CITAS.PELUQUERO_ID.eq(peluquero.id()))
                    .fetchInto(Cita.class);
    }
    @Transactional(readOnly=true)
    public List<Cita> getCitasByFilter(CitasFilter filter ){
        return dsl.selectFrom(CITAS)
                  .where(filter.toCondition(dsl))
                  .fetchInto(Cita.class);
    }
    @Transactional
    public Cita createCita(CitaRequest cita, Cliente cliente,Peluquero peluquero){
        int tipoServicioId = dsl.selectFrom(TIPOS_SERVICIO)
                                .where(TIPOS_SERVICIO.NOMBRE.eq(cita.tipoServicio()))
                                .fetchOptional(TIPOS_SERVICIO.ID)
                                .orElseThrow(() -> new IllegalArgumentException("Tipo de servicio no encontrado: " + cita.tipoServicio()));
        return dsl.insertInto(CITAS)
            .set(CITAS.CLIENTE_ID,cliente.id())
            .set(CITAS.PELUQUERO_ID,peluquero.id()) 
            .set(CITAS.TIPO_SERVICIO_ID,tipoServicioId)
            .set(CITAS.TIMESTAMP_INICIO,cita.timestampInicio())
            .set(CITAS.ESTADO, EstadoCita.PENDIENTE.name())
            .returning()
            .fetchOneInto(Cita.class);
    }
    @Transactional
    public Cita updateCita(Cita cita){
        CitasRecord record = dsl.newRecord(CITAS, cita);
        record.changed(CITAS.ID, false);
        
        return dsl.update(CITAS)
                  .set(record)
                  .where(CITAS.ID.eq(cita.id()))
                  .returning()
                  .fetchOptionalInto(Cita.class)
                  .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada: " + cita.id()));
    }
    @Transactional
    public Cita cancelarCita(Cita cita ){
        return dsl.update(CITAS)
                  .set(CITAS.ESTADO, EstadoCita.CANCELADA.name())
                  .where(CITAS.ID.eq(cita.id()))
                  .returning()
                  .fetchOptionalInto(Cita.class)
                  .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada: " + cita.id()));
    }
    @Transactional
    public Cita confirmarCita(Cita cita){
        return dsl.update(CITAS)
                  .set(CITAS.ESTADO, EstadoCita.CONFIRMADA.name())
                  .where(CITAS.ID.eq(cita.id()))
                  .returning()
                  .fetchOptionalInto(Cita.class)
                  .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada: " + cita.id()));
    }
}