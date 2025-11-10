package com.petcare.PetCare.Controller;


import com.petcare.PetCare.DTO.TutorDTO;
import com.petcare.PetCare.Model.Tutor;
import com.petcare.PetCare.Service.TutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tutores")
public class TutorController {
    private TutorService tutorService;
    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @PostMapping
    public ResponseEntity<TutorDTO> save(@RequestBody TutorDTO tutorDTO) {
        Tutor tutor = tutorService.converterDtoParaEntidade(tutorDTO);
        Tutor salvo = tutorService.cadastrar(tutor);
        TutorDTO resposta = new TutorDTO(
                salvo.getId(),
                salvo.getNome(),
                salvo.getEmail()
        );
        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TutorDTO> atualizarTutor(
            @PathVariable Long id,
            @RequestBody TutorDTO tutorDTO) {

        Tutor tutorAtualizado = tutorService.converterDtoParaEntidade(tutorDTO);


        Tutor atualizado = tutorService.Atualizar(id, tutorAtualizado);

        TutorDTO resposta = new TutorDTO(
                atualizado.getId(),
                atualizado.getNome(),
                atualizado.getEmail()
        );

        return ResponseEntity.ok(resposta);
    }

    @GetMapping
    public ResponseEntity<List<TutorDTO>> listarTutor() {
        List<TutorDTO> lista = tutorService.listarTutores()
                .stream()
                .map(tutor -> new TutorDTO(
                        tutor.getId(),
                        tutor.getNome(),
                        tutor.getEmail()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TutorDTO> buscarTutorPorId(@PathVariable Long id) {
        Tutor tutor = tutorService.buscarPorId(id);

        TutorDTO resposta = new TutorDTO(
                tutor.getId(),
                tutor.getNome(),
                tutor.getEmail()

        );

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<TutorDTO> buscarTutorPorEmail(@PathVariable String email) {
        Tutor tutor = tutorService.buscarPorEmail(email);

        TutorDTO resposta = new TutorDTO(
                tutor.getId(),
                tutor.getNome(),
                tutor.getEmail()

        );

        return ResponseEntity.ok(resposta);
    }

}
