package com.petcare.PetCare.Service;

import com.petcare.PetCare.DTO.AgendaDTO;
import com.petcare.PetCare.Model.*;
import com.petcare.PetCare.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
        Agenda agenda = converterDTOParaEntidade(dto);
        return agendaRepository.save(agenda);
    }

    public java.util.List<Agenda> listarAgendamentos() {
        return agendaRepository.findAll();
    }

    public Agenda atualizar(Long id, Agenda novaAgenda) {
        Agenda existente = agendaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento n達o encontrado com ID: " + id));

        // Atualiza os campos
        existente.setDataInicio(novaAgenda.getDataInicio());
        existente.setObservacao(novaAgenda.getObservacao());
        existente.setTutor(novaAgenda.getTutor());
        existente.setAnimal(novaAgenda.getAnimal());
        existente.setMedicamento(novaAgenda.getMedicamento());

        return agendaRepository.save(existente);
    }

    public Agenda converterDTOParaEntidade(AgendaDTO dto) {
        Agenda agenda = new Agenda();

        if (dto.getId() != null) {
            agenda.setId(dto.getId());
        }

        agenda.setDataInicio(dto.getDataInicio());
        agenda.setObservacao(dto.getObservacao());

        if (dto.getTutorId() != null) {
            Tutor tutor = tutorRepository.findById(dto.getTutorId())
                    .orElseThrow(() -> new EntityNotFoundException("Tutor n達o encontrado"));
            agenda.setTutor(tutor);
        }

        if (dto.getAnimalId() != null) {
            Animal animal = animalRepository.findById(dto.getAnimalId())
                    .orElseThrow(() -> new EntityNotFoundException("Animal n達o encontrado"));
            agenda.setAnimal(animal);
        }

        if (dto.getMedicamentoId() != null) {
            Medicamento medicamento = medicamentoRepository.findById(dto.getMedicamentoId())
                    .orElseThrow(() -> new EntityNotFoundException("Medicamento n達o encontrado"));
            agenda.setMedicamento(medicamento);
        }

        return agenda;
    }
    @Transactional
    public void excluir(Long id) {
        agendaRepository.deleteById(id);
    }

}