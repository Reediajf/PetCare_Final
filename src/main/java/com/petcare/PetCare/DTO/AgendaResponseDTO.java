package com.petcare.PetCare.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendaResponseDTO {

    private Long id;
    private String tutorNome;
    private String animalNome;
    private String medicamentoNome;
    private String observacao;
    private LocalDateTime dataInicio;

}
