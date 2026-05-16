package com.unax.peluqueriascheduler.domain.Citas;

import java.time.LocalDateTime;

public record CitaRequest(
     String tipoServicio,
     LocalDateTime timestampInicio

) {

     public CitaRequest {
          timestampInicio=timestampInicio.truncatedTo(java.time.temporal.ChronoUnit.MINUTES);
          if (tipoServicio == null || tipoServicio.isBlank()) throw new IllegalArgumentException("tipoServicio no puede ser null o vacío");
          if (timestampInicio == null) throw new IllegalArgumentException("timestampInicio no puede ser null");
          if (timestampInicio.isBefore(LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.MINUTES))) throw new IllegalArgumentException("timestampInicio debe ser en el futuro");
     }
    
    
}
