package com.unax.peluqueriascheduler.service.exceptions;

public class UnauthorizedCitaAccessException extends RuntimeException {
    public UnauthorizedCitaAccessException(int citaId, int clienteId) {
        super("Cliente " + clienteId + " no tiene acceso a la cita " + citaId);
    }
}
