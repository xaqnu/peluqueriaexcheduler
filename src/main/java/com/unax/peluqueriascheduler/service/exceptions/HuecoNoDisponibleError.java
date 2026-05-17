package com.unax.peluqueriascheduler.service.exceptions;

import com.unax.peluqueriascheduler.domain.Citas.CitaRequest;

public class HuecoNoDisponibleError extends RuntimeException{
    public HuecoNoDisponibleError(CitaRequest request,Integer peluquero_id){
        super("el peluquero con id: " 
                + peluquero_id.toString()
                +  "no puede atenderle a las " 
                +request.timestampInicio().toString()
                +"para el servicio" 
                + request.tipoServicio());
    }
}
