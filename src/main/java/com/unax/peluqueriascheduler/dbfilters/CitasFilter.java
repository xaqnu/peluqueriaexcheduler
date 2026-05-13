package com.unax.peluqueriascheduler.dbfilters;

import static com.unax.peluqueriascheduler.generated.Tables.CITAS;
import static com.unax.peluqueriascheduler.generated.Tables.VISTA_HUECOS;
import static org.jooq.impl.DSL.extract;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jooq.Condition;

import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.jooq.impl.DSL;

import com.unax.peluqueriascheduler.domain.Citas.EstadoCita;
import com.unax.peluqueriascheduler.utils.TimeInterval;

import lombok.Builder;

@Builder
public record CitasFilter( 
    Integer clienteId,
    Integer peluqueroId,
    Integer tipoServicioId,
    EstadoCita estado,
    Integer dia,
    Integer semana,
    Integer mes,
    Integer anio,
    Integer diaSemana,
    Map<Integer,List<TimeInterval>> rangosHorarios,
    LocalDate desde,
    LocalDate hasta
)implements Filter {
    @Override
    public Condition toCondition(DSLContext dsl) {
        List<Condition> builder = new ArrayList<>();
        if (desde!= null) {
            builder.add(VISTA_HUECOS.DESDE.cast(java.time.LocalDate.class).greaterOrEqual(desde));
        }
        if (hasta != null) {
            builder.add(VISTA_HUECOS.HASTA.cast(java.time.LocalDate.class).lessOrEqual(hasta));
        }
        if (clienteId != null) {
            builder.add(CITAS.CLIENTE_ID.eq(clienteId));
        }
        if (peluqueroId != null) {
            builder.add(CITAS.PELUQUERO_ID.eq(peluqueroId));
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
        for (Map.Entry<Integer, List<TimeInterval>> entry : rangosHorarios.entrySet()) {
            Integer diaSemana = entry.getKey();
            List<TimeInterval> intervals = entry.getValue();
            Condition dayCondition = extract(CITAS.TIMESTAMP_INICIO, DatePart.ISO_DAY_OF_WEEK).eq(diaSemana);
            Condition timeCondition = DSL.noCondition();
            for (TimeInterval interval : intervals) {
                Condition intervalCondition = CITAS.TIMESTAMP_INICIO.cast(LocalTime.class).between(interval.start(), interval.end());
                timeCondition = timeCondition == DSL.noCondition() ? intervalCondition : timeCondition.or(intervalCondition);
            }
            schedulcondition = schedulcondition.or(dayCondition.and(timeCondition));
        }
        return schedulcondition;
    }
}
