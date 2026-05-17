package com.unax.peluqueriascheduler.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unax.peluqueriascheduler.dbfilters.CitasFilter;
import com.unax.peluqueriascheduler.dbfilters.HuecoFilter;
import com.unax.peluqueriascheduler.domain.Citas.Cita;
import com.unax.peluqueriascheduler.domain.Citas.CitaRequest;

import com.unax.peluqueriascheduler.domain.Huecos.Hueco;
import com.unax.peluqueriascheduler.domain.Usuarios.Cliente;
import com.unax.peluqueriascheduler.domain.Usuarios.Peluquero;
import com.unax.peluqueriascheduler.repositories.CitasRepository;
import com.unax.peluqueriascheduler.repositories.HuecosRepository;
import com.unax.peluqueriascheduler.repositories.TiposServicioRepository;
import com.unax.peluqueriascheduler.repositories.UsuariosRepository;
import com.unax.peluqueriascheduler.service.exceptions.CitaCanceladaNoPuedeConfirmarseException;
import com.unax.peluqueriascheduler.service.exceptions.HuecoNoDisponibleError;
import com.unax.peluqueriascheduler.service.exceptions.UnauthorizedCitaAccessException;




@Service
public class CitasService {

    private final CitasRepository citasRepository; 
    private final HuecosRepository huecosRepository;
    private final TiposServicioRepository tiposServicioRepository;
    private final UsuariosRepository usuariosRepository;

    CitasService(
        CitasRepository citasRepository,
        HuecosRepository huecosRepository,
        TiposServicioRepository tiposServicioRepository,
        UsuariosRepository usuariosRepository
    ){

        this.citasRepository= citasRepository;
        this.huecosRepository=huecosRepository;
        this.tiposServicioRepository = tiposServicioRepository;
        this.usuariosRepository =usuariosRepository;
    }


    public List<Cita> getproximasCitas(Cliente cliente, int semanas) {
        CitasFilter filter = CitasFilter.builder()
                                        .desde(LocalDateTime.now())
                                        .hasta(LocalDateTime.now().plusWeeks(semanas))
                                        .build();
        return citasRepository.getCitasByFilter(filter);
    }


    public Cita getCitaById(Cliente cliente,int id) {
        CitasFilter filter = CitasFilter.builder()
                                        .cliente(cliente)
                                        .id(id)
                                        .build();
        List<Cita> citas =citasRepository.getCitasByFilter(filter);
        if (citas.isEmpty()) {throw new UnauthorizedCitaAccessException(id,cliente.id());}                                
        return citasRepository.getCitasByFilter(filter).get(0);
    }


    public List<Hueco> getHuecosDisponiblesByFilter(HuecoFilter filter) {
        return huecosRepository.getHuecosByFilter(filter);
    }

    @Transactional
    public Cita confirmarCita(Cliente cliente, int id) {
        Cita cita =getCitaById(cliente, id);
        switch ( cita.estado()){
            case CONFIRMADA : return cita;
            case PENDIENTE : return citasRepository.confirmarCita(cita);
            case CANCELADA : throw new CitaCanceladaNoPuedeConfirmarseException(cita);
        }
        return cita;
    }
    @Transactional
    public Cita cancelarCita(Cliente cliente, int id) {
        Cita cita =getCitaById(cliente, id);
        return citasRepository.cancelarCita(cita);
    }

    @Transactional
	public Cita agendarCita(CitaRequest request, Cliente cliente) {
        int peluquero_id = request.peluqueroId();
        int duracion = tiposServicioRepository.getDuracion(request.tipoServicio());
        HuecoFilter filter = HuecoFilter.builder()
                                        .peluqueroId(peluquero_id)
                                        .desde(request.timestampInicio())
                                        .hasta(request.timestampInicio().plusMinutes(duracion))
                                        .build();
        List<Hueco> huecos = huecosRepository.getHuecosByFilter(filter);
        if (huecos.isEmpty()) {throw new HuecoNoDisponibleError(request,peluquero_id);}
        Peluquero peluquero =  usuariosRepository.getUsuarioById(peluquero_id).toPeluquero();
        return citasRepository.createCita(request, cliente, peluquero);
	}



}
