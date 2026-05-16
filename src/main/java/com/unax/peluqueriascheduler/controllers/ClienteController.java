package com.unax.peluqueriascheduler.controllers;
import com.unax.peluqueriascheduler.domain.Citas.Cita;
import com.unax.peluqueriascheduler.domain.Usuarios.Cliente;
import com.unax.peluqueriascheduler.service.CitasService;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/cliente")
public class ClienteController{

    private final CitasService citasService; 
    ClienteController(CitasService citasService){
        this.citasService = citasService;
    }

    
    @GetMapping("/{semanas}")
    List<Cita> getProximasCitas(Cliente cliente,@PathVariable int semanas){
        return citasService.getproximasCitas(cliente,semanas);
    }
} 