package com.petcare.PetCare.Controller;

import com.petcare.PetCare.DTO.MedicamentoDTO;
import com.petcare.PetCare.Model.Medicamento;
import com.petcare.PetCare.Service.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/medicamento")
public class MedicamentoController {

    @Autowired
    private final MedicamentoService medicamentoService;

    public MedicamentoController(MedicamentoService medicamentoService) {
        this.medicamentoService = medicamentoService;
    }

    @PostMapping
    public ResponseEntity<MedicamentoDTO> cadastrarMedicamento(@RequestBody MedicamentoDTO medicamentoDTO){
        Medicamento medicamento = medicamentoService.converterDTOparaEntidade(medicamentoDTO);

        Medicamento salvo = medicamentoService.cadastrar(medicamento);

        MedicamentoDTO resposta = new MedicamentoDTO(salvo.getNome(), salvo.getDosagem(), salvo.getViaAdministracao());
        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicamentoDTO> atualizarMedicamento(@PathVariable Long id, @RequestBody MedicamentoDTO medicamentoDTO){

        Medicamento medicamentoAtualizado = medicamentoService.converterDTOparaEntidade(medicamentoDTO);
        Medicamento medicamento = medicamentoService.atualizar(id, medicamentoAtualizado);

        MedicamentoDTO resposta = new MedicamentoDTO(medicamento.getNome(), medicamento.getDosagem(), medicamento.getViaAdministracao());
        return ResponseEntity.ok(resposta);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMedicamento(@PathVariable Long id){
        medicamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MedicamentoDTO>> listarMedicamentos(){

        List<MedicamentoDTO> lista = medicamentoService.listarMedicamentos()
                .stream()
                .map(medicamento -> new MedicamentoDTO(
                        medicamento.getNome(),
                        medicamento.getDosagem(),
                        medicamento.getViaAdministracao()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);

    }

}
