package com.unax.peluqueriascheduler.domain.Citas;

import java.time.LocalDateTime;


public record Cita (
     int id,
     int clientId,
     int peluqueroId,
     int tipoServicioId,
     LocalDateTime timestampInicio,
     LocalDateTime timestampFin,
     EstadoCita estado
){
    public Cita {
        if (id <= 0) throw new IllegalArgumentException("id debe ser positivo");
        if (clientId <= 0) throw new IllegalArgumentException("clienteId debe ser positivo");
        if (peluqueroId <= 0) throw new IllegalArgumentException("peluqueroId debe ser positivo");
        if (tipoServicioId <= 0) throw new IllegalArgumentException("tipoServicioId debe ser positivo");
        if (timestampInicio == null) throw new IllegalArgumentException("timestampInicio no puede ser null");
        if (timestampFin == null) throw new IllegalArgumentException("timestampFin no puede ser null");
        if (timestampFin.isBefore(timestampInicio)) throw new IllegalArgumentException("timestampFin debe ser después de timestampInicio");
        if (estado == null) throw new IllegalArgumentException("estado no puede ser null");
    }
    
};

