package com.petcare.PetCare.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class AgendaDTO {
    private Long tutorId;
    private Long animalId;
    private Long medicamentoId;
    private LocalDateTime dataInicio;
    private String observacao;
}
