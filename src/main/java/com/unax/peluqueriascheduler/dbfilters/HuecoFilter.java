package com.unax.peluqueriascheduler.dbfilters;

import static com.unax.peluqueriascheduler.generated.Tables.VISTA_HUECOS;
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

import com.unax.peluqueriascheduler.utils.TimeInterval;

import lombok.Builder;
@Builder
public record HuecoFilter  (
    Integer peluqueroId,
    LocalDateTime desde,
    LocalDateTime hasta,
    Integer duracionMinutos,
    Integer dia,
    Integer semana,
    Integer mes,
    Integer anio,
    Integer diaSemana,
    Map<Integer,List<TimeInterval>> rangosHorarios

)implements Filter {
    @Override
    public Condition toCondition(DSLContext dsl) {
        List<Condition> builder = new ArrayList<>();
        if (desde!= null) {
            builder.add(VISTA_HUECOS.DESDE.greaterOrEqual(desde));
        }
        if (hasta != null) {
            builder.add(VISTA_HUECOS.HASTA.lessOrEqual(hasta));
        }
        if (peluqueroId != null) {
            builder.add(VISTA_HUECOS.PELUQUERO_ID.eq(peluqueroId));
        }
        if (dia != null) {
            builder.add(extract(VISTA_HUECOS.DESDE, DatePart.DAY).eq(dia));
        }
        if (semana != null) {
            builder.add(extract(VISTA_HUECOS.DESDE, DatePart.WEEK).eq(semana));
        }
        if (mes != null) {
            builder.add(extract(VISTA_HUECOS.DESDE, DatePart.MONTH).eq(mes));
        }
        if (anio != null) {
            builder.add(extract(VISTA_HUECOS.DESDE, DatePart.YEAR).eq(anio));
        }
        if (diaSemana != null) {
            builder.add(extract(VISTA_HUECOS.DESDE, DatePart.ISO_DAY_OF_WEEK).eq(diaSemana));
        }
        Condition schedulcondition = getSchedulcondition();
        if (!schedulcondition.equals(DSL.noCondition())) {
            builder.add(schedulcondition);      
        }
        return builder.stream().reduce(Condition::and).orElse(DSL.noCondition());
    }
    // Fields and methods for HuecoFilter


    private Condition getSchedulcondition() {
        Condition schedulcondition = DSL.noCondition();
        if (rangosHorarios == null || rangosHorarios.isEmpty()){return schedulcondition;}
        for (Map.Entry<Integer, List<TimeInterval>> entry : rangosHorarios.entrySet()) {
            Integer diaSemana = entry.getKey();
            List<TimeInterval> intervals = entry.getValue();
            Condition dayCondition = extract(VISTA_HUECOS.DESDE, DatePart.ISO_DAY_OF_WEEK).eq(diaSemana);
            Condition timeCondition = DSL.noCondition();
            
            for (TimeInterval interval : intervals) {
                Condition intervalCondition;
                if (duracionMinutos == null){
                    intervalCondition = VISTA_HUECOS.DESDE.cast(LocalTime.class).lessOrEqual(interval.end())
                        .and(VISTA_HUECOS.HASTA.cast(LocalTime.class).greaterOrEqual(interval.start()));
                }else{
                    intervalCondition = VISTA_HUECOS.DESDE.cast(LocalTime.class).lessOrEqual(interval.end().minusMinutes(duracionMinutos))
                        .and(VISTA_HUECOS.HASTA.cast(LocalTime.class).greaterOrEqual(interval.start().plusMinutes(duracionMinutos)))
                        .and(DSL.field("EXTRACT(EPOCH FROM ({0} - {1})) / 60", Integer.class, VISTA_HUECOS.HASTA, VISTA_HUECOS.DESDE).greaterOrEqual(duracionMinutos));
                }  
                timeCondition = timeCondition == DSL.noCondition() ? intervalCondition : timeCondition.or(intervalCondition);
            }
            schedulcondition = schedulcondition.or(dayCondition.and(timeCondition));
        }
        return schedulcondition;
    }
}
