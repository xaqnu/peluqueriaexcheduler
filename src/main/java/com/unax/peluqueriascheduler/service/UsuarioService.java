package com.unax.peluqueriascheduler.service;

import org.springframework.stereotype.Service;

import com.unax.peluqueriascheduler.domain.Usuarios.Usuario;
import com.unax.peluqueriascheduler.repositories.UsuariosRepository;

@Service
public class UsuarioService {
    private final UsuariosRepository usuarioRepository;
    
    UsuarioService(UsuariosRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario getUsuarioById(int id){
        return usuarioRepository.getUsuarioById(id);
    }
}
