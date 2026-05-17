package com.unax.peluqueriascheduler.dbfilters;

import static com.unax.peluqueriascheduler.generated.Tables.CITAS;
import static org.jooq.impl.DSL.extract;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jooq.Condition;

import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.jooq.impl.DSL;

import com.unax.peluqueriascheduler.domain.Citas.EstadoCita;
import com.unax.peluqueriascheduler.domain.Usuarios.Cliente;
import com.unax.peluqueriascheduler.domain.Usuarios.Peluquero;
import com.unax.peluqueriascheduler.utils.TimeInterval;

import lombok.Builder;

@Builder
public record CitasFilter( 
    Integer id,
    Cliente cliente,
    Peluquero peluquero,
    Integer tipoServicioId,
    EstadoCita estado,
    EstadoCita noestado,
    Integer dia,
    Integer semana,
    Integer mes,
    Integer anio,
    Integer diaSemana,
    Map<Integer,List<TimeInterval>> rangosHorarios,
    LocalDateTime desde,
    LocalDateTime hasta
)implements Filter {
    @Override
    public Condition toCondition(DSLContext dsl) {
        List<Condition> builder = new ArrayList<>();
        if (id != null){
            builder.add(CITAS.ID.eq(id));
        }
        if  (noestado!=null){
            builder.add(CITAS.ESTADO.notEqual(noestado.name()));
        }
        if (desde!= null) {
            builder.add(CITAS.TIMESTAMP_INICIO.greaterOrEqual(desde));
        }
        if (hasta != null) {
            builder.add(CITAS.TIMESTAMP_FIN.lessOrEqual(hasta));
        }
        if (cliente != null) {
            builder.add(CITAS.CLIENTE_ID.eq(cliente.id()));
        }
        if (peluquero!= null) {
            builder.add(CITAS.PELUQUERO_ID.eq(peluquero.id()));
        }
        if (tipoServicioId != null) {
            builder.add(CITAS.TIPO_SERVICIO_ID.eq(tipoServicioId));
        }
        if (estado != null) {
            builder.add(CITAS.ESTADO.eq(estado.name()));
        }
        if (dia != null) {
            builder.add(extract(CITAS.TIMESTAMP_INICIO, DatePart.DAY).eq(dia));
        }
        if (semana != null) {
            builder.add(extract(CITAS.TIMESTAMP_INICIO, DatePart.WEEK).eq(semana));
        }
        if (mes != null) {
            builder.add(extract(CITAS.TIMESTAMP_INICIO, DatePart.MONTH).eq(mes));
        }
        if (anio != null) {
            builder.add(extract(CITAS.TIMESTAMP_INICIO, DatePart.YEAR).eq(anio));
        }
        if (diaSemana != null) {
            builder.add(extract(CITAS.TIMESTAMP_INICIO, DatePart.ISO_DAY_OF_WEEK).eq(diaSemana));
        }
        Condition schedulcondition = getSchedulcondition();
        if (!schedulcondition.equals(DSL.noCondition())) {
            builder.add(schedulcondition);
        }

        
        return builder.stream().reduce(Condition::and).orElse(DSL.noCondition());
    }

    private Condition getSchedulcondition() {
        Condition schedulcondition = DSL.noCondition();
        if (rangosHorarios == null || rangosHorarios.isEmpty()){return schedulcondition;}
        for (Map.Entry<Integer, List<TimeInterval>> entry : rangosHorarios.entrySet()) {
            Integer diaSemana = entry.getKey();
            List<TimeInterval> intervals = entry.getValue();
            Condition dayCondition = extract(CITAS.TIMESTAMP_INICIO, DatePart.ISO_DAY_OF_WEEK).eq(diaSemana);
            Condition timeCondition = DSL.noCondition();
            for (TimeInterval interval : intervals) {
                Condition intervalCondition = CITAS.TIMESTAMP_INICIO.cast(LocalTime.class).between(interval.start(), interval.end());
                timeCondition =  timeCondition.or(intervalCondition);
            }
            schedulcondition = schedulcondition.or(dayCondition.and(timeCondition));
        }
        return schedulcondition;
    }
}
