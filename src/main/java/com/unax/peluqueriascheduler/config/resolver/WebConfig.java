package com.unax.peluqueriascheduler.config.resolver;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



import org.springframework.web.method.support.HandlerMethodArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Inyectamos el componente del resolutor que Spring ya ha creado y rellenado con su servicio
    private final ClienteArgumentResolver clienteArgumentResolver;

    public WebConfig(ClienteArgumentResolver clienteArgumentResolver) {
        this.clienteArgumentResolver = clienteArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> resolvers) {
        // Registramos la instancia exacta que controla Spring
        resolvers.add(0,clienteArgumentResolver);
    }
}
