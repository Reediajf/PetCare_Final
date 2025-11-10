package com.petcare.PetCare.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MedicamentoDTO {
    private String nome;
    private String dosagem;
    private String viaAdministracao;

}
