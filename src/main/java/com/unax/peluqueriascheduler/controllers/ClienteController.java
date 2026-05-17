package com.unax.peluqueriascheduler.controllers;
import com.unax.peluqueriascheduler.dbfilters.HuecoFilter;
import com.unax.peluqueriascheduler.domain.Citas.Cita;
import com.unax.peluqueriascheduler.domain.Citas.CitaRequest;
import com.unax.peluqueriascheduler.domain.Huecos.Hueco;
import com.unax.peluqueriascheduler.domain.Usuarios.Cliente;
import com.unax.peluqueriascheduler.service.CitasService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;








@RestController
@RequestMapping("/cliente")
public class ClienteController{

    private final CitasService citasService; 
    ClienteController(CitasService citasService){
        this.citasService = citasService;
    }

    
    @GetMapping("/citas/proximas/{semanas}")
    List<Cita> getProximasCitas(Cliente cliente,@PathVariable @Positive @Max(10)  int semanas){
        return citasService.getproximasCitas(cliente,semanas);
    }
    @GetMapping("/cita/id/{id}")
    public Cita getCitaById(Cliente cliente, @PathVariable @Positive int id) {
        return  citasService.getCitaById(cliente,id);
    }
    @PutMapping("/cita/id/{id}/confimar")
    public Cita confirmarCita(Cliente cliente, @PathVariable @Positive int id){
        return citasService.confirmarCita(cliente,id);
    }
    @PutMapping("/cita/id/{id}/cancelar")
    public Cita cancelarCita(Cliente cliente, @PathVariable @Positive int id){
        return citasService.cancelarCita(cliente,id);
    }
    @PostMapping("/agendarCita/buscarOpciones")
    public List<Hueco> huecosByCitaFilter(@RequestBody HuecoFilter filter) {
        return citasService.getHuecosDisponiblesByFilter(filter);
    }
    @PostMapping("/agendarCita")
    public Cita postAgendarCita(@RequestBody CitaRequest request,Cliente cliente) {
        return citasService.agendarCita(request,cliente);
    }
    

    
    
    
} 