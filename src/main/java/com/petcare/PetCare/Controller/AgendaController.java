package com.petcare.PetCare.Controller;

import com.petcare.PetCare.DTO.AgendaDTO;
import com.petcare.PetCare.Model.Agenda;
import com.petcare.PetCare.Service.AgendaService;
import com.petcare.PetCare.Util.AgendaPdfGenerator;
import com.petcare.PetCare.Util.HorarioOcupadoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agendamentos")
public class AgendaController {

    private final AgendaService agendaService;
    private final AgendaPdfGenerator agendaPdfGenerator;


    public AgendaController(AgendaService agendaService, AgendaPdfGenerator agendaPdfGenerator1) {
        this.agendaService = agendaService;
        this.agendaPdfGenerator = agendaPdfGenerator1;
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

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> gerarPdf() {

        List<AgendaDTO> lista = agendaService.listarAgendamentos()
                .stream()
                .map(this::toDTO)
                .toList();

        ByteArrayInputStream pdf = agendaPdfGenerator.gerarPdf(lista);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=agendamentos.pdf")
                .body(pdf.readAllBytes());
    }

    @ExceptionHandler(HorarioOcupadoException.class)
    public ResponseEntity<String> handleHorarioOcupado(HorarioOcupadoException ex) {
        return ResponseEntity.status(409).body(ex.getMessage());
    }


}
