package com.unax.peluqueriascheduler.domain.Usuarios;

public record UsuarioRequest(
    String nombre,
    String email,
    String telefono,
    String rol
) {

}
