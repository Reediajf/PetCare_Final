package com.petcare.PetCare.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "medicamentos")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String dosagem;
    private String viaAdministracao;

    @OneToMany(mappedBy = "medicamento", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Agenda> agendamentos;

}
