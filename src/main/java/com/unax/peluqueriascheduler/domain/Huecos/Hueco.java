package com.unax.peluqueriascheduler.domain.Huecos;

import java.time.LocalTime;

public record Hueco(
    Integer peluqueroId,
    LocalTime desde,
    LocalTime   hasta
) {
    public Hueco {
        if (peluqueroId == null || peluqueroId <= 0) throw new IllegalArgumentException("peluqueroId debe ser positivo");
        if (desde == null) throw new IllegalArgumentException("desde no puede ser null");
        if (hasta == null) throw new IllegalArgumentException("hasta no puede ser null");
        if (hasta.isBefore(desde)) throw new IllegalArgumentException("hasta debe ser después de desde");
    }
    
}
