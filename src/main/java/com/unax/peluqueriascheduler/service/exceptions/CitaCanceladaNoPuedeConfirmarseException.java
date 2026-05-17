package com.unax.peluqueriascheduler.service.exceptions;

import com.unax.peluqueriascheduler.domain.Citas.Cita;

public class CitaCanceladaNoPuedeConfirmarseException extends RuntimeException {
    public CitaCanceladaNoPuedeConfirmarseException(Cita cita){
        super("esta cita esta cancelada y no puede confirmarse:" + cita.toString());
    }
}
