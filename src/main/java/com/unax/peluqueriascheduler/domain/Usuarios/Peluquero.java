package com.unax.peluqueriascheduler.domain.Usuarios;

public final class Peluquero extends Usuario {

    public Peluquero(int id, String nombre, String email, String telefono) {
        super(id, nombre, email, telefono);
    
    }
    public Peluquero(int id, String nombre, String email) {
        super(id, nombre, email, null);
    
    }

}
