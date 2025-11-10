package com.petcare.PetCare.Controller;

import com.petcare.PetCare.DTO.AnimalDTO;
import com.petcare.PetCare.Model.Animal;
import com.petcare.PetCare.Service.AnimalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/animais")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PostMapping
    public ResponseEntity<AnimalDTO> cadastrarAnimal(@RequestBody AnimalDTO animalDTO) {
        Animal animal = animalService.converterDtoParaEntidade(animalDTO);
        Animal salvo = animalService.cadastrar(animal);

        AnimalDTO resposta = new AnimalDTO(
                salvo.getNome(),
                salvo.getRaca(),
                salvo.getPeso(),
                salvo.getDataNascimento()
        );

        return ResponseEntity.ok(resposta);
    }


    @PutMapping("/{id}")
    public ResponseEntity<AnimalDTO> atualizarAnimal(
            @PathVariable Long id,
            @RequestBody AnimalDTO animalDTO) {

        Animal animalAtualizado = animalService.converterDtoParaEntidade(animalDTO);
        Animal atualizado = animalService.atualizar(id, animalAtualizado);

        AnimalDTO resposta = new AnimalDTO(
                atualizado.getNome(),
                atualizado.getRaca(),
                atualizado.getPeso(),
                atualizado.getDataNascimento()
        );

        return ResponseEntity.ok(resposta);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAnimal(@PathVariable Long id) {
        animalService.excluir(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<List<AnimalDTO>> listarAnimais() {
        List<AnimalDTO> lista = animalService.listarAnimais()
                .stream()
                .map(animal -> new AnimalDTO(
                        animal.getNome(),
                        animal.getRaca(),
                        animal.getPeso(),
                        animal.getDataNascimento()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<AnimalDTO> cadastrarAnimalParaUsuario(
            @PathVariable Long usuarioId,
            @RequestBody AnimalDTO animalDTO) {

        Animal animal = animalService.converterDtoParaEntidade(animalDTO);
        Animal salvo = animalService.cadastrarParaUsuario(animal, usuarioId);

        AnimalDTO resposta = new AnimalDTO(
                salvo.getNome(),
                salvo.getRaca(),
                salvo.getPeso(),
                salvo.getDataNascimento()
        );

        return ResponseEntity.ok(resposta);
    }

}
