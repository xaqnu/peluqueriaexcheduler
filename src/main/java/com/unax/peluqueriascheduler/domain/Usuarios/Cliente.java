package com.unax.peluqueriascheduler.domain.Usuarios;

public non-sealed class Cliente extends Usuario {

    public Cliente(int id, String nombre, String email, String telefono) {
        super(id, nombre, email, telefono);
        
    }
    public Cliente(int id, String nombre, String email) {
        super(id, nombre, email, null);
        
    }

}
