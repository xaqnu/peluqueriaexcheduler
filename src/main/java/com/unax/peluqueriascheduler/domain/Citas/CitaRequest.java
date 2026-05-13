package com.unax.peluqueriascheduler.domain.Citas;

import java.time.LocalDateTime;

public record CitaRequest(
     String tipoServicio,
     LocalDateTime timestampInicio

) {

    
    
}
