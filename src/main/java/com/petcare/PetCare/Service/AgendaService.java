package com.petcare.PetCare.Service;

import com.petcare.PetCare.DTO.AgendaDTO;
import com.petcare.PetCare.Model.*;
import com.petcare.PetCare.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    public Agenda criarAgendamento(AgendaDTO dto) {
        Optional<Tutor> tutorOptional = tutorRepository.findById(dto.getTutorId());
        Optional<Animal> animalOpt = animalRepository.findById(dto.getAnimalId());
        Optional<Medicamento> medicamentoOpt = medicamentoRepository.findById(dto.getMedicamentoId());

        if (tutorOptional.isEmpty() || animalOpt.isEmpty() || medicamentoOpt.isEmpty()) {
            throw new RuntimeException("Tutor, Animal ou Medicamento não encontrados!");
        }

        Animal animal = animalOpt.get();
        Tutor tutor = tutorOptional.get();

        if (!animal.getTutor().getId().equals(tutor.getId())) {
            throw new RuntimeException("Este animal não pertence ao tutor informado!");
        }

        Agenda agenda = new Agenda();
        agenda.setTutor(tutor);
        agenda.setAnimal(animal);
        agenda.setMedicamento(medicamentoOpt.get());
        agenda.setDataInicio(dto.getDataInicio());
        agenda.setObservacao(dto.getObservacao());

        return agendaRepository.save(agenda);
    }

    public java.util.List<Agenda> listarAgendamentos() {
        return agendaRepository.findAll();
    }
}
