package com.unax.peluqueriascheduler.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.unax.peluqueriascheduler.dbfilters.CitasFilter;
import com.unax.peluqueriascheduler.domain.Citas.Cita;
import com.unax.peluqueriascheduler.domain.Usuarios.Cliente;
import com.unax.peluqueriascheduler.repositories.CitasRepository;

@Service
public class CitasService {

    private final CitasRepository citasRepository; 

    CitasService(CitasRepository citasRepository){
        this.citasRepository= citasRepository;
    }


    public List<Cita> getproximasCitas(Cliente cliente, int semanas) {
        CitasFilter filter = CitasFilter.builder()
                                        .desde(LocalDateTime.now())
                                        .hasta(LocalDateTime.now().plusWeeks(semanas))
                                        .build();
        return citasRepository.getCitasByFilter(filter);
    }

}
