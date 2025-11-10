package com.petcare.PetCare.Controller;


import com.petcare.PetCare.DTO.AgendaDTO;
import com.petcare.PetCare.Model.Agenda;
import com.petcare.PetCare.Service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/{id}")
    public ResponseEntity<AgendaDTO> atualizar(
            @PathVariable Long id,
            @RequestBody AgendaDTO dto) {

        Agenda agendaParaAtualizar = agendaService.converterDTOParaEntidade(dto);


        Agenda agendaAtualizada = agendaService.atualizar(id, agendaParaAtualizar);

        AgendaDTO resposta = new AgendaDTO(
                agendaAtualizada.getId(),
                agendaAtualizada.getTutor().getId(),
                agendaAtualizada.getAnimal().getId(),
                agendaAtualizada.getMedicamento().getId(),
                agendaAtualizada.getDataInicio(),
                agendaAtualizada.getObservacao()
        );

        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        agendaService.excluir(id);
        return ResponseEntity.noContent().build();
    }



}