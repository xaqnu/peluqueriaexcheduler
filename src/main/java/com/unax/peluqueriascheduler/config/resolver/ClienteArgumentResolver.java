package com.unax.peluqueriascheduler.config.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.unax.peluqueriascheduler.domain.Usuarios.Cliente;
import com.unax.peluqueriascheduler.service.UsuarioService;


@Component
@Validated
public class ClienteArgumentResolver implements HandlerMethodArgumentResolver {

    private final UsuarioService usuarioService;

    ClienteArgumentResolver(UsuarioService usuarioService){
        this.usuarioService=usuarioService;
    }
    @Override
    public boolean supportsParameter( @NonNull MethodParameter parameter) {
        return parameter.getParameterType().equals(Cliente.class);
        
    }

    @Override
    @Nullable
    public Cliente resolveArgument(@NonNull MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
            @NonNull NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        return usuarioService.getUsuarioById(3).toCliente();
    }
    
}
