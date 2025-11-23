package com.petcare.PetCare.Controller;

import com.petcare.PetCare.DTO.AgendaDTO;
import com.petcare.PetCare.Model.Agenda;
import com.petcare.PetCare.Service.AgendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agendamentos")
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }


    @PostMapping
    public ResponseEntity<AgendaDTO> criar(@RequestBody AgendaDTO dto) {
        Agenda agenda = agendaService.criarAgendamento(dto);

        return ResponseEntity.ok(toDTO(agenda));
    }


    @GetMapping
    public ResponseEntity<List<AgendaDTO>> listar() {

        List<AgendaDTO> dtos = agendaService.listarAgendamentos()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }


    @PutMapping("/{id}")
    public ResponseEntity<AgendaDTO> atualizar(
            @PathVariable Long id,
            @RequestBody AgendaDTO dto) {

        Agenda agendaParaAtualizar = agendaService.converterDTOParaEntidade(dto);
        Agenda agendaAtualizada = agendaService.atualizar(id, agendaParaAtualizar);

        return ResponseEntity.ok(toDTO(agendaAtualizada));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        agendaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private AgendaDTO toDTO(Agenda agenda) {
        return new AgendaDTO(
                agenda.getId(),
                agenda.getTutor() != null ? agenda.getTutor().getId() : null,
                agenda.getAnimal() != null ? agenda.getAnimal().getId() : null,
                agenda.getMedicamento() != null ? agenda.getMedicamento().getId() : null,
                agenda.getDataInicio(),
                agenda.getObservacao()
        );
    }
}
