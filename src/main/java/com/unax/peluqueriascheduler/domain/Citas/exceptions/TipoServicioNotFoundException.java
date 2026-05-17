package com.unax.peluqueriascheduler.domain.Citas.exceptions;

public class TipoServicioNotFoundException extends RuntimeException  {
    public TipoServicioNotFoundException(String tipoServicio){
        super("No existe el tipo de servicio " + tipoServicio);
    }
}
