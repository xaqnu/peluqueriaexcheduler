package com.unax.peluqueriascheduler.utils;

import java.time.LocalTime;

public record TimeInterval(
    LocalTime start,
    LocalTime end
) {
    public TimeInterval {
        if (start == null) throw new IllegalArgumentException("start no puede ser null");
        if (end == null) throw new IllegalArgumentException("end no puede ser null");
        if (end.isBefore(start)) throw new IllegalArgumentException("end debe ser después de start");
    }
}
