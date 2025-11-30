package com.petcare.PetCare.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "agendas")
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dataInicio;
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "medicamento_id")
    @JsonIgnore
    private Medicamento medicamento;


    @Override
    public String toString() {
        return "Agenda {" +
                "\n  Medicamento: " + (medicamento != null ? medicamento.getNome() : "N/A") +
                "\n  Animal: " + (animal != null ? animal.getNome() : "N/A") +
                "\n  Tutor: " + (tutor != null ? tutor.getNome() : "N/A") +
                "\n  Observação: '" + (observacao != null ? observacao : "") + "'" +
                "\n  Data de Início: " + (dataInicio != null ? dataInicio : "N/A") +
                "\n}";
    }

}