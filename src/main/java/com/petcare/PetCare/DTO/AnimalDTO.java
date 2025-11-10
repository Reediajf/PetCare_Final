package com.petcare.PetCare.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@AllArgsConstructor
@Getter
@Setter
public class AnimalDTO {

    private String nome;
    private String raca;
    private Float peso;
    private Date dataNascimento;


}
