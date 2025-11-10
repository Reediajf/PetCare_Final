package com.petcare.PetCare.Controller;


import com.petcare.PetCare.DTO.AgendaDTO;
import com.petcare.PetCare.Model.Agenda;
import com.petcare.PetCare.Service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agedamentos")
public class AgendaController {
    @Autowired
    private AgendaService agendaService;

    @PostMapping
    public Agenda criar(@RequestBody AgendaDTO dto) {
        return agendaService.criarAgendamento(dto);
    }

    @GetMapping
    public List<Agenda> listar() {
        return agendaService.listarAgendamentos();
    }
}
